

package de.saxsys.mvvmfx.java11test.main;

import de.saxsys.mvvmfx.java11test.common.javafx.application.GenericJavaFxApplication;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.application.Application.launch;

public class MainApplication extends GenericJavaFxApplication {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static final String CONFIG_DEFAULT_SERVERLIST_JSON = "config/default/serverlist.json";

    public MainApplication() {
        super(MainApplication.class, CONFIG_DEFAULT_SERVERLIST_JSON);
    }

    @Override
    protected void onInitializeMainWindow(Stage primaryStage) {
        new MainWindow(primaryStage, executorService, messagesViewModel);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
