/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.jwt;

import java.security.Principal;

/*Representa o Principal no contexto de segurança*/
public class UserDetails implements Principal{
    
    private final String username;
    private final long idUsuario;
    
    public UserDetails(String username, long idUsuario){
        this.username = username;
        this.idUsuario = idUsuario;
    }
    
    @Override
    public String getName() {
        return username;
    }

    public long getIdUsuario() {
        return idUsuario;
    }
    
}
