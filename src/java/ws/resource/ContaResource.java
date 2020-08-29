
package ws.resource;

import facade.ContaFacade;
import facade.UsuarioFacade;
import java.math.BigDecimal;
import java.security.Key;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.Conta;
import model.ErrorMessager;
import model.TipoConta;
import model.Usuario;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;

@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/conta")
public class ContaResource {
    /** Aguardando model(bean), facade e dao **/
    
    @Context
    private UriInfo context;
    ContaFacade contaFacade = new ContaFacade();
    UsuarioFacade usuarioFacade = new UsuarioFacade();
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext requestContext;
    
    @POST
    @Path("/inserir")
    public Response inserirNovaConta(Conta conta) {
        try {
            Conta novaConta = contaFacade.inserirNovaConta(conta);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(novaConta)
                    .build();
            
        }catch (Exception e) {
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
    public Response alterarConta(Conta conta) {
        try {
            Conta contaAlterada = contaFacade.alterarConta(conta);
            return Response
                    .status(Response.Status.OK)
                    .entity(contaAlterada)
                    .build();
            
        }catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @DELETE
    @Path("/excluir/{id}")
    public Response removerConta(@PathParam("id") long id) {
        if(contaFacade.removerConta(id)) {
            return Response
                    .status(Response.Status.OK)
                    .entity("1")
                    .build();
        }else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("0").build();
        }
    }
    
    @GET
    @Path("/usuario")
    public Response getContasByUsuario() {
        
        try {
            Usuario usuario = usuarioFacade.getUsuarioById(this.getIdUsuarioByToken());
            List<Conta> contas = contaFacade.getContasByUsuario(usuario);
            GenericEntity<List<Conta>> genericEntity = new GenericEntity<List<Conta>>(contas){};
            return Response
                    .status(Response.Status.OK)
                    .entity(genericEntity)
                    .build();
            
        }catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getContaById(@PathParam("id") long id) {
        
        try {
            Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(conta)
                    .build();
            
        }catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @GET
    @Path("/saldomensal/{id}")
    public Response getSaldoMensalConta(@PathParam("id") long id) {
        
        try {
            BigDecimal saldoConta = contaFacade.getSaldoContaMensal(id);
            //Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(saldoConta)
                    .build();
            
        }catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @GET
    @Path("/saldo/{id}")
    public Response getSaldoConta(@PathParam("id") long id) {
        
        try {
            BigDecimal saldoConta = contaFacade.getSaldoConta(id);
            //Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(saldoConta)
                    .build();
            
        }catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @GET
    @Path("/saldototal")
    public Response getSaldoTotal() {
        
        try {
            BigDecimal saldoConta = contaFacade.getSaldoTotal(this.getIdUsuarioByToken());
            //Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(saldoConta)
                    .build();
            
        }catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @GET
    @Path("/tiposConta")
    public Response getTipoContas() {
        List<TipoConta> tpcontas = contaFacade.getTipoContas();
        GenericEntity<List<TipoConta>> gEntityTiposContas = new GenericEntity<List<TipoConta>>(tpcontas) {};
        return Response
            .status(Response.Status.OK) //200 - Esta requisição foi bem sucedida
            .entity(gEntityTiposContas)
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
