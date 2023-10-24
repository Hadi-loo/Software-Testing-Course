package exceptions;

import static defines.Errors.INVALID_RATE_RANGE;

public class InvalidScoreRange extends Exception {
    public InvalidScoreRange() {
        super(INVALID_RATE_RANGE);
    }
}
