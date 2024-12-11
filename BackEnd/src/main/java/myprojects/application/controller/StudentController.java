package myprojects.application.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myprojects.domain.entity.Student;
import myprojects.domain.service.StudentService;
import myprojects.external.repository.StudentRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/students")
public class StudentController extends HttpServlet {

    private final StudentService studentService;

    public StudentController() {
        this.studentService = new StudentService(new StudentRepository());
    }

    // Service method to catch all requests.
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Request received:");
        System.out.println("Method: " + req.getMethod());
        System.out.println("Request URI: " + req.getRequestURI());
        System.out.println("Query String: " + req.getQueryString());

        // Log headers for debugging
        req.getHeaderNames().asIterator().forEachRemaining(header ->
                System.out.println(header + ": " + req.getHeader(header))
        );

        super.service(req, resp);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Get request received.....");

        // Allow cross-origin requests
        addCorsHeaders(resp);

        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                int studentId = Integer.parseInt(idParam);
                Optional<Student> student = studentService.getStudent(studentId);
                if (student.isPresent()) {
                    Student studentGot = student.get();
                    String response = String.format(
                            "{\"id\": %d, \"name\": \"%s\", \"age\": %d}",
                            studentGot.getId(), studentGot.getName(), studentGot.getAge()
                    );
                    sendResponse(resp, response, 200);
                } else {
                    sendResponse(resp, "{\"error\": \"Student not found\"}", 404);
                }
            } else {
                sendResponse(resp, "{\"error\": \"Missing 'id' parameter\"}", 400);
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, "{\"error\": \"Invalid 'id' parameter\"}", 400);
        } catch (SQLException e) {
            logError(e, "Error fetching student by ID");
            sendResponse(resp, "{\"error\": \"Internal Server Error\"}", 500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Post request received.....");

        // Allow cross-origin requests
        addCorsHeaders(resp);

        String requestBody = new String(req.getInputStream().readAllBytes());
        String name = null;
        int age = -1;

        System.out.println(requestBody);

        try {
            if (requestBody.contains("name")) {
                name = requestBody.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
                System.out.println(name);
            }
            if (requestBody.contains("age")) {
                System.out.println("dddddddddddddddddddddddddddddddddddddddddddddd");
                System.out.println(requestBody.split("\"age\"\\s*:\\s*\"")[1]);
                age = Integer.parseInt(requestBody.split("\"age\"\\s*:\\s*\"")[1].split("\"")[0]);
                System.out.println(age);
            }
        } catch (Exception e) {
            sendResponse(resp, "{\"error\": \"Invalid JSON format\"}", 400);
            return;
        }

        if (name == null || age < 0) {
            System.out.println("Name and age are empty");
            sendResponse(resp, "{\"error\": \"Invalid name or age\"}", 400);
            return;
        }

        try {
            System.out.println("name = " + name+ " age = " + age);
            studentService.addStudent(name, age);
            sendResponse(resp, "{\"message\": \"Student added successfully\"}", 200);
        } catch (SQLException e) {
            logError(e, "Error adding student");
            sendResponse(resp, "{\"error\": \"Internal Server Error\"}", 500);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Options request received.....");
        addCorsHeaders(resp);
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With"); // Add all necessary headers
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private void addCorsHeaders(HttpServletResponse resp) {
        System.out.println("adding CORS headers.....");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");  // Frontend URL
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // Allowed methods
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, application/json, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");  // Cache preflight request for 1 hour
    }

    private void sendResponse(HttpServletResponse resp, String response, int status) throws IOException {
        System.out.println("sending response....");
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.getWriter().write(response);
    }

    private void logError(Exception e, String message) {
        System.err.println(message);
        e.printStackTrace();
    }
}