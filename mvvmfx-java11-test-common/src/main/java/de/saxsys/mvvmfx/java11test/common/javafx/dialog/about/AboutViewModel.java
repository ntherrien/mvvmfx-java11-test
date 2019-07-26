

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.about;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AboutViewModel implements ViewModel {

    /*
    Properties
    */
    private StringProperty applicationDescription = new SimpleStringProperty();
    public StringProperty applicationDescriptionProperty() {
        return applicationDescription;
    }

    private StringProperty groupId = new SimpleStringProperty();
    public StringProperty groupIdProperty() {
        return groupId;
    }

    private StringProperty artifactId = new SimpleStringProperty();
    public StringProperty artifactIdProperty() {
        return artifactId;
    }

    private StringProperty version = new SimpleStringProperty();
    public StringProperty versionProperty() {
        return version;
    }

    private StringProperty copyright = new SimpleStringProperty();
    public StringProperty copyrightProperty() {
        return copyright;
    }



    /*
    Commands
    */
    private Command closeCommand;
    public Command getCloseCommand() {
        return closeCommand;
    }
    public void setCloseCommand(Command closeCommand) {
        this.closeCommand = closeCommand;
    }

}
