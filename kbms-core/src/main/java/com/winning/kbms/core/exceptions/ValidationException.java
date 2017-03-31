package com.winning.kbms.core.exceptions;

/**
 * 
 * @author liugang
 * 
 */
public class ValidationException extends ServiceException {
    private static final long serialVersionUID = 4030975478462953548L;

    public ValidationException() {
        super();
    }

    public ValidationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ValidationException(String arg0) {
        super(arg0);
    }

    public ValidationException(Throwable arg0) {
        super(arg0);
    }
}
