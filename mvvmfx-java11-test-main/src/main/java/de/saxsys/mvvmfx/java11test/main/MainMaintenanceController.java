

package de.saxsys.mvvmfx.java11test.main;

import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.controller.Controller;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.viewmodel.MessagesViewModel;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class MainMaintenanceController implements Controller<MainViewModel> {

    private final ExecutorService executorService;
    private final MessagesViewModel messagesViewModel;
    private MainViewModel viewModel;

    public MainMaintenanceController(ExecutorService executorService, MessagesViewModel messagesViewModel) {
        this.executorService = executorService;
        this.messagesViewModel = messagesViewModel;
    }

    @Override
    public void initialize(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }


    public void restoreBackup(File selectedDirectory) {

    }
}
