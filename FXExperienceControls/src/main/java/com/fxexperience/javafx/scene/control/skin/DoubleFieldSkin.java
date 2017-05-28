package com.fxexperience.javafx.scene.control.skin;

import com.fxexperience.javafx.scene.control.DoubleField;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class DoubleFieldSkin extends InputFieldSkin {
    private final InvalidationListener doubleFieldValueListener;

    /**
     * Create a new DoubleFieldSkin.
     * @param control The DoubleField
     */
    public DoubleFieldSkin(final DoubleField control) {
        super(control);

        // Whenever the value changes on the control, we need to update the text
        // in the TextField. The only time this is not the case is when the update
        // to the control happened as a result of an update in the text textField.
        control.valueProperty().addListener(doubleFieldValueListener = (Observable observable) -> {
            updateText();
        });
    }

    @Override public DoubleField getSkinnable() {
        return (DoubleField) control;
    }

    @Override public Node getNode() {
        return getTextField();
    }

    /**
     * Called by a Skinnable when the Skin is replaced on the Skinnable. This method
     * allows a Skin to implement any logic necessary to clean up itself after
     * the Skin is no longer needed. It may be used to release native resources.
     * The methods {@link #getSkinnable()} and {@link #getNode()}
     * should return null following a call to dispose. Calling dispose twice
     * has no effect.
     */
    @Override
    public void dispose() {
        ((DoubleField) control).valueProperty().removeListener(doubleFieldValueListener);
        super.dispose();
    }

    @Override
    protected boolean accept(String text) {
        if (text.length() == 0) return true;
        if (text.matches("[0-9\"\".]*")) {
            try {
                Double.parseDouble(text);
                return true;
            } catch (NumberFormatException ex) {
                Logger.getLogger(DoubleFieldSkin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    protected void updateText() {
        getTextField().setText("" + ((DoubleField) control).getValue());
    }

    @Override
    protected void updateValue() {
        double value = ((DoubleField) control).getValue();
        double newValue;
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim();
        try {
            newValue = Double.parseDouble(text);
            if (newValue != value) {
                ((DoubleField) control).setValue(newValue);
            }
        } catch (NumberFormatException ex) {
            // Empty string most likely
            ((DoubleField) control).setValue(0);
            Platform.runLater(() -> {
                getTextField().positionCaret(1);
            });
        }
    }
}
