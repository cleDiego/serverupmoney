/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package ws.filter;


import ws.exception.UnauthenticatedException;
import ws.jwt.JWTSecurityContext;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;
import ws.jwt.UserDetails;
import ws.resource.LoginJWTResource;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;
import ws.resource.*;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter{
    
    private KeyGenerator keyGenerator = new KeyGenerator();
    private String emailUsuario;
    private long idUsuario;
    
   
    @Context
    private UriInfo uriInfo;
        
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        //System.out.print("Entrou no método filter*********************************");
        
        /*Obtém o cabeçalho AUTHORIZATION da requisição, onde é enviado o Token JWT*/
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if(authorizationHeader != null){
            System.out.print("Passou na autorizacao*********************************");
            /*Retira a string Baeren e Fica apenas com o token*/
            String token = authorizationHeader.trim();
            /*Obtém a chave utilizada na assinatura do token*/
            Key key = keyGenerator.generateKey();
            
            if(TokenJWTUtil.tokenValido(token, key)){
                //System.out.print("TokenJWTUtil.tokenValido(token, keyo*********************************");
                this.emailUsuario = TokenJWTUtil.recuperarNome(token, key); //Recupera usuário e sua lista de permissões
                this.idUsuario = TokenJWTUtil.recuperarIdUsuario(token, key);
                UserDetails userDetails = new UserDetails(emailUsuario, idUsuario);
                
                boolean secure = requestContext.getSecurityContext().isSecure();
                /*Utiliza o contexto de segurança pra recuperar dados*/
                requestContext.setSecurityContext((new JWTSecurityContext(userDetails,secure)));
                return; //a rquisição passou pelo filtro
            }
        /*Para requisições que não necessitam de token*/    
        }else if (acessoParaLoginNaAPI(requestContext)){
            return;
        }else if (acessoCadastrarUsuario(requestContext)){
            return;
        }else if (acessoRecuperarSenha(requestContext)){
            return;
        }else if (acessoAvatares(requestContext)){
            return;
        }
        
        throw new UnauthenticatedException("Token inválido/expirado ou usuário não autenticado!");
    
    }

    private boolean acessoParaLoginNaAPI(ContainerRequestContext requestContext) {
        
        return requestContext.getUriInfo().getAbsolutePath().toString()
                .equals(uriInfo.getBaseUriBuilder().path(LoginJWTResource.class)
                        .build().toString());
    }

    private boolean acessoCadastrarUsuario(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getAbsolutePath().toString()
                .equals(uriInfo.getBaseUriBuilder().path(UsuarioResource.class)
                .path("inserir").build().toString());
    }
    
    private boolean acessoRecuperarSenha(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getAbsolutePath().toString()
                .equals(uriInfo.getBaseUriBuilder().path(UsuarioResource.class)
                .path("recuperar").build().toString());
    }
    
    private boolean acessoAvatares(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getAbsolutePath().toString()
                .equals(uriInfo.getBaseUriBuilder().path(UsuarioResource.class)
                .path("avatar").build().toString());
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }
    
    
    
    
}
