package in.BusReservation.Userservice.Exception;

public class PasswordIncorrectException extends  RuntimeException{
    public PasswordIncorrectException(String message){
        super(message);
    }
}
