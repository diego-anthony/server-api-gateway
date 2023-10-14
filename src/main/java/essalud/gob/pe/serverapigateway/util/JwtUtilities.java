package essalud.gob.pe.serverapigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public abstract class JwtUtilities {

    public static boolean isValidTokenSignature(String token, String secretKey) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // En caso de que la firma no sea válida o el token esté mal formado, se lanzará una excepción
            return false;
        }
    }

    public static boolean isTokenExpired(String token, String secretKey) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date currentDate = DateUtilities.currentDate();
            return expirationDate.before(currentDate);
        } catch (Exception e) {
            // Si hay algún error al analizar el token, consideramos que ha expirado
            return true;
        }
    }

}
