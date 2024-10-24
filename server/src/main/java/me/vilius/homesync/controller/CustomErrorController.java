package me.vilius.homesync.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = "Unknown error";

        if (statusCode != null) {
            if (HttpStatus.NOT_FOUND.value() == statusCode) {
                message = "Invalid endpoint. The requested resource could not be found.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                message = "An unexpected server error occurred.";
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                message = "Invalid HTTP method or body.";
            }
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();  // Default status code
            message = "Unknown error occurred.";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", statusCode);
        response.put("error", message);
        return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }

    public String getErrorPath() {
        return "/error";
    }
}