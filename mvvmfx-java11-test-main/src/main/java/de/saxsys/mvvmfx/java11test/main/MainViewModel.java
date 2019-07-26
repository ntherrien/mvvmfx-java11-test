

package de.saxsys.mvvmfx.java11test.main;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import de.saxsys.mvvmfx.java11test.common.javafx.dialog.configuration.AbstractViewModel;

public class MainViewModel extends AbstractViewModel {

    /*
    Properties
    */
    private StringProperty applicationStatus = new SimpleStringProperty();
    public StringProperty applicationStatusProperty() {
        return applicationStatus;
    }

    private final ListProperty<String> items;
    public ListProperty<String> itemsProperty() { return items; }

    private StringProperty selectedItem = new SimpleStringProperty();
    public StringProperty selectedItemProperty() {
        return selectedItem;
    }

    private StringProperty customDocumentEnabled = new SimpleStringProperty();
    public StringProperty customDocumentEnabledProperty() {return customDocumentEnabled;}

    private ObjectProperty<Task<?>> busyReading = new SimpleObjectProperty<>();
    public ObjectProperty<Task<?>> busyReadingProperty() { return busyReading; }

    private ObjectProperty<Task<?>> busyWriting = new SimpleObjectProperty<>();
    public ObjectProperty<Task<?>> busyWritingProperty() { return busyWriting; }



    /*
    Commands
    */
    private Command createUriCommand;
    public Command getCreateUriCommand() {
        return createUriCommand;
    }
    public void setCreateUriCommand(Command createUriCommand) {
        this.createUriCommand = createUriCommand;
    }

    private Command deleteUriCommand;
    public Command getDeleteUriCommand() {
        return deleteUriCommand;
    }
    public void setDeleteUriCommand(Command deleteUriCommand) {
        this.deleteUriCommand = deleteUriCommand;
    }

    private Command editUriCommand;
    public Command getEditUriCommand() {
        return editUriCommand;
    }
    public void setEditUriCommand(Command editUriCommand) {
        this.editUriCommand = editUriCommand;
    }

    private Command restoreConfigurationCommand;
    public Command getRestoreConfigurationCommand() {
        return restoreConfigurationCommand;
    }
    public void setRestoreConfigurationCommand(Command restoreConfigurationCommand) {
        this.restoreConfigurationCommand = restoreConfigurationCommand;
    }

    private Command backupConfigurationCommand;
    public Command getBackupConfigurationCommand() {
        return backupConfigurationCommand;
    }
    public void setBackupConfigurationCommand(Command backupConfigurationCommand) {
        this.backupConfigurationCommand = backupConfigurationCommand;
    }

    private Command displayAppInformationCommand;
    public Command getDisplayAppInformationCommand() {
        return displayAppInformationCommand;
    }
    public void setDisplayAppInformationCommand(Command displayAppInformationCommand) {
        this.displayAppInformationCommand = displayAppInformationCommand;
    }

    private Command displayAboutBoxCommand;
    public Command getDisplayAboutBoxCommand() {
        return displayAboutBoxCommand;
    }
    public void setDisplayAboutBoxCommand(Command displayAboutBoxCommand) {
        this.displayAboutBoxCommand = displayAboutBoxCommand;
    }

    private Command exitCommand;
    public Command getExitCommand() {
        return exitCommand;
    }
    public void setExitCommand(Command exitCommand) {
        this.exitCommand = exitCommand;
    }

    /*
    Notifications
    */
    private String notificationLogEvent = "notificationLogEvent";
    public String getLogEventNotification() {return notificationLogEvent;}

    private String notificationError = "notificationError";
    public String getErrorNotification() {return notificationError;}



    /*
    Implementation
    */
    public MainViewModel() {
        super();

        ObservableList<String> strings = FXCollections.observableArrayList();
        items = new SimpleListProperty<>(strings);

    }


}
