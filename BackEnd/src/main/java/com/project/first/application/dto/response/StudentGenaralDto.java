package com.project.first.application.dto.response;

import java.util.Date;

public class StudentGenaralDto {
    private String name;
    private Date dob;
    private String teacherName;

    public StudentGenaralDto(String name, Date dob, String teacherName) {
        this.name = name;
        this.dob = dob;
        this.teacherName = teacherName;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}

