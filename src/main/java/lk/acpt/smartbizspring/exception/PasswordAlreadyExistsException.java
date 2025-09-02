package lk.acpt.smartbizspring.exception;

public class PasswordAlreadyExistsException extends RuntimeException {
    public PasswordAlreadyExistsException(String message) {
        super(message);
    }
}