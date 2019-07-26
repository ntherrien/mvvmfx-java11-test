

package de.saxsys.mvvmfx.java11test.common.javafx.dialog.connection.model.serverlist.v2;

import com.github.jonpeterson.jackson.module.versioning.JsonVersionedModel;
import de.saxsys.mvvmfx.java11test.common.model.json.VersionedModel;

import java.util.List;

@JsonVersionedModel(currentVersion = "2")
public class ServerListModel extends VersionedModel {
    List<String> serverList;

    public ServerListModel() {
        super(ServerListModel.class.getName(), ServerListModel.class.getAnnotation(JsonVersionedModel.class).currentVersion());
    }

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
    }
}
