

package de.saxsys.mvvmfx.java11test.common.javafx.application;

import de.saxsys.mvvmfx.java11test.common.javafx.DialogHelper;
import de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection.ConnectionDialog;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.AlertMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ExceptionMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.MessagesViewModel;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.ProgressFeedbackMessage;
import de.saxsys.mvvmfx.java11test.common.javafx.task.rest.RestApiGetAppInformationTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.saxsys.mvvmfx.java11test.common.javafx.task.ws.WebsocketConnectTask;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class GenericJavaFxApplication extends Application {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GenericJavaFxApplication.class);
    protected final String defaultConfigurationFile;

    protected MessagesViewModel messagesViewModel = new MessagesViewModel();

    protected final ExecutorService executorService;

    protected Stage primaryStage;

    protected boolean applicationStopping = false;
    protected boolean applicationRestarting = false;

    protected URI uri;

    public GenericJavaFxApplication(Class<? extends GenericJavaFxApplication> applicationClass, String defaultConfigurationFile) {
        this.defaultConfigurationFile = defaultConfigurationFile;
        Platform.setImplicitExit(false);

        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, applicationClass.getSimpleName() + "-TaskThread");
            }
        });

        // Shutdown hooks are called even in cases where System.exit() or OS interrupts are used.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutdown hook triggerred.");

            // could be shut down already if application is terminating gracefully. (see stop() method)
            // This hook is useful when the application is being killed and stop() is bypassed.
            // So shutdown() is called only if the hook was triggerred unexpectedly.
            if(!applicationStopping)
                shutdown();

        }));
    }

    private void shutdown() {
        LOGGER.info("Shutting down services...");
        if (!executorService.isShutdown())
            executorService.shutdown();
    }

    private void restart() {
        LOGGER.info("Application restarting...");
        applicationRestarting = true;

        showConnectionDialog();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        LOGGER.info("Application starting...");
        this.primaryStage = primaryStage;

        // Initialize user feedback dialogs and animations
        messagesViewModel.exceptionMessageProperty().addListener((observable, oldValue, newValue) -> DialogHelper.showExceptionDialogLater(primaryStage, newValue));
        messagesViewModel.alertMessageProperty().addListener((observable, oldValue, newValue) -> DialogHelper.showAlertLater(primaryStage, newValue) );
        messagesViewModel.progressProperty().addListener((observable, oldValue, newValue) -> DialogHelper.showProgressDialogLater(primaryStage, newValue) );

        showConnectionDialog();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LOGGER.info("Application stopping...");
        applicationStopping = true;

        // In the case the application is stopping, shutdown hook will also be called.
        shutdown();
    }

    public boolean isApplicationStopping() {
        return applicationStopping;
    }

    public boolean isApplicationRestarting() {
        return applicationRestarting;
    }

    protected void showConnectionDialog() {
        LOGGER.info("Displaying Connection Dialog");
        ConnectionDialog dialog = new ConnectionDialog(defaultConfigurationFile);
        dialog.showAndWait();
        if (dialog.getResult().equals(ConnectionDialog.Result.CONNECT) && dialog.getUri().isPresent()) {

            uri = dialog.getUri().get();
            LOGGER.info("User selected to connect to uri: " + uri);

            onConnectToRestApi(uri);

        } else {
            // exit
            LOGGER.info("User selected to exit application.");
            Platform.exit();
        }
    }

    /**
     * First stage of the connection process.   The UI has a URI to connect to and will initiate a REST API connection
     * to the app.
     */
    protected void onConnectToRestApi(URI uri) {

        LOGGER.info("Requesting REST API AppInformation from base uri: " + uri);

        // Request database connection information...
        RestApiGetAppInformationTask restApiCall = new RestApiGetAppInformationTask(uri);
        restApiCall.setOnFailed(event -> Platform.runLater(() -> {

            // Check if error is related to parsing of JSON
            if (restApiCall.getException() != null && restApiCall.getException().getCause() != null && restApiCall.getException().getCause().getClass().getName().startsWith("com.fasterxml")) {
                final String errorMessage = "REST API mismatch";
                final Throwable exception = restApiCall.getException();
                LOGGER.error(errorMessage, exception);
                showAlertAndReturnToConnectionDialog(new AlertMessage(Alert.AlertType.ERROR, errorMessage, "Connection was successful, but the remote REST API schema does not match this application.\n"));
            } else {
                // Needs to block.
                final String errorMessage = "Connection failed.";
                final Throwable exception = restApiCall.getException();
                LOGGER.error(errorMessage, exception);
                DialogHelper.showExceptionDialog(primaryStage, new ExceptionMessage(errorMessage, exception));
                showConnectionDialog(); // Only when exception dialog has been acknowledged will we show the new connection dialog.
            }
        }));
        restApiCall.setOnSucceeded(event -> Platform.runLater(() -> {
            try {
                String appInformation = restApiCall.get();
                onRestApiConnectionSuccessful(appInformation);
            } catch (InterruptedException | ExecutionException e) {
                final String errorMessage = "Problem retrieving app information.";
                LOGGER.error(errorMessage, e);
                messagesViewModel.exceptionMessageProperty().set(new ExceptionMessage(errorMessage, e));
            }
        }));


        executorService.submit(restApiCall);
        // Must keep a window open or else the application will shut down.
        //DialogHelper.showProgressDialog(primaryStage, new ProgressFeedbackMessage(restApiCall, "Connecting to " + uri.toString() + "..."));

    }


    protected void onRestApiConnectionSuccessful(String appInformation) {

        LOGGER.info("REST API Call was successful. Validating AppInformation:\n" + appInformation.toString() + "\n");


        LOGGER.info("Validation successful. This UI is compatible with app.");

            WebsocketConnectTask connectTask = new WebsocketConnectTask();
            connectTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            final String errorMessage = "Websocket connection failed for Real Time Logs.";
                            LOGGER.error(errorMessage, connectTask.getException());
                            messagesViewModel.exceptionMessageProperty().set(new ExceptionMessage(errorMessage, connectTask.getException()));

                        }
                    });
                }
            });
            connectTask.setOnSucceeded(event -> onWebsocketApiConnectionSuccessful());

            executorService.submit(connectTask);

            messagesViewModel.progressProperty().set(new ProgressFeedbackMessage(connectTask, "Connecting to " + "" + "..."));



    }


    protected void onWebsocketApiConnectionSuccessful() {

        LOGGER.info("Websocket connection was successful. Initializing database connection...");


        LOGGER.info("Database connection was successful. Initializing persistence...");

        LOGGER.info("Persistence initialized succesfully. Initializing main window...");
        onInitializeMainWindow(primaryStage);

        // We want the application to exit only when the user requests it, not just when the app may hide the window.
        // There are use cases where the main window is hidden and the connection dialog shown.
        primaryStage.setOnCloseRequest(event -> applicationStopping = true);
        primaryStage.setOnHidden(event -> {
            if(applicationStopping)
                Platform.exit();
            else {
                // Window hidden due to error or some other reason, but not due to user exiting.
                restart();
            }
        });

        LOGGER.info("Window ready. Displaying...");
        primaryStage.show();
    }


    protected abstract void onInitializeMainWindow(Stage primaryStage);


    protected void showAlertAndReturnToConnectionDialog(AlertMessage alertMessage) {
        LOGGER.warn(alertMessage.getTitle() + ": " + alertMessage.getMessage());
        Platform.runLater(() -> {
            DialogHelper.showAlert(primaryStage, alertMessage);
            showConnectionDialog();
        });
    }





}
