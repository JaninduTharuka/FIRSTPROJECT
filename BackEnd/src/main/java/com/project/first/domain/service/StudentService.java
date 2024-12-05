package com.project.first.domain.service;

import com.project.first.application.dto.request.StudentCreateDto;
import com.project.first.application.dto.response.StudentGenaralDto;
import com.project.first.domain.entity.Student;
import com.project.first.domain.exception.StudentNotFoundException;
import com.project.first.external.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public ResponseEntity<StudentGenaralDto> getStudent(int id) {
        Optional<Student> student = studentRepository.getStudent(id);
        StudentGenaralDto studentGenaralDto = new StudentGenaralDto();
        if (student.isPresent()) {
            Student studentEntity = student.get();
            studentGenaralDto.setName(studentEntity.getName());
            studentGenaralDto.setDob(studentEntity.getDob());
            return ResponseEntity.ok(studentGenaralDto);
        } else {
            throw new StudentNotFoundException("Student not found with id " + id);
        }
    }

    public ResponseEntity<StudentGenaralDto> addStudent(StudentCreateDto studentCreateDto) {
        Student student = new Student();
        student.setName(studentCreateDto.getName());
        student.setDob(studentCreateDto.getDob());
        System.out.println(studentCreateDto.getTeacherName());
        student.setTeacherName(studentCreateDto.getTeacherName());
        int id = studentRepository.addStudent(student);
        System.out.println(id);

        Student studentEntity = studentRepository.getStudent(id).get();
        StudentGenaralDto studentGenaralDto = new StudentGenaralDto();
        studentGenaralDto.setName(studentEntity.getName());
        studentGenaralDto.setDob(studentEntity.getDob());
        studentGenaralDto.setTeacherName(studentEntity.getTeacherName());
        return ResponseEntity.ok(studentGenaralDto);
    }

    public ResponseEntity<String> deleteStudent(int id) {
        Optional<Student> student = studentRepository.getStudent(id);
        if (student.isPresent()) {
            studentRepository.deleteStudent(id);
            return ResponseEntity.ok("Student deleted");
        }
        throw new StudentNotFoundException("Student not Found while Delete");
    }

    public ResponseEntity<String> updateStudentName(int id, String newName) {
        Optional<Student> student = studentRepository.getStudent(id);
        if (student.isPresent()) {
            student.get().setName(newName);
            studentRepository.addStudent(student.get());
            return ResponseEntity.ok("Student Name updated");
        } else {
            throw new StudentNotFoundException("Student not Found while Update");
        }
    }
}
