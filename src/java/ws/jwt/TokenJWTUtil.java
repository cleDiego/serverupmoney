/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author amand
 */
public class TokenJWTUtil {
    
    private static KeyGenerator keyGenerator = new KeyGenerator();
    
    public static String gerarToken(String username, long id){
        
        Key key = keyGenerator.generateKey();
        
        //Nenhum campo declarado no payload é obrigatório
        String jwtToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, key)
                .setHeaderParam("typ","JWT")
                .setSubject(username) //assunto ao qual se refere o token
                .setIssuer("UpMoney") //Quem emite o token
                .setIssuedAt(new Date()) //Data de criação do token
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(6000L))) //Em quanto tempo o token deve expirar
                .claim("idUsuario", id)
                .compact();
        
       return jwtToken;
          
    }

    private static Date toDate(LocalDateTime localDateTime) {
       return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean tokenValido(String token, Key key) {
        try{
            /*Valida token*/
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String recuperarNome(String token, Key key) {
        /*Cria-se um objeto com todas as claims do token*/
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claimsJws.getBody().getSubject();
    }
    
    public static Long recuperarIdUsuario(String token, Key key) {
        /*Cria-se um objeto com todas as claims do token*/
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claimsJws.getBody().get("idUsuario", Long.class);
    }

}
