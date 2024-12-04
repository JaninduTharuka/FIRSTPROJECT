package com.project.first.application.controllers;

import com.project.first.application.dto.request.StudentCreateDto;
import com.project.first.application.dto.response.StudentGenaralDto;
import com.project.first.domain.entity.Student;
import com.project.first.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:3000")
//@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getStudent")
    public ResponseEntity<StudentGenaralDto> getStudent(@RequestParam int id) {
        return studentService.getStudent(id);
    }

    @PostMapping("/addStudent")
    public ResponseEntity<StudentGenaralDto> addStudent(@RequestBody StudentCreateDto studentCreateDto) {
        System.out.println(studentCreateDto);
        return studentService.addStudent(studentCreateDto);
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudent(@RequestParam int id) {
        return studentService.deleteStudent(id);
    }

    @PutMapping("/updateStudentName")
    ResponseEntity<String> updateStudentName(@RequestParam int id, @RequestParam String newName) {
        return studentService.updateStudentName(id, newName);
    }
}
