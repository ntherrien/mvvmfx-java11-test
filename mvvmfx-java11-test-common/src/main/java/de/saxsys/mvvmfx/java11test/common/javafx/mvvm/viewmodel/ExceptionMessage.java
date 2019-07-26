

package de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel;

public class ExceptionMessage {

    private String title = null;
    private final String message;
    private final Throwable exception;

    public ExceptionMessage(String message, Throwable exception) {
        this.message = message;
        this.exception = exception;
    }

    public ExceptionMessage(String title, String message, Throwable exception) {
        this.title = title;
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }

    public String getTitle() {
        return title;
    }
}
