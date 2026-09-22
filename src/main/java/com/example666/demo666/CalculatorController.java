package com.example666.demo666;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @GetMapping("/")
    public String home() {
        return "Calculator application is running successfully";
    }

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(
            @RequestParam double first,
            @RequestParam double second,
            @RequestParam String operation) {

        Map<String, Object> response = new LinkedHashMap<>();
        double result;

        switch (operation.toLowerCase()) {
            case "add":
                result = first + second;
                break;

            case "subtract":
                result = first - second;
                break;

            case "multiply":
                result = first * second;
                break;

            case "divide":
                if (second == 0) {
                    response.put("error", "Division by zero is not allowed");
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(response);
                }

                result = first / second;
                break;

            default:
                response.put("error", "Invalid operation");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
        }

        response.put("firstNumber", first);
        response.put("secondNumber", second);
        response.put("operation", operation);
        response.put("result", result);

        return ResponseEntity.ok(response);
    }
}