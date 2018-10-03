package com.blogspot.chunkingz.eat_it2.Model;

public class User {
    private String name;
    private String password;
    private String address;
    private String email;
    private String secureCode;
    private String phoneNumber;
    private String birthday;
    private String weddingAnniversary;

    public  User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String password, String address, String email, String secureCode, String phoneNumber, String birthday, String weddingAnniversary) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.email = email;
        this.secureCode = secureCode;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.weddingAnniversary = weddingAnniversary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWeddingAnniversary() {
        return weddingAnniversary;
    }

    public void setWeddingAnniversary(String weddingAnniversary) {
        this.weddingAnniversary = weddingAnniversary;
    }

}
