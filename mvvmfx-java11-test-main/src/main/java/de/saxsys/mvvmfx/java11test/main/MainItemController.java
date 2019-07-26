

package de.saxsys.mvvmfx.java11test.main;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.controller.Controller;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.MessagesViewModel;

import java.util.concurrent.ExecutorService;

public class MainItemController implements Controller<MainViewModel> {

    private static Logger LOGGER = LoggerFactory.getLogger(MainItemController.class);

    private final ExecutorService executorService;
    private final MessagesViewModel messagesViewModel;

    private MainViewModel viewModel;

    public MainItemController(ExecutorService executorService, MessagesViewModel messagesViewModel) {
        this.executorService = executorService;
        this.messagesViewModel = messagesViewModel;
    }

    @Override
    public void initialize(MainViewModel viewModel) {
        this.viewModel = viewModel;

        refreshListOfUris();

    }

    public void refreshListOfUris() {


        Platform.runLater(() -> {
            viewModel.itemsProperty().clear();
                viewModel.itemsProperty().addAll("");

        });

    }


    public void add(String uri) {

        LOGGER.info("Adding uri: " + uri);

    }

    public void delete(String uri) {
        LOGGER.info("Deleting uri: " + uri);
    }

    public void readAndUpdateQuickPeekArea(String uri) {

    }


}
