package com.winning.mybatis.exceptions;

public class MappingException extends MybatisException
{
    private static final long serialVersionUID = 1L;

    public MappingException ()
    {
        super ();
    }

    public MappingException (String message)
    {
        super (message);
    }

    public MappingException (String message, Throwable cause)
    {
        super (message, cause);
    }

    public MappingException (Throwable cause)
    {
        super (cause);
    }
}
