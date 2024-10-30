package com.fyp.prosafetyapps;

public class UserInformation {

    public String name;
    public String surname;

    public UserInformation(){
    }

    public UserInformation(String name,String surname){
        this.name = name;
        this.surname = surname;
    }
    public String getUserName() {
        return name;
    }
    public String getUserSurname() {
        return surname;
    }
}

