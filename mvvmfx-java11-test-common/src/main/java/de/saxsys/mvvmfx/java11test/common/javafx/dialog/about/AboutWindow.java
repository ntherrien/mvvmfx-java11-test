

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.about;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.ZonedDateTime;

public class AboutWindow extends Stage {

    private final AboutViewModel viewModel;

    public AboutWindow(Stage parentStage, String applicationName, String applicationDescription) {

        ViewTuple<AboutView, AboutViewModel> viewTuple = FluentViewLoader.fxmlView(AboutView.class).load();
        viewModel = viewTuple.getViewModel();
        Parent root = viewTuple.getView();

        viewModel.applicationDescriptionProperty().set(applicationDescription);

        String year = Integer.toString(ZonedDateTime.now().getYear());
        viewModel.copyrightProperty().set("(c) " + year);

        viewModel.setCloseCommand(new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                close();
            }
        }));

        setTitle("About the " + applicationName);
        setScene(new Scene(root));

        setResizable(false);

        initOwner(parentStage);
        initModality(Modality.APPLICATION_MODAL);

        viewModel.groupIdProperty().set("de.saxsys.mvvmfx");
        viewModel.artifactIdProperty().set("java11-test");
        viewModel.versionProperty().set("1.0.0-SNAPSHOT");

    }

}
