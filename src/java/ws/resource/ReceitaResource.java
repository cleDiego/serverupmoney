
package ws.resource;

import facade.CategoriaFacade;
import facade.LancamentoFacade;
import facade.UsuarioFacade;
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
import model.ErrorMessager;
import model.Receita;
import model.TipoRepeticao;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;

@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/receita")
public class ReceitaResource {
    @Context
    private UriInfo context;
    LancamentoFacade lancamentoFacade = new LancamentoFacade();
    UsuarioFacade usuarioFacade = new UsuarioFacade();
    CategoriaFacade categoriaFacade = new CategoriaFacade();
    private Object requestContext;
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext rC;
    
    @POST
    @Path("/inserir")
    public Response inserirReceita(Receita receita) {
        
        try {
            Receita novaReceita = lancamentoFacade.inserirReceita(receita);
            return Response
                .status(Response.Status.CREATED)
                .entity(novaReceita).build();
            
        } catch(Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
        
    }
    
    @PUT
    @Path("/alterar")
    public Response alterarReceita(Receita receita) {
        try {
            Receita novaReceita = lancamentoFacade.alterarReceita(receita);
            return Response
                .status(Response.Status.OK)
                .entity(novaReceita).build();
            
        } catch(Exception e) {
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
    @Path("/alterar/receitas")
    public Response alterarReceitas(Receita receita) {
        try {
            Receita novaReceita = lancamentoFacade.alterarReceitas(receita);
            return Response
                .status(Response.Status.OK)
                .entity(novaReceita).build();
            
        } catch(Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro).build();
        }
    }
    
    @DELETE
    @Path("/excluir/{id}")
    public Response removerReceita(@PathParam("id" ) long id) {
        if(lancamentoFacade.removerReceita(id)) {
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
    @Path("/conta/{id}")
    public Response getReceitasByConta(@PathParam("id") long id) {
        try {
            List<Receita> receitas = lancamentoFacade.getReceitasByConta(id);
            GenericEntity<List<Receita>> genericEntity = new GenericEntity<List<Receita>>(receitas){};
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
    @Path("/conta/{idConta}/{mes}")
    public Response getReceitasContaByMes(@PathParam("idConta") long id, @PathParam("mes") int mes) {
        try {
            List<Receita> receitas = lancamentoFacade.getReceitasContasByMes(id, mes);
            GenericEntity<List<Receita>> genericEntity = new GenericEntity<List<Receita>>(receitas){};
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
    public Response getReceitaById(@PathParam("id") long id) {
        try {
            Receita receita = lancamentoFacade.getReceitaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(receita)
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
    @Path("/categoria/{id}")
    public Response getReceitasByCategoria(@PathParam("id") long id) {
        
        try {
            List<Receita> receitas = lancamentoFacade.getReceitasByCategoria(id);
            GenericEntity<List<Receita>> gEntity = new GenericEntity<List<Receita>>(receitas) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntity).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
    }
    
    @GET
    @Path("/usuario/{id}")
    public Response getReceitasByUsuario(@PathParam("id") long id) {
        
        try {
            
            List<Receita> receitas = lancamentoFacade.getReceitasByUsuario(id);
            GenericEntity<List<Receita>> gEntity = new GenericEntity<List<Receita>>(receitas) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntity).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
    }
    
    @GET
    @Path("/tiposRepeticao")
    public Response getTipoRepeticao() {
        
        try {
            List<TipoRepeticao> tpRepeticoes = lancamentoFacade.getTipoRepeticao();
            GenericEntity<List<TipoRepeticao>> genericEntity = new GenericEntity<List<TipoRepeticao>>(tpRepeticoes){};
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
    @Path("/mes/{mes}, {ano}, {id}")
    public Response getReceitasByMes(@PathParam("mes") Integer mes, 
                                        @PathParam("ano") Integer ano, 
                                        @PathParam("id") long id) {
        
        try {
            List<Receita> receitas = lancamentoFacade.getReceitasByMes(mes, ano, id);
            GenericEntity<List<Receita>> gEntity = new GenericEntity<List<Receita>>(receitas) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntity).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
    }
    
    
    @DELETE
    @Path("/excluir/receitas/{idReceita}")
    public Response removerReceitas(@PathParam("idReceita") long idReceita) {
        
        if(lancamentoFacade.removerReceitas(idReceita)) {
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
    
    @DELETE
    @Path("/categoria/excluir/{id}")
    public Response removerReceitasByCategoria(@PathParam("id") long id) {
        if(lancamentoFacade.removerReceitasByCategoria(id)) {
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
    
    @DELETE
    @Path("/conta/excluir/{id}")
    public Response removerReceitasByConta(@PathParam("id") long id) {
        if(lancamentoFacade.removerReceitasByConta(id)) {
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
    
        
    private long getIdUsuarioByToken(){
        String authorizationHeader = rC.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        return TokenJWTUtil.recuperarIdUsuario(token, key);
    }
}
