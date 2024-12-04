package com.project.first.application.dto.request;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class StudentCreateDto {
    private String name;
    private  Date dob;

    public StudentCreateDto(String name, Date dob, String email, String password) {
        this.name = name;
        this.dob = dob;
    }
    public StudentCreateDto() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }
}
