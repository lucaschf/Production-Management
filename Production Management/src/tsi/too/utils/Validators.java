package tsi.too.utils;

import tsi.too.Constants;
import tsi.too.io.InputDialog;

public class Validators {
    public static  final int MIN_NAME_LENGTH = 3;

    public static final InputDialog.InputValidator<String> nameValidator = input -> {
        if (input.isBlank())
            return Constants.NAME_CANNOT_BE_BLANK;
        if (input.length() < MIN_NAME_LENGTH)
            return Constants.NAME_IS_TO_SHORT;
        return null;
    };

    public static final InputDialog.InputValidator<Double> priceValidator = input -> {
        if (input <= 0)
            return Constants.PRICE_MUST_BE_GREATER_THAN_ZERO;

        return null;
    };
}
