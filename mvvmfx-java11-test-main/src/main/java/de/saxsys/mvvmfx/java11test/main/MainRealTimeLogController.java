

package de.saxsys.mvvmfx.java11test.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.saxsys.mvvmfx.java11test.common.javafx.mvvm.controller.Controller;

public class MainRealTimeLogController implements Controller<MainViewModel> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainRealTimeLogController.class);

    private MainViewModel viewModel;


    @Override
    public void initialize(MainViewModel viewModel) {

        this.viewModel = viewModel;


    }



}
