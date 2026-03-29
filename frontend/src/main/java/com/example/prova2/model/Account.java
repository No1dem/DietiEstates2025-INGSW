package com.example.prova2.model;

public class Account {
    private String username;

    private String password;

    private String email;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(String email) {
        this.email = email;
    }

    public Account() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
