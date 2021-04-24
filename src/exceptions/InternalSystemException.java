package exceptions;

public class InternalSystemException extends CouponSystemException{

    public InternalSystemException() {
    }

    public InternalSystemException(String message) {
        super(message);
    }

    public InternalSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalSystemException(Throwable cause) {
        super(cause);
    }
}
