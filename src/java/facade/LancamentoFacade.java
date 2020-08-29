
package facade;

import dao.LancamentoDAO;
import java.math.BigDecimal;
import java.util.List;
import model.Despesa;
import model.Receita;
import model.TipoRepeticao;

public class LancamentoFacade {
    
    private LancamentoDAO lancamentoDAO;

    public LancamentoFacade() {
        lancamentoDAO = new LancamentoDAO();
    }
    
    public List<TipoRepeticao> getTipoRepeticao(){
        return lancamentoDAO.getTiposRepeticao();
    }
    
    public TipoRepeticao getTipoRepeticaoById(long id){
        return lancamentoDAO.getTipoRepeticaoById(id);
    }
    
    public Receita inserirReceita(Receita receita){
        return lancamentoDAO.inserirReceita(receita);
    }
    
    public Receita alterarReceita(Receita receita){
        return lancamentoDAO.alterarReceita(receita);
    }
    
    public Receita alterarReceitas(Receita receita){
        return lancamentoDAO.alterarReceitas(receita);
    }
    
    public boolean removerReceita(long id){
        return lancamentoDAO.removerReceita(id);
    }
    
    public boolean removerDespesa(long id){
        return lancamentoDAO.removerDespesa(id);
    }
    
    public boolean removerReceitas(long idReceita){
        return lancamentoDAO.removerReceitas(idReceita);
    }
    
    public boolean removerDespesas(long id){
        return lancamentoDAO.removerDespesas(id);
    }
    
    public List<Receita> getReceitasByConta(long id){
        return lancamentoDAO.getReceitasByConta(id);
    }
    
    public List<Despesa> getDespesaByConta(long idConta){
        return lancamentoDAO.getDespesaByConta(idConta);
    }
    
    public List<Receita> getReceitasByCategoria(long idCategoriaReceita){
        return lancamentoDAO.getReceitaByCategoria(idCategoriaReceita);
    }
    
    public List<Despesa> getDespesaBySubCategoria(long idSubDespesa){
        return lancamentoDAO.getDespesaBySubCategoria(idSubDespesa);
    }
    
    public Receita getReceitaById(long id){
        return lancamentoDAO.getReceitaById(id);
    }
    
    public Despesa getDespesaById(long idDespesa){
        return lancamentoDAO.getDespesaById(idDespesa);
    }
    
    public List<Receita> getReceitasByUsuario(long id){
        return lancamentoDAO.getReceitasByUsuario(id);
    }
    
    public List<Despesa> getDespesaByUsuario(long id){
        return lancamentoDAO.getDespesaByUsuario(id);
    }
    
    public List<Receita> getReceitasByMes(Integer mes, Integer ano, long idUsuario){
        return lancamentoDAO.getReceitasByMes(mes, ano, idUsuario);
    }
    
    public List<Despesa> getDespesasByMes(Integer mes, Integer ano, long idUsuario){
        return lancamentoDAO.getDespesaByMes(mes, ano, idUsuario);
    }
    
    public List<Receita> getReceitasContasByMes( long idConta, Integer mes){
        return lancamentoDAO.getReceitasContaByMes(idConta, mes);
    }
    
    public List<Despesa> getDespesasContaByMes(long idConta, Integer mes){
        return lancamentoDAO.getDespesaContaByMes(idConta, mes);
    }
    
    /*ESSE METODO NAO VAI AO SERVIDOR, ELE EH CHAMADO QUANDO UMA CATEGORIA RECEITA EH DELETADA*/
    public boolean removerReceitasByCategoria(long idCategoria){
        return lancamentoDAO.removerReceitasByCategoria(idCategoria);
    }
    
    /*ESSE METODO NAO VAI AO SERVIDOR, ELE EH CHAMADO QUANDO UMA SUBCATEGORIA EH DELETADA 
    - VER ONDE COLOCAR ESSA CHAMADA CATEGORIARESOURCE, CATEGORIAFACADE OU CATEGORIADAO*/
    public boolean removerDespesasByCategoria(long idCategoria){
        return lancamentoDAO.removerDespesasByCategoria(idCategoria);
    }
    
    /*ESSE METODO NAO VAI AO SERVIDOR, ELE EH CHAMADO QUANDO UMA CONTA EH DELETADA*/
    public boolean removerReceitasByConta(long idConta){
        return lancamentoDAO.removerReceitasByConta(idConta);
    }
    
    /*ESSE METODO NAO VAI AO SERVIDOR, ELE EH CHAMADO QUANDO UMA SUBCATEGORIA EH DELETADA 
    - VER ONDE COLOCAR ESSA CHAMADA CATEGORIARESOURCE, CATEGORIAFACADE OU CATEGORIADAO*/
    public boolean removerDespesasByConta(long idConta){
        return lancamentoDAO.removerDespesasByConta(idConta);
    }
       
    public Despesa inserirDespesa(Despesa despesa){
        return lancamentoDAO.inserirDespesa(despesa);
    }
    
    public Despesa alterarDespesa(Despesa despesa){
        return lancamentoDAO.alterarDespesa(despesa);
    }
    
    public Despesa alterarDespesas(Despesa despesa){
        return lancamentoDAO.alterarDespesas(despesa);
    }
    
    public BigDecimal getValorGastoMes(long id){
        return lancamentoDAO.getTotalGastoMes(id);
    }
    
    public BigDecimal getValorGastoMes(long id, int mes){
        return lancamentoDAO.getTotalGastoMes(id, mes);
    }
    
    public BigDecimal getTotalReceitaMes(long idUsuario){
        return lancamentoDAO.getTotalReceitaMes(idUsuario);
    }
    
    public List<Despesa> getDespesaSubcategoriaContaByMes(long idSubcategoria, long idConta, Integer mes){
        return lancamentoDAO.getDespesaSubcategoriaContaByMes(idSubcategoria, idConta, mes);
    }
}
