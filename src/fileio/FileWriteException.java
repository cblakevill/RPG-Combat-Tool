package fileio;

/**
 * Exception for handling file writing errors
 */
public class FileWriteException extends Exception
{
    public FileWriteException(String message, Throwable e)
    {
        super(message, e);
    }

    public FileWriteException(String message)
    {
        super(message);
    }
}
