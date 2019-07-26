

package de.saxsys.mvvmfx.java11test.main;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.StatusBar;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainView implements FxmlView<MainViewModel>, Initializable {

    @InjectViewModel //is provided by mvvmFX
    private MainViewModel viewModel;

    @FXML
    private MenuItem menuItem_ImportFromDirectory;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ListView<String> listView_items;

    @FXML
    private Button button_create_item;

    @FXML
    private Button button_delete_item;

    @FXML
    private Button button_edit_item;

    @FXML
    private Label label_custom_enabled;

    @FXML
    private StatusBar statusbar;

    @FXML
    private TextArea textArea_logs;

    @FXML
    void createUri(ActionEvent event) { viewModel.getCreateUriCommand().execute(); }

    @FXML
    void deleteUri(ActionEvent event) {
        viewModel.getDeleteUriCommand().execute();
    }

    @FXML
    void editUri(ActionEvent event) { viewModel.getEditUriCommand().execute(); }

    @FXML
    void onRestoreConfiguration(ActionEvent event) { viewModel.getRestoreConfigurationCommand().execute(); }

    @FXML
    void onBackupConfiguration(ActionEvent actionEvent) {
        viewModel.getBackupConfigurationCommand().execute();
    }

    @FXML
    void onItemClicked(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                //&& (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0)) {
            viewModel.getEditUriCommand().execute();
        }
    }


    @FXML
    void onDisplayAppInformation(ActionEvent event) { viewModel.getDisplayAppInformationCommand().execute(); }

    @FXML
    void onAbout(ActionEvent actionEvent) { viewModel.getDisplayAboutBoxCommand().execute(); }

    @FXML
    void onExit() { viewModel.getExitCommand().execute(); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView_items.itemsProperty().bindBidirectional(viewModel.itemsProperty());
        viewModel.selectedItemProperty().bind(listView_items.getSelectionModel().selectedItemProperty());
        viewModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> listView_items.getSelectionModel().select(newValue));

        viewModel.subscribe(viewModel.getLogEventNotification(), (s, objects) -> Arrays.stream(objects).map(String.class::cast).forEach(logEntry -> {
            textArea_logs.appendText(logEntry + "\n");
        }));

        statusbar.textProperty().bind(viewModel.applicationStatusProperty());

        label_custom_enabled.textProperty().bind(viewModel.customDocumentEnabledProperty());
    }
}
