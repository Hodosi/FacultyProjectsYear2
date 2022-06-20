package skeleton.services;

public class SkeletonException extends Exception {
    public SkeletonException(){

    }

    public SkeletonException(String message) {
        super(message);
    }

    public SkeletonException(String message, Throwable cause) {
        super(message, cause);
    }
}
