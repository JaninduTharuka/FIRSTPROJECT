package myprojects.application.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import myprojects.domain.entity.Student;
import myprojects.domain.service.StudentService;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StudentController implements HttpHandler {


    private  final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Access-Control-Max-Age", "3600");
    }


    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        System.out.println("Got a request.......................");

        String method = exchange.getRequestMethod();

        switch (method) {
            case "OPTIONS":
                handleOptions(exchange);
                return;
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
//            case "PUT":
//                handlePut(exchange);
//                break;
//            case "DELETE":
//                handleDelete(exchange);
//                break;
            default:
                sendResponse(exchange, "{\"error\": \"Unsupported Request: " + method + "\"}", 405);
        }
    }

    private void handleOptions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(204, -1);            // No content for OPTIONS
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQueryParams(query);
            String idParam = params.get("id");

            if (idParam != null) {
                int studentId = Integer.parseInt(idParam);
                Optional<Student> student = studentService.getStudent(studentId);
                if (student.isPresent()) {
                    Student studentGot = student.get();
                    String response = String.format(
                            "{\"id\": %d, \"name\": \"%s\", \"age\": %d}",
                            studentGot.getId(), studentGot.getName(), studentGot.getAge()
                    );
                    sendResponse(exchange, response, 200);
                } else {
                    sendResponse(exchange, "{\"error\": \"Not Found\"}", 404);
                }
            } else {
                sendResponse(exchange, "{\"error\": \"Missing 'id' parameter\"}", 400);
            }
        } catch (Exception e) {
            sendResponse(exchange, "{\"error\": \"" + e.getMessage() + "\"}", 500);
        }
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        System.out.println("Got a Post Request..............");
        String request = new String(exchange.getRequestBody().readAllBytes());
        System.out.println(request);

        String name = null;
        int age = -1;

        try {
            System.out.println("***********************************************");
            if (request.contains("name")) {
                name = request.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
            }
            if (request.contains("age")) {
                age = Integer.parseInt(request.split("\"age\"\\s*:\\s*\"")[1].split("\"")[0]);
            }
        } catch (Exception e) {
            System.out.println(e);
            sendResponse(exchange, "Invalid JSON format", 400);
            return;
        }

        System.out.println(name + " " + age);

        if (name == null || age < 0) {
            sendResponse(exchange, "Invalid Name/Age", 400);
            return;
        }
        try {
            studentService.addStudent(name, age);
            sendResponse(exchange, "Success", 200);
        } catch (SQLException e) {
            sendResponse(exchange, e.getMessage(), 500);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int status) throws IOException {
        exchange.sendResponseHeaders(status, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}