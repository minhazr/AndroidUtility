
package com.android.utility.exception;

public class ConstructorNotCalledException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -6967232896873613760L;

    public ConstructorNotCalledException() {
        super();
    }

    public ConstructorNotCalledException(String message) {
        super(message);
    }

    public ConstructorNotCalledException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstructorNotCalledException(Throwable cause) {
        super(cause);
    }

}
