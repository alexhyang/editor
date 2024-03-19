package model.exceptions;

public class IllegalNameException extends NameException {
    public IllegalNameException() {
        super();
    }

    public IllegalNameException(String msg) {
        super(msg);
    }
}
