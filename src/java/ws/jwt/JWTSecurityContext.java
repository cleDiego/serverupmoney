/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.jwt;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;


public class JWTSecurityContext implements SecurityContext{
    
    private UserDetails  userDetails;
    private final boolean secure;

    public JWTSecurityContext(UserDetails userDetails, boolean secure) {
        this.userDetails = userDetails;
        this.secure = secure;
    }
   
    @Override
    public Principal getUserPrincipal() {
        return this.userDetails;
    }

    @Override
    public boolean isUserInRole(String string) {
        return false; //Tem que entender como aplicar no contexto do tcc
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
    
}
