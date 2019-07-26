

package de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel;

import javafx.concurrent.Task;

public class ProgressFeedbackMessage {
    private final Task<?> task;
    private final String headerText;

    public ProgressFeedbackMessage(Task<?> task, String headerText) {
        this.task = task;
        this.headerText = headerText;
    }

    public Task<?> getTask() {
        return task;
    }

    public String getHeaderText() {
        return headerText;
    }
}
