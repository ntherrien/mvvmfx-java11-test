

package de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MessagesViewModel {

    private ObjectProperty<ExceptionMessage> exceptionMessage = new SimpleObjectProperty<>();
    public ObjectProperty<ExceptionMessage> exceptionMessageProperty() { return exceptionMessage; }

    private ObjectProperty<AlertMessage> alertMessage = new SimpleObjectProperty<>();
    public ObjectProperty<AlertMessage> alertMessageProperty() { return alertMessage; }

    private ObjectProperty<ProgressFeedbackMessage> progress = new SimpleObjectProperty<>();
    public ObjectProperty<ProgressFeedbackMessage> progressProperty() {
        return progress;
    }

}
