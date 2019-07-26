

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.about;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutView implements FxmlView<AboutViewModel>, Initializable {

    @InjectViewModel //is provided by mvvmFX
    private AboutViewModel viewModel;

    @FXML
    private Label label_description;

    @FXML
    private TextField text_group_id;

    @FXML
    private TextField text_artifact_id;

    @FXML
    private TextField text_version;

    @FXML
    private Label label_copyright;

    @FXML
    private Button button_ok;

    @FXML
    void onClose(ActionEvent event) {
        viewModel.getCloseCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label_description.textProperty().bind(viewModel.applicationDescriptionProperty());
        text_group_id.textProperty().bind(viewModel.groupIdProperty());
        text_artifact_id.textProperty().bind(viewModel.artifactIdProperty());
        text_version.textProperty().bind(viewModel.versionProperty());
        label_copyright.textProperty().bind(viewModel.copyrightProperty());
    }
}
