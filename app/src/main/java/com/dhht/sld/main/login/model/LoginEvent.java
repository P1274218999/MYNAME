package com.dhht.sld.main.login.model;

public class LoginEvent {
    public String phone;

    public LoginEvent(String phone)
    {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
