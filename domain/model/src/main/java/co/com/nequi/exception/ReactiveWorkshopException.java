package co.com.nequi.exception;

public class ReactiveWorkshopException extends Exception{
    public ReactiveWorkshopException(String message){
        super(message);
    }

    public ReactiveWorkshopException(Throwable throwable){
        super(throwable);
    }
}
