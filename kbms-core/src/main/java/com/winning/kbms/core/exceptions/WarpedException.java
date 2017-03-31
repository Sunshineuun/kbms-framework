package com.winning.kbms.core.exceptions;

public class WarpedException extends ServiceException {

	private static final long serialVersionUID = 4260568886379886130L;

	public WarpedException() {
		super();
	}

	public WarpedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public WarpedException(String arg0) {
		super(arg0);
	}

	public WarpedException(Throwable arg0) {
		super(arg0);
	}
}
