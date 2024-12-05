package com.project.first.application.dto.request;

import java.util.Date;

public class StudentCreateDto {
    private String name;
    private  Date dob;
    private String teachername;

    public StudentCreateDto(String name, Date dob, String teachername) {
        this.name = name;
        this.dob = dob;
        this.teachername = teachername;
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
    public String getTeacherName() {
        return teachername;
    }
    public void setTeacherName(String teachername) {
        this.teachername = teachername;
    }
}