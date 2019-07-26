

package de.saxsys.mvvmfx.java11test.common.javafx.controls.validation;

import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.ValidationStatus;
import javafx.application.Platform;
import javafx.scene.control.Control;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.ValidationDecoration;

import java.util.Optional;

public class ValidationVisualizer {

    protected final ValidationStatus validationStatus;
    protected final Control control;
    protected final ValidationDecoration decoration = new GraphicValidationDecoration();

    public ValidationVisualizer(ValidationStatus validationStatus, Control control) {
        this.validationStatus = validationStatus;
        this.control = control;
    }

    public void initialize() {
        validationStatus.validProperty().not().addListener((observable, oldValue, newValue) -> {
            runValidation();

        });

        // Initial check
        Platform.runLater(() -> runValidation() );

    }

    private void runValidation() {
        Optional<ValidationMessage> highestMessage = validationStatus.getHighestMessage();
        if(highestMessage.isPresent()) {
            if (!validationStatus.isValid()) {
                decoration.applyValidationDecoration(new org.controlsfx.validation.ValidationMessage() {
                    @Override
                    public String getText() {
                        return highestMessage.get().getMessage();
                    }

                    @Override
                    public Severity getSeverity() {
                        switch(highestMessage.get().getSeverity()) {
                            case ERROR:
                                return Severity.ERROR;
                            case WARNING:
                                return Severity.WARNING;
                        }
                        return null;
                    }

                    @Override
                    public Control getTarget() {
                        return control;
                    }
                });
            }
        }
        else {
            decoration.removeDecorations(control);
        }
    }

}
