/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.resource;

import facade.LimiteFacade;
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
import model.LimiteCategoriaDespesa;
import model.LimiteSubcategoria;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;

/**
 *
 * @author amand
 */
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/remanejar")
public class LimiteResource {
    
    @Context
    private UriInfo context;
    LimiteFacade limiteFacade = new LimiteFacade();
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext requestContext;
    
    private long getIdUsuario(){
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        return TokenJWTUtil.recuperarIdUsuario(token, key);
    }
    
    @POST
    @Path("/subcategoria/inserir")
    public Response inserirLimiteSubcategoria(LimiteSubcategoria remanejarSubcategoria){
        try {
            LimiteSubcategoria ls = limiteFacade.inserirRemanejarSubcategoria(remanejarSubcategoria);
            return Response
                .status(Response.Status.CREATED)
                .entity(ls).build();
            
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
    
    @POST
    @Path("/categoria/inserir")
    public Response inserirLimiteCategoriaDespesa(LimiteCategoriaDespesa remanejarCategoriaDespesa) {
        try {
            LimiteCategoriaDespesa rcd = limiteFacade.inserirRemanejarCategoriaDespesa(remanejarCategoriaDespesa);
            return Response
                .status(Response.Status.CREATED)
                .entity(rcd).build();
            
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
    @Path("/subcategoria/alterar")  
    public Response alterarLimiteSubcategoria(LimiteSubcategoria remanejarSubcategoria) {
         try {
            LimiteSubcategoria newls = limiteFacade.alterarRemanejarSubcategoria(remanejarSubcategoria);
            return Response
                .status(Response.Status.OK)
                .entity(newls).build();
            
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
    @Path("/categoria/alterar")
    public Response alterarLimiteCategoriaDespesa(LimiteCategoriaDespesa remanejarCategoriaDespesa) {
        try {
            LimiteCategoriaDespesa newrcd = limiteFacade.alterarRemanejarCategoriaDespesa(remanejarCategoriaDespesa);
            return Response
                .status(Response.Status.OK)
                .entity(newrcd).build();
            
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
    
    @GET
    @Path("/subcategoria/usuario")
    public Response getLimiteSubcategoriaByUsuario() {
        try {
            List<LimiteSubcategoria> limiteSubcategorias = limiteFacade.getRemanejarSubcategoriaByUsuario(getIdUsuario());
            GenericEntity<List<LimiteSubcategoria>> genericEntity = new GenericEntity<List<LimiteSubcategoria>>(limiteSubcategorias){};
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
    @Path("/categoria/usuario")
    public Response getLimiteCategoriaDespesaByUsuario() {
        try {
            List<LimiteCategoriaDespesa> limiteCategorias = limiteFacade.getRemanejarCategoriaDespesaByUsuario(getIdUsuario());
            GenericEntity<List<LimiteCategoriaDespesa>> genericEntity = new GenericEntity<List<LimiteCategoriaDespesa>>(limiteCategorias){};
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
    
    @DELETE
    @Path("/subcategoria/excluir/{id}")
    public Response removerLimiteSubcategoria (@PathParam("id" ) long id) {
        if(limiteFacade.removerRemanejarSubcategoria(id)) {
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
    public Response removerLimiteCategoriaDespesa (@PathParam("id" ) long id) {
        if(limiteFacade.removerRemanejarCategoriaDespesa(id)) {
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
    @Path("/subcategoria/usuario/excluir")
    public Response removerLimiteSubcategoriaByUsuario () {
        if(limiteFacade.removerRemanejarSubcategoria(getIdUsuario())) {
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
    @Path("/categoria/usuario/excluir")
    public Response removerLimiteCategoriaDespesaByUsuario () {
        if(limiteFacade.removerRemanejarCategoriaDespesa(getIdUsuario())) {
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
    
}
