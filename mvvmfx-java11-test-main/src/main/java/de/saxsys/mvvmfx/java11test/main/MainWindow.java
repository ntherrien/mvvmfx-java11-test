

package de.saxsys.mvvmfx.java11test.main;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.saxsys.mvvmfx.java11test.common.javafx.DialogHelper;
import de.saxsys.mvvmfx.java11test.common.javafx.dialog.about.AboutWindow;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.AlertMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ConfirmationMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.MessagesViewModel;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ProgressFeedbackMessage;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class MainWindow {

    Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);

    public static final String APP_TITLE = "App";
    public static final String APP_DESCRIPTION = "App description";
    private final Stage stage;
    private final MessagesViewModel messagesViewModel;
    private MainViewModel viewModel;

    private final ExecutorService executorService;

    private final MainItemController urisController;
    private final MainMaintenanceController maintenanceController;
    private final MainRealTimeLogController realTimeLogsController;

    public MainWindow(Stage stage, ExecutorService executorService, MessagesViewModel messagesViewModel) {
        this.stage = stage;
        this.executorService = executorService;
        this.messagesViewModel = messagesViewModel;

        ViewTuple<MainView, MainViewModel> viewTuple = FluentViewLoader.fxmlView(MainView.class).load();
        viewModel = viewTuple.getViewModel();
        Parent root = viewTuple.getView();

        urisController = new MainItemController(executorService, messagesViewModel);
        urisController.initialize(viewModel);

        maintenanceController = new MainMaintenanceController(executorService, messagesViewModel);
        maintenanceController.initialize(viewModel);

        realTimeLogsController = new MainRealTimeLogController();
        realTimeLogsController.initialize(viewModel);

        stage.setScene(new Scene(root));
        stage.setTitle(APP_TITLE);

        // User feedback and Animations
        viewModel.busyReadingProperty().addListener(observable -> {
            DialogHelper.showProgressDialogLater(stage, new ProgressFeedbackMessage(viewModel.busyReadingProperty().get(), "Reading data..."));
        });

        viewModel.busyWritingProperty().addListener(observable -> {
            DialogHelper.showProgressDialogLater(stage, new ProgressFeedbackMessage(viewModel.busyWritingProperty().get(), "Writing data..."));
        });

        // Common user interface options
        String uriStr = "";
        viewModel.publish(viewModel.getLogEventNotification(), uriStr + " is connected.");
        viewModel.applicationStatusProperty().set("Connected to: " + uriStr);

        viewModel.setExitCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                stage.fireEvent(
                        new WindowEvent(
                                stage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
            }
        }));

        viewModel.setRestoreConfigurationCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                onRestoreConfiguration();
            }
        }));
        viewModel.setBackupConfigurationCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                onBackupConfiguration();
            }
        }));
        viewModel.setDisplayAboutBoxCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                onAbout();
            }
        }));
        viewModel.setDisplayAppInformationCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                onDisplayAppInformation();
            }
        }));

        viewModel.subscribe(viewModel.getErrorNotification(), (s, objects) -> {
            DialogHelper.showAlertLater(stage, new AlertMessage(Alert.AlertType.ERROR, "Error", (String)objects[0]));
        });


        viewModel.setCreateUriCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                createUri();
            }
        }));
        viewModel.setEditUriCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                editUri();
            }
        }));
        viewModel.setDeleteUriCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                deleteUri();
            }
        }));

        viewModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> urisController.readAndUpdateQuickPeekArea(newValue));

    }

    void createUri() {
        LOGGER.info("User selected to Create Uri");

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Uri Dialog");
        dialog.setHeaderText("Enter the uri you wish to create.");
        dialog.setContentText("Uri:");
        dialog.getEditor().setMinWidth(300);

        // Go
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(uri -> {
            urisController.add(uri);
            urisController.refreshListOfUris();
        });

    }

    void deleteUri() {
        // Read current selection
        String selectedUri = viewModel.selectedItemProperty().get();
        LOGGER.info("User selected to Delete URI: " + selectedUri);

        if(selectedUri == null || selectedUri.isEmpty()) {
            String errorMessage = "No uri has been selected. Please select the uri you wish to delete.";
            LOGGER.error("User error: " + errorMessage);
            viewModel.publish(viewModel.getErrorNotification(), errorMessage);
        }

        final Optional<ButtonType> result = DialogHelper.showConfirmationDialog(stage, new ConfirmationMessage("Confirmation Dialog", "Deleting URI: " + selectedUri, "Are you ok with this?"));
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            // Deleting...
            urisController.delete(selectedUri);
            urisController.refreshListOfUris();
        } else {
            // ... user chose CANCEL or closed the dialog
        }


    }

    void editUri() {
        // Read current selection
        String selectedUri = viewModel.selectedItemProperty().get();

        if(selectedUri == null || selectedUri.isEmpty()) {
            String errorMessage = "No uri has been selected. Please select the uri you wish to edit.";
            LOGGER.error("User error: " + errorMessage);
            viewModel.publish(viewModel.getErrorNotification(), errorMessage);
        }


    }

    void onRestoreConfiguration() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());
        File selectedDirectory = directoryChooser.showDialog(stage);

        maintenanceController.restoreBackup(selectedDirectory);

        urisController.refreshListOfUris();

    }

    void onBackupConfiguration() {
        System.out.println("backup...");
    }

    void onDisplayAppInformation() {
        // Open new window...
    }

    void onAbout() {
        // Open new window...
        AboutWindow aboutWindow = new AboutWindow(stage, APP_TITLE, APP_DESCRIPTION);
        aboutWindow.showAndWait();
    }

}
