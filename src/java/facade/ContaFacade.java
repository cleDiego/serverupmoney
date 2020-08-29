/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import dao.ContaDAO;
import java.math.BigDecimal;
import java.util.List;
import model.Conta;
import model.Despesa;
import model.Receita;
import model.TipoConta;
import model.Usuario;


public class ContaFacade {
    
    private ContaDAO contaDAO;
    private LancamentoFacade lancamentoFacade;

    public ContaFacade() {
        this.contaDAO = new ContaDAO();
        this.lancamentoFacade = new LancamentoFacade();
    }
    
    public Conta inserirNovaConta(Conta conta){
        return contaDAO.inserirConta(conta);
    }
    
    public Conta alterarConta(Conta conta){
        return contaDAO.alterarConta(conta);
    }
    
    public boolean removerConta(long id){
        
        List<Receita> receitas = lancamentoFacade.getReceitasByConta(id);
        List<Despesa> despesas = lancamentoFacade.getDespesaByConta(id);
        
        if(receitas != null){
            lancamentoFacade.removerReceitasByConta(id);
        }
        if(despesas != null){
            lancamentoFacade.removerDespesasByConta(id);
        }
        
        return contaDAO.removerConta(id);
    }
    
    public List<Conta> getContasByUsuario(Usuario usuario){
        return contaDAO.getContasByUsuario(usuario);
    }
    
    public Conta getContaById(long id){
        return contaDAO.getContaById(id);
    }
    
    public List<TipoConta> getTipoContas(){
        return contaDAO.getTipoContas();
    }
    
    public TipoConta getTipoConta(long id){
        return contaDAO.getTipoContaById(id);
    }
    
    public BigDecimal getSaldoContaMensal (long id){
        return contaDAO.obterSaldoContaByMesAtual(id);
    }
    
    public BigDecimal getSaldoConta(long idConta){
        return contaDAO.obterSaldo(idConta);
    }
    
    public BigDecimal getSaldoTotal(long idUsuario){
        return contaDAO.obterSaldoByUsuario(idUsuario);
    }
    
}
