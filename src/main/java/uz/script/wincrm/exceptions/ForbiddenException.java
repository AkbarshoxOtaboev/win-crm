package uz.script.wincrm.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException (String message){
        super(message);
    }
}
