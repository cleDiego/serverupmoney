/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.resource;

import facade.RelatorioFacade;
import java.math.BigDecimal;
import java.security.Key;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import model.Relatorio;
import ws.jwt.KeyGenerator;
import ws.jwt.TokenJWTUtil;


@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("/relatorio")
public class RelatorioResource {
    
    @Context
    private UriInfo context;
    private RelatorioFacade relatorioFacade = new RelatorioFacade();
    private KeyGenerator keyGenerator = new KeyGenerator();
    @Inject
    ContainerRequestContext requestContext;
    
    @GET
    @Path("/analisemensal")
    public Response getAnaliseMensal() {
        
        try {
            List<Relatorio> informacoes = relatorioFacade.getAnaliseMensal(this.getIdUsuarioByToken());
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(informacoes) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
    
    @GET
    @Path("/saldo")
    public Response getSaldoByUsuario() {
        
        try {
            BigDecimal saldo = relatorioFacade.obterSaldoByUsuario(this.getIdUsuarioByToken());
                    //contaFacade.getSaldoConta(id);
            //Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(saldo)
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
    @Path("/valorParcela/{valorTotal}/{qtdeParcelas}")
    public Response getvalorParcela(@PathParam("valorTotal") BigDecimal valorTotal, 
                                    @PathParam("qtdeParcelas") int qtde) {
        
        try {
            BigDecimal valorParcela = relatorioFacade.getValorParcela(valorTotal, qtde);
                    //contaFacade.getSaldoConta(id);
            //Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(valorParcela)
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
    @Path("/valorParcelaAlterada/{tipo}/{id}/{valorTotal}/{qtdeParcelas}")
    public Response getvalorParcelaAlterada(@PathParam("tipo") String tipo, 
                                    @PathParam("id") long id,
                                    @PathParam("valorTotal") BigDecimal valorTotal, 
                                    @PathParam("qtdeParcelas") int qtde) {
        
        try {
            BigDecimal valorParcela = relatorioFacade.getValorParcelaAlterada(tipo, id, valorTotal, qtde);
                    //contaFacade.getSaldoConta(id);
            //Conta conta = contaFacade.getContaById(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(valorParcela)
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
    @Path("/subcategoria/porcentagem")
    public Response getPorcentagemSubcategoria() {
        
        try {
            List<Relatorio> relatorioSubcategoria = relatorioFacade.obterPorcentagemLimiteGastoSubcategoria(this.getIdUsuarioByToken());
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(relatorioSubcategoria) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
    
    @GET
    @Path("/subcategoria/porcentagem/{idSubcategoria}")
    public Response getPorcentagemBySubcategoria(@PathParam("idSubcategoria") long idSubcategoria) {
        
        try {
            List<Relatorio> relatorioSubcategoria = relatorioFacade.obterPorcentagemLimiteSubcategoria(this.getIdUsuarioByToken(), idSubcategoria);
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(relatorioSubcategoria) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
    
    @GET
    @Path("/subcategoria/porcentagem/categoria/{idCategoria}")
    public Response getPorcentagemByCategoriaDespesa(@PathParam("idCategoria") long idCategoria) {
        
        try {
            List<Relatorio> relatorioSubcategoria = relatorioFacade.obterPorcentagemLimiteSubcategoriaByCategoria(this.getIdUsuarioByToken(), idCategoria);
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(relatorioSubcategoria) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
   
    @GET
    @Path("/categoriadespesa")
    public Response getPorcentagemCategoriaDespesa() {
        
        try {
            List<Relatorio> relatorios = relatorioFacade.getPorcentagemCategoriaDespesa(this.getIdUsuarioByToken());
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(relatorios) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
    
    @GET
    @Path("/categoriadespesa/analisegastos")
    public Response getAnaliseGastosMensalCatDespesa() {
        
        try {
            List<Relatorio> relatorios = relatorioFacade.getAnaliseMensalGastos(this.getIdUsuarioByToken());
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(relatorios) {};
            return Response
                .status(Response.Status.OK)
                .entity(gEntityCategorias).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
    
    @GET
    @Path("/porcentagem/{valorLimite}")
    public Response getPorcentagem(@PathParam("valorLimite") BigDecimal valorLimite) {
        
        try {
           BigDecimal porcentagem = relatorioFacade.getPorcentagem(valorLimite, this.getIdUsuarioByToken());
            return Response
                .status(Response.Status.OK)
                .entity(porcentagem).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
    }
    
    @GET
    @Path("/subcategoriasbyconta/{idConta}/{mes}")
    public Response getSaldoSubcategoriaByConta(@PathParam("idConta") long idConta,
                                                @PathParam("mes") int mes) {
        
        try {
           List<Relatorio> relatorios = relatorioFacade.getSaldoSubcategoriasByConta(this.getIdUsuarioByToken(), idConta, mes);
            GenericEntity<List<Relatorio>> gEntityCategorias = 
                    new GenericEntity<List<Relatorio>>(relatorios) {};
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
    @Path("/situacaogeral")
    public Response getSituacaoGeral() {
        
        try {
            Relatorio relatorio = relatorioFacade.getSituacaoGeral(this.getIdUsuarioByToken());
            return Response
                .status(Response.Status.OK)
                .entity(relatorio).build();
            
        } catch (Exception e) {
            ErrorMessager erro = new ErrorMessager();
            erro.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            erro.setStatusMessage(Response.Status.BAD_REQUEST.toString());
            erro.setErro(e.getMessage());
            e.printStackTrace();
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
