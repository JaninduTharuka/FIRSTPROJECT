//package com.project.first.external.repository;
//
//import com.project.first.domain.entity.Student;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface StudentRepository extends JpaRepository<Student, Integer> {
//}


// use JDBC template to database operations

package com.project.first.external.repository;

import com.project.first.domain.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Add a student to the database
//    public int addStudent(Student student) {
//        String sql = "INSERT INTO student (name, dob) VALUES (?, ?) RETURNING id";
//        int id = jdbcTemplate.queryForObject(sql, new Object[]{student.getName(), student.getDob()}, Integer.class);
//        System.out.println(id);
//        return id;
//    }
    @Transactional
    public int addStudent(Student student) {
        // Step 1: Check if the teacher exists
        String teacherCheckSql = "SELECT id FROM teacher WHERE name = ?";
        Integer teacherId;
        try {
            teacherId = jdbcTemplate.queryForObject(teacherCheckSql, new Object[]{student.getTeacherName()}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            // Step 2: Add the teacher if not found
            String teacherInsertSql = "INSERT INTO teacher (name) VALUES (?) RETURNING id";
            teacherId = jdbcTemplate.queryForObject(teacherInsertSql, new Object[]{student.getTeacherName()}, Integer.class);
        }

        // Step 3: Insert the student
        String studentInsertSql = "INSERT INTO student (name, dob, teacherName) VALUES (?, ?, ?) RETURNING id";
        int studentId = jdbcTemplate.queryForObject(
                studentInsertSql,
                new Object[]{student.getName(), student.getDob(), student.getTeacherName()},
                Integer.class
        );

        System.out.println("Student ID: " + studentId);
        return studentId;
    }

    // Get a student by ID
    public Optional<Student> getStudent(int id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        List<Student> students = jdbcTemplate.query(sql, new Object[]{id}, new StudentRowMapper());
        // use Object convertor to prevent SQL Injection.
        return students.stream().findFirst();  // Convert to Optional
    }

    // Get a student by name (or any other criteria)
    public Optional<Student> getStudentByName(String name) {
        String sql = "SELECT * FROM student WHERE name = ?";
        List<Student> students = jdbcTemplate.query(sql, new Object[]{name}, new StudentRowMapper());
        return students.stream().findFirst();  // Convert to Optional
    }

    // Delete a student by ID
    public void deleteStudent(int id) {
        String sql = "DELETE FROM student WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{id});
    }

    // Update student's name
    public void updateStudentName(Student student) {
        String sql = "UPDATE student SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, student.getName(), student.getId());
    }

    // RowMapper for Student entity
    private static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setDob(rs.getDate("dob"));
            student.setTeacherName(rs.getString("teacherName"));
            return student;
        }
    }
}
