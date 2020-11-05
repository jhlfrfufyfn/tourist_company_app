package com.bsu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Tour {

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public Tour() {
        this.id = "";
    }

    public static final Tour INCORRECT_TOUR = new Tour();



    public Tour(String id, String companyName, String tourNumber, String tourName,
                String startDate, String endDate, String category, double price, double rating) throws ParseException {
        this.id = id;
        this.companyName = companyName;
        this.tourNumber = tourNumber;
        this.tourName = tourName;
        this.startDate = FORMAT.parse(startDate);
        this.endDate = FORMAT.parse(endDate);
        this.category = category;
        this.price = price;
        this.rating = rating;
    }

    public Tour(String []data) throws ParseException {
        this(data[0],data[1],data[2],data[3],data[4],data[5]
                ,data[6], Double.parseDouble(data[7]),Double.parseDouble(data[8]));
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id='" + id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", tourNumber='" + tourNumber + '\'' +
                ", tourName='" + tourName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTourNumber() {
        return tourNumber;
    }

    public void setTourNumber(String tourNumber) {
        this.tourNumber = tourNumber;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) throws ParseException {
        this.startDate = FORMAT.parse(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        this.endDate = FORMAT.parse(endDate);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return Double.compare(tour.price, price) == 0 &&
                Double.compare(tour.rating, rating) == 0 &&
                Objects.equals(id, tour.id) &&
                Objects.equals(companyName, tour.companyName) &&
                Objects.equals(tourNumber, tour.tourNumber) &&
                Objects.equals(tourName, tour.tourName) &&
                Objects.equals(startDate, tour.startDate) &&
                Objects.equals(endDate, tour.endDate) &&
                Objects.equals(category, tour.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, tourNumber, tourName, startDate, endDate, category, price, rating);
    }

    private String id;
    private String companyName;
    private String tourNumber;
    private String tourName;
    private Date startDate;
    private Date endDate;
    private String category;
    private double price;
    private double rating;
}
