package model.exceptions;

public class SelfDirException extends Exception {
    public SelfDirException() {
        super();
    }

    public SelfDirException(String msg) {
        super(msg);
    }
}
