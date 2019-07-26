

package de.saxsys.mvvmfx.java11test.common.javafx.validator;

import de.saxsys.mvvmfx.utils.validation.FunctionBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import javafx.beans.value.ObservableValue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;
import java.util.function.Predicate;

public class ServerUriValidator extends FunctionBasedValidator<String> {
    public ServerUriValidator(ObservableValue<String> source) {
        super(source, new Function<String, ValidationMessage>() {
            @Override
            public ValidationMessage apply(String input) {
                if (input == null || input.trim().isEmpty()) {
                    return ValidationMessage.error("Field may not be empty.");
                }
                try {
                    new URI(input);
                } catch (URISyntaxException e) {
                    return ValidationMessage.error("URI Syntax is incorrect. (" + e.getMessage() + ")");
                }
                return null; // ok
            }
        });
    }

    public ServerUriValidator(ObservableValue<String> source, Predicate<String> predicate, ValidationMessage message) {
        super(source, predicate, message);
    }
}
