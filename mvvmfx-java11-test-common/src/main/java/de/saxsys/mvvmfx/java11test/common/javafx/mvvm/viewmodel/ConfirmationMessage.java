

package de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel;

public class ConfirmationMessage {
    String title;
    String message;
    private final String question;

    public ConfirmationMessage(String title, String message, String question) {
        this.title = title;
        this.message = message;
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getQuestion() {
        return question;
    }
}
