package fileio;

/**
 * Exception for handling file reading errors
 */
public class FileReadException extends Exception
{
    public FileReadException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FileReadException(String message)
    {
        super(message);
    }
}
