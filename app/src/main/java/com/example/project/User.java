package com.example.project;

public class User {
    public String userFirstName;
    public String userLastName;
    public String userEmail;
    public String userPassword;
    public User(){

    }
    public User(String userFirstName,String userLastName, String userEmail, String userPassword){
        this.userFirstName=userFirstName;
        this.userLastName=userLastName;
        this.userEmail=userEmail;
        this.userPassword=userPassword;
    }
}
