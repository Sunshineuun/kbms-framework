package com.winning.mybatis.exceptions;

public class MybatisException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public MybatisException()
	{
		super();
	}

	public MybatisException(String message)
	{
		super(message);
	}

	public MybatisException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MybatisException(Throwable cause)
	{
		super(cause);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
