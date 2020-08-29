package com.example.khushbhu.go_dutch;

public class str {
    String strid;
    String person;
    String phone;
    int balance=0;

    public str()
    {

    }

    public str(String strid, String person,String phone, int balance) {
        this.strid = strid;
        this.person = person;
        this.phone=phone;
        this.balance = balance;
    }

    public String getStrid() {
        return strid;
    }

    public String getPerson() {
        return person;
    }

    public String getPhone() {
        return phone;
    }


    public int getBalance() {
        return balance;
    }
}
