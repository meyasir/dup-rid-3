package com.darewrorider.Models;

/**
 * Created by Jaffar on 2018-02-20.
 */

public class User {
    int id;
    String number;
    String username;
    String password;
    String tokenKey;
    String email;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(int id, String number, String username, String password, String tokenKey, String email, String title) {
        this.id = id;
        this.number = number;
        this.username = username;
        this.password = password;
        this.tokenKey = tokenKey;
        this.email = email;
        this.title = title;
    }

    public User() {
    }
}
