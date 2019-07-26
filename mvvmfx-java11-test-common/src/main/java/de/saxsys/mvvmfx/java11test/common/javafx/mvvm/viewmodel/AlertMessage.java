

package de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel;

import javafx.scene.control.Alert;

public class AlertMessage {
    String title;
    String message;
    Alert.AlertType alertType;

    public AlertMessage(Alert.AlertType alertType, String title, String message) {
        this.title = title;
        this.message = message;
        this.alertType = alertType;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Alert.AlertType getAlertType() {
        return alertType;
    }
}
