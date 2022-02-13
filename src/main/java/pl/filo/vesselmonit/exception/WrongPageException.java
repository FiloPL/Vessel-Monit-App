package pl.filo.vesselmonit.exception;

public class WrongPageException extends RuntimeException {
    public WrongPageException(final String errorMassage) {
        super(errorMassage);
    }
}
