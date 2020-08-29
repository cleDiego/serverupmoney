/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.resource;

import facade.UsuarioFacade;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.jwt.TokenJWTUtil;
import model.Usuario;
import ws.exception.UnauthenticatedException;
import ws.jwt.JWTSecurityContext;
import ws.jwt.UserDetails;


@Path("/login")
public class LoginJWTResource {
    
    private final UsuarioFacade usuarioFacade= new UsuarioFacade();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticarUsuario(Usuario u) {
        try{
            System.out.println("Autenticar:***************************************************************");
            Usuario usuario = usuarioFacade.autenticarLogin(u);
            String token;
            try {
                token = TokenJWTUtil.gerarToken(usuario.getEmail(),usuario.getId());
                System.out.println("Efetuado Login do Usuario "+usuario.getEmail()+" id: "+usuario.getId());
            }catch(NullPointerException e) {
                throw new UnauthenticatedException("Usuário ou Senha incorretos ou Token Inválido");
            }
            
            return Response
                .status(Response.Status.OK) //200 - Esta requisição foi bem sucedida
                .header("Authorization", token)
                .entity(usuario)
                .build();
        }catch(Exception e){
            e.getMessage();
            e.printStackTrace();
            System.out.println("Erro na Autenticação:***************************************************************");
            throw new UnauthenticatedException(e.getMessage());
        }
    }
    
    @POST
    @Path("/reflesh")
    public Response atualizarToken(@Context ContainerRequestContext requestContext){
        
        /*Recupera dados do Token*/
        JWTSecurityContext JWTSecurityContext = (JWTSecurityContext) requestContext.getSecurityContext();
        UserDetails userDetails = (UserDetails) JWTSecurityContext.getUserPrincipal();
        String token = TokenJWTUtil.gerarToken(userDetails.getName(), userDetails.getIdUsuario());
        
        /*Devolve o token no cabeçalho da requisição*/
        return Response
                .ok()
                .header("Authorization", "Baerer" + token)
                .build();
    }
    
}
