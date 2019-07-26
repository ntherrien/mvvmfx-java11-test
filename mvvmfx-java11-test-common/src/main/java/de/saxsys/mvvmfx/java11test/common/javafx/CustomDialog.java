

package de.saxsys.mvvmfx.java11test.common.javafx;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CustomDialog<ResultType> extends Stage {

    private final Map<ButtonType, Node> buttonNodes = new HashMap<>();

    private double prefX = Double.NaN;
    private double prefY = Double.NaN;

    private ButtonType result;
    private Optional<ResultType> value;

    // Default result converter
    private Callback<ButtonType, Optional<ResultType>> resultConverter = new Callback<ButtonType, Optional<ResultType>>() {
        @Override
        public Optional<ResultType> call(ButtonType param) {
            return Optional.empty();
        }
    };


    public CustomDialog(Stage parent) {

        setResizable(false);

        setOnCloseRequest(windowEvent -> {
            if (requestPermissionToClose()) {
                setResult(ButtonType.CANCEL);
                close();
            } else {
                // if we are here, we consume the event to prevent closing the dialog
                windowEvent.consume();
            }
        });

        addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                if (!keyEvent.isConsumed() && requestPermissionToClose()) {
                    close();
                    keyEvent.consume();
                }
            }
        });

        initModality(Modality.APPLICATION_MODAL);
        initOwner(parent);

    }

    protected boolean requestPermissionToClose() {
        return true;
    }


    private void positionStageOnOwner() {
        Scene scene = getScene();
        Parent root = scene.getRoot();
        double x = getX();
        double y = getY();

        // if the user has specified an x/y location, use it
        if (!Double.isNaN(x) && !Double.isNaN(y) &&
                Double.compare(x, prefX) != 0 && Double.compare(y, prefY) != 0) {
            // weird, but if I don't call setX/setY here, the stage
            // isn't where I expect it to be (in instances where a single
            // dialog is shown and closed multiple times). I expect the
            // second showing to be in the place the dialog was when it
            // was closed the first time, but on Windows it jumps to the
            // top-left of the screen.
            setX(x);
            setY(y);
            return;
        }

        // Firstly we need to force CSS and layout to happen, as the dialogPane
        // may not have been shown yet (so it has no dimensions)
        root.applyCss();
        root.layout();

        final Window owner = getOwner();
        final Scene ownerScene = owner.getScene();

        // scene.getY() seems to represent the y-offset from the top of the titlebar to the
        // start point of the scene, so it is the titlebar height
        final double titleBarHeight = ownerScene.getY();

        // because Stage does not seem to centre itself over its owner, we
        // do it here.

        // then we can get the dimensions and position the dialog appropriately.
        final double dialogWidth = root.prefWidth(-1);
        final double dialogHeight = root.prefHeight(dialogWidth);

//        stage.sizeToScene();

        x = owner.getX() + (ownerScene.getWidth() / 2.0) - (dialogWidth / 2.0);
        y = owner.getY() + titleBarHeight / 2.0 + (ownerScene.getHeight() / 2.0) - (dialogHeight / 2.0);

        prefX = x;
        prefY = y;

        setX(x);
        setY(y);
    }

    @Override
    public void centerOnScreen() {
        Window owner = getOwner();
        if (owner != null) {
            positionStageOnOwner();
        } else {
            if (getWidth() > 0 && getHeight() > 0) {
                super.centerOnScreen();
            }
        }
    }


    public final ButtonType getResult() {
        return result;
    }

    public void setResult(ButtonType result) {
        this.result = result;
    }

    public Optional<ResultType> getValue() {
        return value;
    }

    private void setValue(Optional<ResultType> value) {
        this.value = value;
    }

    public void addDialogEventHandler(ButtonType buttonType, Node node) {
        buttonNodes.put(buttonType, node);
        node.addEventHandler(ActionEvent.ACTION, ae -> {
            if (ae.isConsumed()) return;
            if (CustomDialog.this != null) {
                Callback<ButtonType, Optional<ResultType>> resultConverter = getResultConverter();
                setResult(buttonType);
                Optional<ResultType> resultValue = resultConverter.call(buttonType);
                setValue(resultValue);
                close();
            }
        });

    }


    public void setResultConverter(Callback<ButtonType, Optional<ResultType>> value) {
        if(value == null) throw new IllegalArgumentException("Result converter cannot be null.");
        resultConverter = value;
    }

    public Callback<ButtonType, Optional<ResultType>> getResultConverter() {
        return resultConverter;
    }

}
