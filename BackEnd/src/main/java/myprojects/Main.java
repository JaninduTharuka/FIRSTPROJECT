package myprojects;

import com.sun.net.httpserver.HttpServer;
import myprojects.application.controller.StudentController;
import myprojects.domain.service.StudentService;
import myprojects.external.repository.StudentRepository;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            StudentRepository studentRepository = new StudentRepository();
            StudentService studentService = new StudentService(studentRepository);
            StudentController studentController = new StudentController(studentService);

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/students", studentController);
            server.setExecutor(null);
            server.start();

            System.out.println("Server started on port 8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}