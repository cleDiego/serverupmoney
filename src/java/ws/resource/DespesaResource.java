
package ws.resource;

import facade.CategoriaFacade;
import facade.LancamentoFacade;
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
import model.Despesa;
import model.ErrorMessager;
import model.TipoRepeticao;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;

@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/despesa")
public class DespesaResource {
    @Context
    private UriInfo context;
    LancamentoFacade lancamentoFacade = new LancamentoFacade();
    UsuarioFacade usuarioFacade = new UsuarioFacade();
    CategoriaFacade categoriaFacade = new CategoriaFacade();
    
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext requestContext;
    
    @POST
    @Path("/inserir")
    public Response inserirDespesa(Despesa despesa) {
        
        try {
            Despesa novaDespesa = lancamentoFacade.inserirDespesa(despesa);
            return Response
                .status(Response.Status.CREATED)
                .entity(novaDespesa).build();
            
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
    public Response alterarDespesa(Despesa Despesa) {
        try {
            Despesa despesaAlterada = lancamentoFacade.alterarDespesa(Despesa);
            return Response
                .status(Response.Status.OK)
                .entity(despesaAlterada).build();
            
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
    @Path("/alterar/despesas")
    public Response alterarDespesas(Despesa Despesa) {
        try {
            Despesa despesaAlterada = lancamentoFacade.alterarDespesas(Despesa);
            return Response
                .status(Response.Status.OK)
                .entity(despesaAlterada).build();
            
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
    
    @DELETE
    @Path("/excluir/{id}")
    public Response removerDespesa(@PathParam("id" ) long id) {
        if(lancamentoFacade.removerDespesa(id)) {
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
    public Response getDespesasByConta(@PathParam("id") long id) {
        try {
            List<Despesa> Despesas = lancamentoFacade.getDespesaByConta(id);
            GenericEntity<List<Despesa>> genericEntity = new GenericEntity<List<Despesa>>(Despesas){};
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
    public Response getDespesasContaByMes(@PathParam("idConta") long id, @PathParam("mes") int mes) {
        try {
            List<Despesa> despesas = lancamentoFacade.getDespesasContaByMes(id, mes);
            GenericEntity<List<Despesa>> genericEntity = new GenericEntity<List<Despesa>>(despesas){};
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
    @Path("/{idSubcategoria}/{idConta}/{mes}")
    public Response getDespesaSubcategoriaContaByMes(
            @PathParam("idSubcategoria") long idSubcategoria,
            @PathParam("idConta") long idConta, 
            @PathParam("mes") Integer mes) {
        
        try {
            List<Despesa> despesas = lancamentoFacade.getDespesaSubcategoriaContaByMes(idSubcategoria, idConta, mes);
            GenericEntity<List<Despesa>> genericEntity = new GenericEntity<List<Despesa>>(despesas){};
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
    public Response getDespesaById(@PathParam("id") long id) {
        try {
            Despesa Despesa = lancamentoFacade.getDespesaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(Despesa)
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
    @Path("/subcategoria/{id}")
    public Response getDespesaBySubCategoria(@PathParam("id") long id) {
        
        try {
            List<Despesa> Despesas = lancamentoFacade.getDespesaBySubCategoria(id);
            GenericEntity<List<Despesa>> gEntity = new GenericEntity<List<Despesa>>(Despesas) {};
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
    @Path("/usuario")
    public Response getDespesasDoUsuario() {
        
        try {
            List<Despesa> despesas = lancamentoFacade.getDespesaByUsuario(this.getIdUsuarioByToken());
            GenericEntity<List<Despesa>> gEntity = new GenericEntity<List<Despesa>>(despesas) {};
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
    @Path("/mes/{mes}, {ano}")
    public Response getDespesasByMes(@PathParam("mes") Integer mes, @PathParam("ano") Integer ano) {
        
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        long idUsuario = TokenJWTUtil.recuperarIdUsuario(token, key);
        
        try {
            
            List<Despesa> Despesas = lancamentoFacade.getDespesasByMes(mes, ano, idUsuario);
            GenericEntity<List<Despesa>> gEntity = new GenericEntity<List<Despesa>>(Despesas) {};
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
    @Path("/valorgasto")
    public Response getValorGasto() {
        
        try {
            BigDecimal valorGastoMes = lancamentoFacade.getValorGastoMes(this.getIdUsuarioByToken());
            return Response
                .status(Response.Status.OK)
                .entity(valorGastoMes).build();
            
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
    @Path("/data/excluir/{id}")
    public Response removerDespesas(@PathParam("id") long id) {
        if(lancamentoFacade.removerDespesas(id)) {
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
    public Response removerDespesasByCategoria(@PathParam("id") long id) {
        if(lancamentoFacade.removerDespesasByCategoria(id)) {
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
    public Response removerDespesasByConta(@PathParam("id") long id) {
        if(lancamentoFacade.removerDespesasByConta(id)) {
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
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        return TokenJWTUtil.recuperarIdUsuario(token, key);
    }
}
