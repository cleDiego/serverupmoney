/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.jwt;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author amand
 */
public class KeyGenerator {
    
    public Key generateKey(){
        
        //Key deve ser conhecida apenas ao servidor e ao cliente
        String keyString = "RDA3OTcwNDk3RkM2MkNDMEY3MTA1MUM0RDBDMDFENTY2NkEyNEVFOUZENzZDODQxNTlBOTI2MDY3NzMwNTU0Rg=="; //UpMoney após SHA-256 e EncodeBase64
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HmacSHA256");
        
        return key;
    }
}
