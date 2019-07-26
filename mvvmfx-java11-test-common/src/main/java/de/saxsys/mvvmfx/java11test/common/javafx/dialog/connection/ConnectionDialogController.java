

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection.model.serverlist.v2.ServerListModel;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ConnectionDialogController implements Controller<ConnectionDialogViewModel> {

    Logger LOGGER = LoggerFactory.getLogger(ConnectionDialogController.class);

    private final String configurationFileName;
    private ConnectionDialogViewModel viewModel;
    private ServerListModel serverListModel;

    public ConnectionDialogController(String configurationFileName) {

        this.configurationFileName = configurationFileName;
    }

    @Override
    public void initialize(ConnectionDialogViewModel viewModel) {
        this.viewModel = viewModel;

        serverListModel = loadConfigurationFile();
        viewModel.serverUrisProperty().addAll(serverListModel.getServerList());

        viewModel.serverUrisProperty().addListener((observable, oldValue, newValue) -> {
            serverListModel.getServerList().clear();
            serverListModel.getServerList().addAll(viewModel.serverUrisProperty().get());
            saveConfigurationFile();
        });

    }

    public void add(String uriString) {
        viewModel.serverUrisProperty().add(uriString);
    }

    public void remove(String uriString) {
        viewModel.serverUrisProperty().remove(uriString);
    }

    public void edit(String uriString, String newUriString) {
        int index = viewModel.serverUrisProperty().indexOf(uriString);
        viewModel.serverUrisProperty().set(index, newUriString);
    }

    private ServerListModel loadConfigurationFile() {
        LOGGER.info("Loading server list configuration file: " + configurationFileName);
        File workingDirectory = Paths.get("").toAbsolutePath().toFile();
        File file = new File(workingDirectory, configurationFileName);

        ObjectMapper objectMapper = new ObjectMapper();
        ServerListModel serverListModel = null;
        try {
            serverListModel = objectMapper.readValue(file, ServerListModel.class);
        } catch (IOException e) {
            LOGGER.error("Problem reading configuration file: " + configurationFileName, e);
        }

        return serverListModel;
    }

    private void saveConfigurationFile() {
        LOGGER.info("Saving server list configuration file: " + configurationFileName);
        File workingDirectory = Paths.get("").toAbsolutePath().toFile();
        File file = new File(workingDirectory, configurationFileName);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, serverListModel);
        } catch (IOException e) {
            String errorMessage = "Problem writing configuration file";
            LOGGER.error(errorMessage, e);
            viewModel.publish(viewModel.getErrorNotification(), errorMessage + " (" + e.getMessage() + ")" );
        }

    }
}
