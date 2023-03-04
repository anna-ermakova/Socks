package pro.sky.socks.exception;

public class EmptyStockException extends RuntimeException{
    public EmptyStockException(String message) {
        super(message);
    }
}
