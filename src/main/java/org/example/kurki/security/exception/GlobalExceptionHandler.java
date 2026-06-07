package org.example.kurki.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 404,
                        "error", "User not found",
                        "message", ex.getMessage()
                )
        );
    }
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("message", ex.getMessage());
        body.put("error", "Forbidden");

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad request",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(PredictionException.class)
    public ResponseEntity<Map<String, Object>> handlePredictionException(PredictionException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Prediction failed", "message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Server error", "message", ex.getMessage()));
    }

    @ExceptionHandler(DatabaseUnavailableException.class)
    public ResponseEntity<?> handleDatabaseUnavailable(DatabaseUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "error", "Database unavailable",
                        "message", "Usługa jest chwilowo niedostępna. Problem z połączeniem z bazą danych. Spróbuj ponownie za chwilę.",
                        "code", "DATABASE_DISCONNECTED"
                )
        );
    }

    @ExceptionHandler(ChatbotUnavailableException.class)
    public ResponseEntity<?> handleChatbotUnavailable(ChatbotUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "error", "Chatbot unavailable",
                        "message", "Chatbot jest chwilowo niedostępny. Nie martw się — Twoje konto i dane są bezpieczne. Spróbuj ponownie za chwilę.",
                        "code", "CHATBOT_UNAVAILABLE"
                )
        );
    }
}