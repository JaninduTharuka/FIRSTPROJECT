package com.project.first.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Student {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date dob;
    private String teachername;

    public Student(Integer id, String name, Date dob, String teachername) {
        this.id = id;
        this.name = name;
        this.dob = dob;
    }
    public Student() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
