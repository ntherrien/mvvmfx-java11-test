

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Supplier;

public class ConnectionDialogViewModel implements ViewModel {

    /*
    Properties
     */
    private final ListProperty<String> serverUris;
    public ListProperty<String> serverUrisProperty() { return serverUris; }

    private StringProperty selectedServerUri = new SimpleStringProperty();
    public StringProperty selectedServerUriProperty() {
        return selectedServerUri;
    }


    /*
    Commands
     */
    protected BooleanProperty connectCommandCondition = new SimpleBooleanProperty();
    private Supplier<Action> connectCommandActionSupplier;
    private Command connectCommand = new DelegateCommand(() -> connectCommandActionSupplier.get(), connectCommandCondition);
    public Command getConnectCommand() { return connectCommand; }
    public void setConnectCommandActionSupplier(Supplier<Action> supplier) { connectCommandActionSupplier = supplier; }

    private Supplier<Action> cancelCommandActionSupplier;
    private Command cancelCommand = new DelegateCommand(() -> cancelCommandActionSupplier.get());
    public Command getCancelCommand() { return cancelCommand; }
    public void setCancelCommandActionSupplier(Supplier<Action> supplier) { cancelCommandActionSupplier = supplier; }

    private Supplier<Action> addCommandActionSupplier;
    private Command addCommand = new DelegateCommand(() -> addCommandActionSupplier.get());
    public Command getAddCommand() { return addCommand; }
    public void setAddCommandActionSupplier(Supplier<Action> supplier) { addCommandActionSupplier = supplier; }

    private Supplier<Action> removeCommandActionSupplier;
    protected BooleanProperty removeCommandCondition = new SimpleBooleanProperty();
    private Command removeCommand = new DelegateCommand(() -> removeCommandActionSupplier.get(), removeCommandCondition);
    public Command getRemoveCommand() { return removeCommand; }
    public void setRemoveCommandActionSupplier(Supplier<Action> supplier) { removeCommandActionSupplier = supplier; }

    private Supplier<Action> editCommandActionSupplier;
    protected BooleanProperty editCommandCondition = new SimpleBooleanProperty();
    private Command editCommand = new DelegateCommand(() -> editCommandActionSupplier.get(), editCommandCondition);
    public Command getEditCommand() { return editCommand; }
    public void setEditCommandActionSupplier(Supplier<Action> supplier) { editCommandActionSupplier = supplier; }

    /*
    Notifications
    */
    private String notificationError = "notificationError";
    public String getErrorNotification() {return notificationError;}


    /*
    Implementation
     */
    public ConnectionDialogViewModel() {
        ObservableList<String> strings = FXCollections.observableArrayList();
        serverUris = new SimpleListProperty<>(strings);

        BooleanBinding selectedServerNotEmpty = Bindings.createBooleanBinding(() -> selectedServerUri.get() == null ? false : !selectedServerUri.get().isEmpty(), selectedServerUri);
        connectCommandCondition.bind(selectedServerNotEmpty);
        removeCommandCondition.bind(selectedServerNotEmpty);
        editCommandCondition.bind(selectedServerNotEmpty);

    }
}
