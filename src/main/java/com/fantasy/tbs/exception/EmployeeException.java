package com.fantasy.tbs.exception;

public class EmployeeException extends RuntimeException
{
    public EmployeeException(String msg)
    {
        super(msg);
    }

    public EmployeeException(Exception exception)
    {
        super(exception);
    }

    public EmployeeException(String msg, Exception exception)
    {
        super(msg, exception);
    }
}
