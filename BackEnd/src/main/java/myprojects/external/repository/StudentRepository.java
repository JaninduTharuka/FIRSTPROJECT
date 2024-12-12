package myprojects.external.repository;

import myprojects.domain.entity.Student;

import java.sql.*;
import java.util.Optional;
import java.util.Properties;

public class StudentRepository {
    private static final String db_url = "jdbc:postgresql://localhost:5432/student_db";
    private static final String db_user = "postgres";
    private static final String db_password = "2001";

    private Connection getConnection() throws SQLException {
        try {
            // Explicitly load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.setProperty("user", db_user);
            props.setProperty("password", db_password);

            System.out.println("Connecting to database...");

            // Try to establish a connection to the database
            Connection conn = DriverManager.getConnection(db_url, props);
            System.out.println("Connected to the database successfully!");
            return conn;

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL driver not found: " + e.getMessage());
            throw new SQLException("PostgreSQL driver not found.");
        } catch (SQLException e) {
            System.out.println("Could not connect to the database: " + e.getMessage());
            throw e; // Rethrow the SQLException to be handled elsewhere
        }
    }

    public Optional<Student> findById(int id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Executing query: " + sql + " with parameters: " + id);

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mapping the ResultSet to a Student object
                    int studentId = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    Student student = new Student(studentId, name, age);
                    System.out.println("Found student by,   name: " + student.getName() + " age: " + student.getAge());
                    return Optional.of(student); // Return the Student wrapped in Optional
                }
            }
        }
        return Optional.empty(); // Return an empty Optional if no student is found
    }

    public void addStudent(String name, int age) throws SQLException {
        String sql = "INSERT INTO student (name, age) VALUES (?, ?)";
        System.out.println("Came to repositary..........."); // ***************************************
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding student: "+e.getMessage());
            throw e;
        }
    }
}
