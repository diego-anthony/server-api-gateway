package essalud.gob.pe.serverapigateway.exception;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

}