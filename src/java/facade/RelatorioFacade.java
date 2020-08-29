/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import dao.RelatorioDAO;
import java.math.BigDecimal;
import java.util.List;
import model.Relatorio;


public class RelatorioFacade {
    
    RelatorioDAO relatorioDAO;

    public RelatorioFacade() {
        relatorioDAO = new RelatorioDAO();
    }
          
    public List<Relatorio> getAnaliseMensal(long idUsuario){
        return relatorioDAO.getAnaliseMensal(idUsuario);
    }
    
    public BigDecimal obterSaldoByUsuario(long idUsuario){
        return relatorioDAO.obterSaldoByUsuario(idUsuario);
    }
    
    public List<Relatorio> obterPorcentagemLimiteGastoSubcategoria (long idUsuario){
        return relatorioDAO.getPorcentagemLimiteGastoSubcategoria(idUsuario);
    }
    
    public List<Relatorio> obterPorcentagemLimiteSubcategoria (long idUsuario, long idSubcategoria){
        return relatorioDAO.getPorcentagemLimiteGastoSubcategoriaGroup(idUsuario, idSubcategoria);
    }
    
    public List<Relatorio> obterPorcentagemLimiteSubcategoriaByCategoria (long idUsuario, long idCategoria){
        return relatorioDAO.getPorcentagemLimiteGastoSubcategoriaByCategoriaGroup(idUsuario, idCategoria);
    }
    
    public List<Relatorio> getPorcentagemCategoriaDespesa(long idUsuario){
        return relatorioDAO.getPorcentagemCategoriaDespesa(idUsuario);
    }
    
    public List<Relatorio> getAnaliseMensalGastos(long idUsuario){
        return relatorioDAO.getAnaliseMensalGastos(idUsuario);
    }
    
    public List<Relatorio> getSaldoSubcategoriasByConta (long idUsuario, long idConta, int mes){
        return relatorioDAO.getSaldoSubcategoriasByConta(idUsuario, idConta, mes);
    }
    
    public BigDecimal getPorcentagem(BigDecimal valorLimite, long idUsuario){
         return relatorioDAO.getPorcentagem(valorLimite, idUsuario);
    }
    
    public Relatorio getSituacaoGeral(long idUsuario){
        return relatorioDAO.getSituacaoGeral(idUsuario);
    }
    
    public BigDecimal getValorParcela(BigDecimal valorTotal, int qtde){
        return relatorioDAO.getValorParcela(valorTotal, qtde);
    }
    
    public BigDecimal getValorParcelaAlterada(String tipo, long id, BigDecimal valorTotal, int qtdeParcelas){
        return relatorioDAO.getValorParcelaAlterada(tipo, id, valorTotal, qtdeParcelas);
    }
}
