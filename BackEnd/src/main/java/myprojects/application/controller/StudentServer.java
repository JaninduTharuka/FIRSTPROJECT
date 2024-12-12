package myprojects.application.controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;
import myprojects.domain.entity.Student;
import myprojects.domain.service.StudentService;
import myprojects.external.repository.StudentRepository;

public class StudentServer {

    private final StudentService studentService;

    public StudentServer() {
        this.studentService = new StudentService(new StudentRepository());
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            // Read the HTTP request
            String requestLine = reader.readLine();
            System.out.println("Request: " + requestLine);

            if (requestLine == null || requestLine.isEmpty()) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 3) {
                sendResponse(writer, "400 Bad Request", "{\"error\": \"Invalid request format\"}");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];

            if (method.equals("GET") && path.startsWith("/students")) {
                handleGetRequest(path, writer);
            } else if (method.equals("POST") && path.equals("/students")) {
                handlePostRequest(reader, writer);
            } else if (method.equals("OPTIONS") && path.equals("/students")) {
                handleOptionsRequest(writer);
            } else {
                sendResponse(writer, "404 Not Found", "{\"error\": \"Endpoint not found\"}");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(String path, PrintWriter writer) {
        String[] parts = path.split("\\?");
        if (parts.length < 2 || !parts[1].startsWith("id=")) {
            sendResponse(writer, "400 Bad Request", "{\"error\": \"Missing 'id' parameter\"}");
            return;
        }

        try {
            int id = Integer.parseInt(parts[1].split("=")[1]);
            Optional<Student> student = studentService.getStudent(id);
            System.out.println("Got student to server");

            if (student.isPresent()) {
                Student studentData = student.get();
                String response = String.format(
                        "{\"id\": %d, \"name\": \"%s\", \"age\": %d}",
                        studentData.getId(), studentData.getName(), studentData.getAge()
                );
                System.out.println("Sending response: \n" + response);
                sendResponse(writer, "200 OK", response);
            } else {
                sendResponse(writer, "404 Not Found", "{\"error\": \"Student not found\"}");
            }
        } catch (NumberFormatException e) {
            sendResponse(writer, "400 Bad Request", "{\"error\": \"Invalid 'id' parameter\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(writer, "500 Internal Server Error", "{\"error\": \"Server error\"}");
        }
    }

    private void handlePostRequest(BufferedReader reader, PrintWriter writer) {
        try {
            System.out.println("Got POST request");

            // Read headers (skip them)
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println("Reading Header");
                System.out.println("Header: " + line);
            }
            System.out.println("Done reading Header");

            // Read the body based on Content-Length
            StringBuilder body = new StringBuilder();
            char[] buffer = new char[1024];
            int charsRead;

            while (reader.ready() && (charsRead = reader.read(buffer)) != -1) {
                body.append(buffer, 0, charsRead);
            }
            System.out.println("Received body: " + body.toString());

            // Ensure the body is in JSON format and has content
            String requestBody = body.toString().trim();
            if (requestBody.isEmpty()) {
                sendResponse(writer, "400 Bad Request", "{\"error\": \"Empty request body\"}");
                return;
            }
            // Extract data (assuming JSON format)
            String name = null;
            int age = -1;

            // Divide Request body to name part and age part
            String[] parts = requestBody.split(",");
            for (String part : parts) {
                System.out.println("part = " + part);
            }

            if (requestBody.contains("\"name\"")) {
                name = parts[0].split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
                System.out.println("Got name: " + name);
            }
            if (requestBody.contains("\"age\"")) {
                String[] ageString = parts[1].split("\"age\"\\s*\"")[0].split("\"");
                for (int i = 0; i < ageString.length; i++) {
                    System.out.println(ageString[i]);
                }
                System.out.println("Got age: " + ageString[3]);

                age = Integer.parseInt(ageString[3]);
                System.out.println("Got age: " + age);
            }

            // Validate extracted values
            if (name == null || age < 0) {
                sendResponse(writer, "400 Bad Request", "{\"error\": \"Invalid name or age\"}");
                return;
            }

            // Call the service to add the student
            studentService.addStudent(name, age);
            sendResponse(writer, "200 OK", "{\"message\": \"Student added successfully\"}");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            sendResponse(writer, "500 Internal Server Error", "{\"error\": \"Server error\"}");
        }
    }


    private void handleOptionsRequest(PrintWriter writer) {
        // Send a successful response to the OPTIONS request with the necessary CORS headers
        sendResponse(writer, "200 OK", "{}");  // Empty JSON body is enough for the pre-flight request
    }

    private void sendResponse(PrintWriter writer, String status, String body) {
        // Add CORS headers to the response
        writer.println("HTTP/1.1 " + status);
        writer.println("Content-Type: application/json");
        writer.println("Content-Length: " + body.length());
        writer.println("Access-Control-Allow-Origin: *");  // Allow requests from any origin (adjust as necessary)
        writer.println("Access-Control-Allow-Methods: GET, POST, OPTIONS"); // Allow GET, POST, and OPTIONS methods
        writer.println("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With"); // Allow specific headers
        writer.println("Access-Control-Allow-Credentials: true");  // Allow credentials (cookies, authorization headers, etc.)
        writer.println();
        writer.println(body);
    }


    public static void main(String[] args) {
        StudentServer server = new StudentServer();
        server.start(8080);
    }
}
