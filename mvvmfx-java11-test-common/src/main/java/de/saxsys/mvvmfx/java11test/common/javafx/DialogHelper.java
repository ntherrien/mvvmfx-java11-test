

package de.saxsys.mvvmfx.java11test.common.javafx;

import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.AlertMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ConfirmationMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ExceptionMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ProgressFeedbackMessage;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;

import java.util.Optional;

public class DialogHelper {

    public static void showAlert(Window owner, AlertMessage alertMessage) {
        Alert alert = new Alert(alertMessage.getAlertType());
        alert.setTitle(alertMessage.getTitle());
        alert.setHeaderText(null);
        alert.setContentText(alertMessage.getMessage());
        alert.initModality(Modality.APPLICATION_MODAL);
        if(owner.getScene() != null) alert.initOwner(owner);
        alert.showAndWait();
    }

    public static void showAlertLater(Window owner, AlertMessage alertMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showAlert(owner, alertMessage);
            }
        });
    }

    public static void showAlertLater(Window owner, final String title, final String message) {
        showAlertLater(owner, new AlertMessage(Alert.AlertType.ERROR, title, message));
    }

    private static ProgressDialog progressDialog;
    public static void showProgressDialogLater(Window owner, ProgressFeedbackMessage progressFeedbackMessage) {
        Platform.runLater(() -> {
            // The progress animation will only start if the task lasts for more than 100 millis.
            PauseTransition delay = new PauseTransition(Duration.millis(100));
            delay.setOnFinished( event -> Platform.runLater(() -> {
                // This view model deals with multiple tasks in parallel by showing
                // only one progress window for one of the tasks.
                // When that task is completed, other tasks still running will cause
                // the progress dialog to re-open.
                final Task<?> task = progressFeedbackMessage.getTask();
                if(progressDialog == null && (!task.isDone() && !task.isCancelled())) {
                    showProgressDialog(owner, progressFeedbackMessage);
                    progressDialog = null;
                }
                else {
                    // Progress animation is already busy with other task, requeue.
                    if(!task.isDone() && !task.isCancelled())
                        showProgressDialogLater(owner, progressFeedbackMessage);
                }
            } ));
            delay.play();

        });

    }

    public static void showProgressDialog(Window owner, ProgressFeedbackMessage progressFeedbackMessage) {
        ProgressDialog progressDialog = new ProgressDialog(progressFeedbackMessage.getTask());
        progressDialog.setHeaderText(progressFeedbackMessage.getHeaderText());
        progressDialog.initModality(Modality.APPLICATION_MODAL);
        if(owner.getScene() != null) progressDialog.initOwner(owner);
        progressDialog.showAndWait();
    }


    public static void showExceptionDialogAndLog(Logger logger, Window owner, String errorMessage, Throwable exception) {
        logger.error(errorMessage, exception);
        showExceptionDialogLater(owner, new ExceptionMessage(errorMessage, exception));
    }

    public static void showExceptionDialogLater(Window owner, ExceptionMessage exceptionMessage) {
        Platform.runLater(() -> {
            showExceptionDialog(owner, exceptionMessage);
        });
    }

    public static void showExceptionDialog(Window owner, ExceptionMessage exceptionMessage) {
        ExceptionDialog exceptionDialog = new ExceptionDialog(exceptionMessage.getException());
        if(exceptionMessage.getTitle() != null) exceptionDialog.setTitle(exceptionMessage.getTitle());
        exceptionDialog.getDialogPane().setExpanded(true);
        exceptionDialog.setHeaderText(exceptionMessage.getMessage());
        exceptionDialog.initModality(Modality.APPLICATION_MODAL);
        if(owner.getScene() != null) exceptionDialog.initOwner(owner);
        exceptionDialog.showAndWait();
    }


    public static Optional<ButtonType> showConfirmationDialog(Window owner, ConfirmationMessage confirmationMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(confirmationMessage.getTitle());
        alert.setHeaderText(confirmationMessage.getMessage());
        alert.setContentText(confirmationMessage.getQuestion());
        alert.initModality(Modality.APPLICATION_MODAL);
        if(owner.getScene() != null) alert.initOwner(owner);

        Optional<ButtonType> result = alert.showAndWait();
        return result;

    }

}
