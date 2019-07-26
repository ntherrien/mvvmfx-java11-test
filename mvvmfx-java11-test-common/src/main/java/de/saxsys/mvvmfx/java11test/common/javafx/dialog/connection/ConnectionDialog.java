

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection;

import de.saxsys.mvvmfx.java11test.common.javafx.DialogHelper;
import de.saxsys.mvvmfx.java11test.common.javafx.controls.validation.ValidationVisualizer;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.AlertMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.validator.ServerUriValidator;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection.ConnectionDialog.Result.CANCEL;
import static de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection.ConnectionDialog.Result.CONNECT;

public class ConnectionDialog extends Stage {

    Logger LOGGER = LoggerFactory.getLogger(ConnectionDialog.class);

    private Result result = CANCEL;

    public enum Result {
        CONNECT,
        CANCEL
    }

    private final ConnectionDialogViewModel viewModel;
    private final ConnectionDialogController controller;

    public ConnectionDialog(String configurationFileName) {

        ViewTuple<ConnectionDialogView, ConnectionDialogViewModel> viewTuple = FluentViewLoader.fxmlView(ConnectionDialogView.class).load();
        viewModel = viewTuple.getViewModel();
        Parent root = viewTuple.getView();

        setScene(new Scene(root));
        setTitle("Connection Dialog");

        controller = new ConnectionDialogController(configurationFileName);
        controller.initialize(viewModel);


        viewModel.setConnectCommandActionSupplier(() -> new Action() {
            @Override
            protected void action() throws Exception {
                result = CONNECT;
                close();
            }
        });

        viewModel.setCancelCommandActionSupplier(() -> new Action() {
            @Override
            protected void action() throws Exception {
                result = CANCEL;
                close();
            }
        });

        viewModel.subscribe(viewModel.getErrorNotification(), (s, objects) -> {
            DialogHelper.showAlertLater(this, new AlertMessage(Alert.AlertType.ERROR, "Error", (String)objects[0]));
        });

        viewModel.setAddCommandActionSupplier(() -> new Action() {
            @Override
            protected void action() throws Exception {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Server URI Dialog");
                dialog.setHeaderText("Enter the server URI you wish to add");
                dialog.setContentText("Server URI:");
                dialog.getEditor().setMinWidth(300);

                // Setup Validation
                ServerUriValidator serverUriValidator = new ServerUriValidator(dialog.getEditor().textProperty());
                final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                btOk.disableProperty().bind(serverUriValidator.getValidationStatus().validProperty().not());

                ValidationVisualizer visualizer = new ValidationVisualizer(serverUriValidator.getValidationStatus(), dialog.getEditor());
                visualizer.initialize();

                // Go
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(uriString -> controller.add(uriString));
            }
        });

        viewModel.setRemoveCommandActionSupplier(() -> new Action() {
            @Override
            protected void action() throws Exception {
                controller.remove(viewModel.selectedServerUriProperty().get());
            }
        });

        viewModel.setEditCommandActionSupplier(() -> new Action() {
            @Override
            protected void action() throws Exception {
                TextInputDialog dialog = new TextInputDialog(viewModel.selectedServerUriProperty().get());
                dialog.setTitle("Edit Server URI Dialog");
                dialog.setHeaderText("Edit the server URI");
                dialog.setContentText("Server URI:");
                dialog.getEditor().setMinWidth(300);

                // Setup Validation
                ServerUriValidator serverUriValidator = new ServerUriValidator(dialog.getEditor().textProperty());
                final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                btOk.disableProperty().bind(serverUriValidator.getValidationStatus().validProperty().not());

                ValidationVisualizer visualizer = new ValidationVisualizer(serverUriValidator.getValidationStatus(), dialog.getEditor());
                visualizer.initialize();

                // Go
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(uriString -> controller.edit(viewModel.selectedServerUriProperty().get(), uriString));
            }
        });


    }

    public Result getResult() {
        return result;
    }

    public Optional<URI> getUri() {
        if (result == CANCEL) return Optional.empty();
        try {
            return Optional.of(new URI(viewModel.selectedServerUriProperty().get()));
        } catch (URISyntaxException e) {
            LOGGER.error("Problem getting selected server uri.", e);
            return Optional.empty();
        }
    }


}
