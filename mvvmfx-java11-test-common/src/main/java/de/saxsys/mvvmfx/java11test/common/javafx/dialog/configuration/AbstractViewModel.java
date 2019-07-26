package de.saxsys.mvvmfx.java11test.common.javafx.dialog.configuration;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.value.ObservableBooleanValue;

public class AbstractViewModel implements ViewModel {

    /*
    Notifications
    */
    private String saveSuccessfulEvent = "saveSuccessfulEvent";
    public String getSaveSuccessfulEventNotification() {return saveSuccessfulEvent;}

}