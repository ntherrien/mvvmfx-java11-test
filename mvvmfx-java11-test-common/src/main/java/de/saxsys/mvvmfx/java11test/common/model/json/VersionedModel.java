package de.saxsys.mvvmfx.java11test.common.model.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"modelName", "modelVersion"})
public abstract class VersionedModel {
    private final String modelName;
    private final String modelVersion;

    public VersionedModel(String modelName, String modelVersion) {
        this.modelName = modelName;
        this.modelVersion = modelVersion;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public String getModelName() {
        return modelName;
    }

}

