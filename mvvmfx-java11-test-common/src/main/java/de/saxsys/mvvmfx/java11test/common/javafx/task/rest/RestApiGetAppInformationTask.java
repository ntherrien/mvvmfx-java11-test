package de.saxsys.mvvmfx.java11test.common.javafx.task.rest;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class RestApiGetAppInformationTask extends Task<String> {

    private static Logger LOGGER = LoggerFactory.getLogger(RestApiGetAppInformationTask.class);

    public RestApiGetAppInformationTask(URI uri) {

    }

    @Override
    protected String call() throws Exception {


        LOGGER.info("REST API Call: AppInformation (" + "" + ")");

        return "success";

    }
}
