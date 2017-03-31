package com.winning.kbms.core.exceptions;

/**
 * 
 * @author liugang
 * 
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 4030975478462953548L;

    public ServiceException() {
        super();
    }

    public ServiceException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ServiceException(String arg0) {
        super(arg0);
    }

    public ServiceException(Throwable arg0) {
        super(arg0);
    }
}
