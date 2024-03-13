package model.exceptions;

public class EmptyNameException extends IllegalNameException {
    public EmptyNameException() {
        super();
    }

    public EmptyNameException(String msg) {
        super(msg);
    }
}
