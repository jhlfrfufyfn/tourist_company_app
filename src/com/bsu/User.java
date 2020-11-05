package com.bsu;

import java.util.Objects;

public class User {
    public User() {
        this.login = "";
        this.email = "";
        this.password = "";
        this.name = "";
        this.role = Role.USER;
    }

    public static final User INCORRECT_USER = new User();

    enum Role{
        USER, ADMIN;
    }

    private Role getRole(String s){
        if(s.equalsIgnoreCase("ADMIN")){
            return Role.ADMIN;
        }
        else{
            return Role.USER;
        }
    }

    User(String aName,String aLogin,String aEmail, String aPassword, Role aRole){
        name = aName;
        login = aLogin;
        email = aEmail;
        password = aPassword;
        role = aRole;
    }

    User(String []data){
        this(data[0],data[1],data[2],data[3],Role.USER);
        if(getRole(data[4]) == Role.ADMIN){
            setRole(Role.ADMIN);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                Objects.equals(login, user.login) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, email, password, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getLogin() {
        return login;
    }

    void setLogin(String login) {
        this.login = login;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    Role getRole() {
        return role;
    }

    void setRole(Role role) {
        this.role = role;
    }

    private String name;
    private String login;
    private String email;
    private String password;
    private Role role;
}
