package ws.resource;

import facade.UsuarioFacade;
import java.security.Key;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.Avatar;
import model.ErrorMessager;
import model.NovaSenha;
import model.Usuario;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;

@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/usuario")
public class UsuarioResource {
    
    @Context
    private UriInfo context;
    private final UsuarioFacade usuarioFacade= new UsuarioFacade();
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext requestContext;
    
    public UsuarioResource() {}
    
    /** Recurso para inserção de um novo usuário */
    @POST
    @Path("/inserir")
    public Response insertUsuario(Usuario usuario, @HeaderParam("base64") String base64) {
        
        try {
            Usuario usuarioAdicionado = usuarioFacade.inserirNovoUsuario(usuario, base64);
            return Response
                .status(Response.Status.CREATED) //201 - A requisição foi bem sucedida e um novo recurso foi criado como resultado
                .entity(usuarioAdicionado).build();
            
        } catch (Exception e) {
            //e.printStackTrace();
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
            
        }
    }
    
    @PUT
    @Path("/alterar")
    public Response updateUsuario(Usuario usuario, @HeaderParam("base64") String base64) {
        try {
            Usuario usuarioAlterado = usuarioFacade.atualizarUsuario(usuario, base64);
            return Response
                .status(Response.Status.OK) //201 - A requisição foi bem sucedida e um novo recurso foi criado como resultado
                .entity(usuarioAlterado).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @PUT
    @Path("/recuperar")
    public Response recuperarSenha(Usuario usuario) {
        if(usuarioFacade.recuperarSenha(usuario.getEmail())){
            return Response
                .status(Response.Status.OK)
                .entity("1").build();
            
        } else {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("0").build();
        }
    }
    
    @PUT
    @Path("/alterarSenha")
    public Response alterarSenha(NovaSenha novaSenha) {
        novaSenha.setIdUsuario(getIdUsuarioByToken());
        if(usuarioFacade.alterarSenha(novaSenha)){
            return Response
                .status(Response.Status.OK)
                .entity("1").build();
        } else {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("0").build();
        }
    }
    
    @GET
    @Path("/avatar")
    public Response avatar() {
        
        List<Avatar> avatares = usuarioFacade.getAvatares();
        GenericEntity<List<Avatar>> gEntityAvatares = new GenericEntity<List<Avatar>>(avatares) {};
        return Response
            .status(Response.Status.OK) //200 - Esta requisição foi bem sucedida
            .entity(gEntityAvatares)
            .build();
    }
    
    private long getIdUsuarioByToken(){
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        return TokenJWTUtil.recuperarIdUsuario(token, key);
    }
}
