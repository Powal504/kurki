package org.example.kurki.security.exception;

public class ChatbotUnavailableException extends RuntimeException {
    public ChatbotUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatbotUnavailableException(String message) {
        super(message);
    }
}