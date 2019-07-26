

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionDialogView implements FxmlView<ConnectionDialogViewModel>, Initializable {

    @InjectViewModel
    ConnectionDialogViewModel viewModel;

    @FXML
    private ListView<String> listView_urls;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_add;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_connect;

    @FXML
    private Button button_cancel;

    @FXML
    void onAdd(ActionEvent event) {
        viewModel.getAddCommand().execute();
    }

    @FXML
    void onCancel(ActionEvent event) {
        viewModel.getCancelCommand().execute();
    }

    @FXML
    void onConnect(ActionEvent event) {
        viewModel.getConnectCommand().execute();
    }

    @FXML
    void onEdit(ActionEvent event) {
        viewModel.getEditCommand().execute();
    }

    @FXML
    void onRemove(ActionEvent event) {
        viewModel.getRemoveCommand().execute();
    }

    @FXML
    void onUrlClicked(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            viewModel.getConnectCommand().execute();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView_urls.itemsProperty().bindBidirectional(viewModel.serverUrisProperty());
        viewModel.selectedServerUriProperty().bind(listView_urls.getSelectionModel().selectedItemProperty());
        viewModel.selectedServerUriProperty().addListener((observable, oldValue, newValue) -> listView_urls.getSelectionModel().select(newValue));

        button_connect.disableProperty().bind(viewModel.getConnectCommand().executableProperty().not());
        button_edit.disableProperty().bind(viewModel.getEditCommand().executableProperty().not());
        button_delete.disableProperty().bind(viewModel.getRemoveCommand().executableProperty().not());

    }
}
