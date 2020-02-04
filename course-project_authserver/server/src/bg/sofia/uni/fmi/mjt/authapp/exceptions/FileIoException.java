package bg.sofia.uni.fmi.mjt.authapp.exceptions;

public class FileIoException extends RuntimeException {
    public FileIoException(String... filePaths) {
        super("Failed to perform IO operation with files "
                + String.join(", ", filePaths));
    }
}
