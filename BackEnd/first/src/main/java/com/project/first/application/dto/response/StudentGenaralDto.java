package com.project.first.application.dto.response;

import java.util.Date;

public class StudentGenaralDto {
    private String name;
    private Date dob;

    public StudentGenaralDto(String name, Date dob) {
        this.name = name;
        this.dob = dob;
    }

    public StudentGenaralDto() {
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
