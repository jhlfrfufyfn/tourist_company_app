package com.bsu;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static User currentUser;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    enum UserRequests {
        REGISTRATION(1), SIGN_IN(2), WATCH_RECORDS(3), FIND_TOUR(4), TOUR_STATS(5), TOP_N_COMPANIES(6), EXIT(7), ERROR(-1);

        private final int label;

        UserRequests(int label) {
            this.label = label;
        }

        private static UserRequests fromIntValue(int x) {
            for (UserRequests type : values()) {
                if (type.label == x) {
                    return type;
                }
            }
            return ERROR;
        }
    }

    public static void main(String[] args) {
        try (Scanner userScanner = new Scanner(new File("users.txt"))
             ; Scanner tourScanner = new Scanner(new File("tourism.txt"))
             ; Scanner consScanner = new Scanner(System.in)
             ; FileWriter userWriter = new FileWriter(new File("users.txt"), true)
        ) {
            currentUser = new User();
            Set<User> userList = new HashSet<>();
            Set<Tour> tourList = new HashSet<>();
            while (userScanner.hasNext()) {
                String[] data = userScanner.nextLine().split(";");
                userList.add(new User(data));
            }

            while (tourScanner.hasNext()) {
                String[] data = tourScanner.nextLine().split(";");
                tourList.add(new Tour(data));
            }

            boolean endProgram = false;
            do {
                if (currentUser.equals(User.INCORRECT_USER)) {
                    preLoginPrintMenu();
                    System.out.println("Enter the request number: ");
                    UserRequests request = UserRequests.fromIntValue(Integer.parseInt(consScanner.nextLine()));
                    switch (request) {
                        case REGISTRATION:
                            User newUser = userRegistration(consScanner, userList);
                            userWriter.write(newUser.getName() + ";" + newUser.getLogin() + ";"
                                    + newUser.getEmail() + ";" + newUser.getPassword()
                                    + ";" + newUser.getRole().toString() + System.lineSeparator());
                            break;
                        case SIGN_IN:
                            currentUser = signIn(consScanner, userList);
                            break;
                        case EXIT:
                            endProgram = true;
                            break;
                        case ERROR:
                            wrongRequest();
                            break;
                    }
                }
                if (!currentUser.equals(User.INCORRECT_USER)) {
                    afterLoginPrintMenu();
                    System.out.println("Enter the request number: ");
                    UserRequests request = UserRequests.fromIntValue(Integer.parseInt(consScanner.nextLine()));
                    switch (request) {
                        case WATCH_RECORDS:
                            watchRecords(tourList);
                            break;
                        case FIND_TOUR:
                            if (currentUser.getRole() != User.Role.ADMIN) {
                                System.out.println("Error: permission denied. You must be at least ADMIN to do this");
                            } else {
                                Tour tour = findTour(consScanner, tourList);
                                if (tour.equals(Tour.INCORRECT_TOUR)) {
                                    System.out.println("Tour not found");
                                } else {
                                    System.out.println(tour.toString());
                                }
                            }
                            break;
                        case TOUR_STATS:
                            System.out.println("Enter first date in format (dd-MM-yyyy): ");
                            String data = consScanner.nextLine();
                            Date firDate = new SimpleDateFormat("dd-MM-yyyy").parse(data);

                            System.out.println("Enter second date in format (dd-MM-yyyy): ");
                            data = consScanner.nextLine();
                            Date secDate = new SimpleDateFormat("dd-MM-yyyy").parse(data);

                            tourStats(firDate, secDate, tourList);
                            break;
                        case TOP_N_COMPANIES:
                            topNCompanies(consScanner, tourList);
                            break;
                        case EXIT:
                            endProgram = true;
                            break;
                        default:
                            wrongRequest();
                            break;
                    }
                }
            } while (!endProgram);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private static void tourStats(Date firDate, Date secDate, Set<Tour> tourList) {
        Set<String> usedCompanies = new HashSet<>();
        boolean companyFound = true;

        List<String> companyNames = new ArrayList<>();
        while (companyFound) {
            companyFound = false;
            for (Tour tour : tourList) {
                if (!usedCompanies.contains(tour.getCompanyName())) {
                    companyFound = true;
                    companyNames.add(tour.getCompanyName());
                    usedCompanies.add(tour.getCompanyName());
                }
            }
        }
        int n = companyNames.size();

        for (String name : companyNames) {
            List<Tour> stats = new ArrayList<>();
            Tour mostExpensive = new Tour();
            Tour mostCheap = new Tour();
            double numExp = 0.;
            double numCheap = 100000000000.;
            for (Tour tour : tourList) {
                if (tour.getStartDate().after(firDate) && tour.getEndDate().before(secDate)) {
                    stats.add(tour);
                    if (tour.getPrice() < numCheap) {
                        numCheap = tour.getPrice();
                        mostCheap = tour;
                    }
                    if (tour.getPrice() > numExp) {
                        numExp = tour.getPrice();
                        mostExpensive = tour;
                    }
                }
            }
            System.out.println("Company: " + name);
            System.out.println("Number of tours: " + stats.size() + ";");
            if (stats.size() != 0) {
                System.out.println("Most expensive: " + mostExpensive.toString());
                System.out.println("Most cheap: " + mostCheap.toString());
            }
        }
    }

    private static void topNCompanies(Scanner sc, Set<Tour> tourList) {
        System.out.print("Enter N: ");
        int N = Integer.parseInt(sc.nextLine());

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        Set<String> usedCompanies = new HashSet<>();
        boolean companyFound = true;

        List<String> companyNames = new ArrayList<>();
        while (companyFound) {
            companyFound = false;
            for (Tour tour : tourList) {
                if (!usedCompanies.contains(tour.getCompanyName())) {
                    companyFound = true;
                    companyNames.add(tour.getCompanyName());
                    usedCompanies.add(tour.getCompanyName());
                }
            }
        }
        int n = companyNames.size();
        List<Integer> numberOfGrades = new ArrayList<>(n);
        List<Double> ratingSum = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            numberOfGrades.add(0);
            ratingSum.add(0.);
        }
        for (String name : companyNames) {
            for (Tour tour : tourList) {
                if (tour.getCompanyName().equals(name) && tour.getCategory().equals(category)) {
                    int index = companyNames.indexOf(name);
                    numberOfGrades.set(index, numberOfGrades.get(index) + 1);
                    ratingSum.set(index, ratingSum.get(index) + tour.getRating());
                }
            }
        }

        for (int i = 0; i < n; i++) {
            ratingSum.set(i, ratingSum.get(i) / (double) numberOfGrades.get(i));
        }

        List<Pair<Double, String>> arr = new ArrayList<Pair<Double, String>>();
        for (int i = 0; i < n; i++) {
            arr.add(new Pair<Double, String>(ratingSum.get(i), companyNames.get(i)));
        }
        arr.sort(new PairComparator());

        for (int i = 0; i < N; i++) {
            System.out.println(arr.get(i).element1 + ": " + df2.format(arr.get(i).element0));
        }
    }

    private static Tour findTour(Scanner sc, Set<Tour> tourList) {
        System.out.println("Enter the tour number: ");
        String number = sc.nextLine();

        for (Tour tour : tourList) {
            if (tour.getId().equals(number)) {
                return tour;
            }
        }
        return Tour.INCORRECT_TOUR;
    }

    private static void preLoginPrintMenu() {
        System.out.println("-------------MENU-------------");
        System.out.println("1. Sign up");
        System.out.println("2. Sign in");
        System.out.println("7. Exit program");
        System.out.println("------------------------------");
    }

    private static void afterLoginPrintMenu() {
        System.out.println("-------------MENU-------------");
        System.out.println("3. See all tour signs");
        System.out.println("4. Find the tour by its number");
        System.out.println("5. Print statistics for each company in pediod of time");
        System.out.println("6. Print top-N companies by average rating in tour category");
        System.out.println("7. Exit program");
        System.out.println("------------------------------");
    }

    private static User userRegistration(Scanner sc, Set<User> existingUsers) {
        sc.useDelimiter("\\n"); ///might not work

        while (true) {
            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            System.out.print("Enter your e-mail: ");
            String email = sc.nextLine();

            System.out.print("Enter your login: ");
            String login = sc.nextLine();

            System.out.print("Enter your password: ");
            String password = sc.nextLine();

            User newUser = new User(name, login, email, password, User.Role.USER);

            boolean breakCycle = true;
            for (User existUser : existingUsers) {
                if (existUser.getLogin().equals(newUser.getLogin())) {
                    System.out.println("Error: user with that login already exists. Try again.");
                    breakCycle = false;
                }
                if (existUser.getEmail().equals(newUser.getEmail())) {
                    System.out.println("Error: user with that e-mail already exists. Try again.");
                    breakCycle = false;
                }
            }
            if (breakCycle) {
                return newUser;
            }
        }
    }

    private static User signIn(Scanner sc, Set<User> existingUsers) {
        while (true) {
            System.out.print("Enter your login: ");
            String login = sc.nextLine();

            System.out.print("Enter your password: ");
            String password = sc.nextLine();

            User ansUser = new User();
            for (User existUser : existingUsers) {
                if (existUser.getLogin().equals(login)) {
                    if (existUser.getPassword().equals(password)) {
                        ansUser = existUser;
                    }
                }
            }
            if (ansUser.equals(User.INCORRECT_USER)) {
                System.out.println("Error: user with this login and password not found. Please try again.");
            } else {
                return ansUser;
            }
        }
    }

    private static void watchRecords(Set<Tour> list) {
        for (Tour it : list) {
            System.out.println(it.toString());
        }
    }

    private static void wrongRequest() {
        System.out.println("Error: wrong request type. Try again.");
    }

    public static class Pair<K, V> {

        private final K element0;
        private final V element1;

        public static <K, V> Pair<K, V> createPair(K element0, V element1) {
            return new Pair<K, V>(element0, element1);
        }

        Pair(K element0, V element1) {
            this.element0 = element0;
            this.element1 = element1;
        }

        public K getElement0() {
            return element0;
        }

        public V getElement1() {
            return element1;
        }
    }

    static class PairComparator implements Comparator<Pair<Double, String>> {
        @Override
        public int compare(Pair<Double, String> c1, Pair<Double, String> c2) {
            return (int) ((c1.element0 - c2.element0)*100.);
        }
    }
}
