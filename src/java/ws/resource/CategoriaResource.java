/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.resource;

import facade.CategoriaFacade;
import ws.jwt.KeyGenerator;
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
import model.CategoriaDespesa;
import model.CategoriaReceita;
import model.ErrorMessager;
import model.Subcategoria;
import ws.jwt.TokenJWTUtil;

@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/categoria")
public class CategoriaResource {
    
    @Context
    private UriInfo context;
    LancamentoFacade lancamentoFacade = new LancamentoFacade();
    UsuarioFacade usuarioFacade = new UsuarioFacade();
    CategoriaFacade categoriaFacade = new CategoriaFacade();
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext requestContext;
    
    @POST
    @Path("/receita/inserir")
     public Response inserirCategoriaReceita(CategoriaReceita categoria) {
        
        try {
            CategoriaReceita categoriaReceitaAdicionada = categoriaFacade.inserirCategoriaReceita(categoria);
            return Response
                .status(Response.Status.CREATED)
                .entity(categoriaReceitaAdicionada).build();
            
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
     
    @PUT
    @Path("/receita/alterar")
    public Response alterarCategoriaReceita(CategoriaReceita categoria){
        
        try {
            CategoriaReceita categoriaReceitaAlterada = categoriaFacade.alterarCategoriaReceita(categoria);
            return Response
                .status(Response.Status.OK)
                .entity(categoriaReceitaAlterada).build();
            
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
    @Path("/receita/excluir/{id}")
    public Response removeCategoriaReceita(@PathParam("id") long id ) {
        
        try {
            //corrigido erro 500 a facade retorna um bollean mas a resposta nãoa ceita enviar ele
            if(categoriaFacade.removeCategoriaReceita(id)) {
                return Response
                    .status(Response.Status.OK)
                    .entity("1").build();
            }else {
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("0").build();
            }
            
            
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
    @Path("/receita/todas")
    public Response getCategoriasReceitasByUsuario() {
        
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        long idUsuario = TokenJWTUtil.recuperarIdUsuario(token, key);
        
        try {
            List<CategoriaReceita> categorias = categoriaFacade.getCategoriasReceitasByUsuario(idUsuario);
            GenericEntity<List<CategoriaReceita>> gEntityCategorias = new GenericEntity<List<CategoriaReceita>>(categorias) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
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
    @Path("/receita/usuario")
    public Response getCategoriasReceitasDoUsuario() {
        
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();

        long idUsuario = TokenJWTUtil.recuperarIdUsuario(token, key);
        
        try {
            List<CategoriaReceita> categorias = categoriaFacade.getCategoriaReceitaDoUsuario(idUsuario);
            GenericEntity<List<CategoriaReceita>> gEntityCategorias = new GenericEntity<List<CategoriaReceita>>(categorias) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
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
    @Path("/receita/{id}")
    public Response getCategoriasReceitasById(@PathParam("id") long id) {
        
        try {
            CategoriaReceita categoria = categoriaFacade.getCategoriasReceitasById(id);
            return Response
                .status(Response.Status.OK)
                .entity(categoria).build();
            
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
    
    
    
    /* Categoria Despesa */
    @GET
    @Path("/despesa")
    public Response getCategoriasDespesa() {
        try {
            List<CategoriaDespesa> categoriaDespesa = categoriaFacade.getCategoriasDespesa();
            GenericEntity<List<CategoriaDespesa>> gEntityCategorias = 
                    new GenericEntity<List<CategoriaDespesa>>(categoriaDespesa) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
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
    
    
    /* Subcategoria */
    @POST
    @Path("/subcategoria/inserir")
    public Response inserirSubcategoria(Subcategoria subcategoria) {
        
        try {
            Subcategoria subcategoriaAdicioanada = categoriaFacade.inserirSubcategoria(subcategoria);
            return Response
                .status(Response.Status.CREATED)
                .entity(subcategoriaAdicioanada).build();
            
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
    
    @PUT
    @Path("/subcategoria/alterar")
    public Response alterarSubcategoria(Subcategoria subcategoria) {
        
        try {
            Subcategoria subcategoriaAlterada = categoriaFacade.alterarSubcategoria(subcategoria);
            return Response
                .status(Response.Status.OK)
                .entity(subcategoriaAlterada).build();
            
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
    @Path("/subcategoria/excluir/{id}")
    public Response removerSubcategoria(@PathParam("id") long id) {
        
        try {
            
            String res =  Boolean.toString(categoriaFacade.removerSubcategoria(id));
            return Response
                .status(Response.Status.OK)
                .entity(res).build();
            
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
    @Path("/subcategoria/usuario")
    public Response getSubcategoriaByUsuario() {
        
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();

        long idUsuario = TokenJWTUtil.recuperarIdUsuario(token, key);
        
        try {
            List<Subcategoria> subcategorias = categoriaFacade.getSubcategoriasByUsuario(idUsuario);
            GenericEntity<List<Subcategoria>> gEntityCategorias = 
                    new GenericEntity<List<Subcategoria>>(subcategorias) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
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
    @Path("/subcategoria/usuario/{id}")
    public Response getSubcategoriaDoUsuario(@PathParam("id") long id) {
        
        try {
            List<Subcategoria> subcategorias = categoriaFacade.getSubcategoriasDoUsuario(id);
            GenericEntity<List<Subcategoria>> gEntityCategorias = 
                    new GenericEntity<List<Subcategoria>>(subcategorias) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
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
    @Path("/subcategoria/categoria/{id}")
    public Response getSubcategoriasByCategoriaDespesa(@PathParam("id") long id) {
        
        try {
            List<Subcategoria> subcategorias = categoriaFacade.getSubcategoriasByCategoriaDespesa(id, this.getIdUsuarioByToken());
            GenericEntity<List<Subcategoria>> gEntityCategorias = 
                    new GenericEntity<List<Subcategoria>>(subcategorias) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
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
    
    private long getIdUsuarioByToken(){
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.trim();
        /*Obtém a chave utilizada na assinatura do token*/
        Key key = keyGenerator.generateKey();
        return TokenJWTUtil.recuperarIdUsuario(token, key);
    }
}
