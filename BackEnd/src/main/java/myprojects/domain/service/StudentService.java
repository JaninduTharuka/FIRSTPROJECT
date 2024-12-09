package myprojects.domain.service;

import myprojects.domain.entity.Student;
import myprojects.external.repository.StudentRepository;

import java.sql.SQLException;
import java.util.Optional;

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Student> getStudent(int id) throws SQLException {
        System.out.println("Came to Student Service"); // ******************************
        System.out.println(id); // ******************************************************
        return studentRepository.findById(id);
    }

    public void addStudent(String name, int age) throws SQLException {
        System.out.println(name + " " + age);
        studentRepository.addStudent(name, age);
    }
}
