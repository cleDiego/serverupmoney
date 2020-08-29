/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import dao.LimiteDAO;
import java.util.List;
import model.LimiteCategoriaDespesa;
import model.LimiteSubcategoria;
import model.Usuario;

public class LimiteFacade {
    
    private LimiteDAO limiteDAO;
    
    public LimiteFacade(){
        limiteDAO = new LimiteDAO();
    }
    
    public LimiteSubcategoria inserirRemanejarSubcategoria(LimiteSubcategoria remanejarSubcategoria) {
        return limiteDAO.inserirLimiteSubcategoria(remanejarSubcategoria);
    }
    
    public LimiteSubcategoria alterarRemanejarSubcategoria(LimiteSubcategoria remanejarSubcategoria) {
        return limiteDAO.alterarLimiteSubcategoria(remanejarSubcategoria);
    }
    
    public boolean removerRemanejarSubcategoria(long id) {
        return limiteDAO.removerLimiteSubcategoria(id);
    }
    
    public boolean removerRemanejarCategoriaDespesa(long id) {
        return limiteDAO.removerLimiteCategoriaDespesa(id);
    }
    
    public LimiteCategoriaDespesa inserirRemanejarCategoriaDespesa(LimiteCategoriaDespesa remanejarCategoriaDespesa) {
        return limiteDAO.inserirLimiteCategoriaDespesa(remanejarCategoriaDespesa);
    }
    
    public LimiteCategoriaDespesa alterarRemanejarCategoriaDespesa(LimiteCategoriaDespesa remanejarCategoriaDespesa) {
        return limiteDAO.alterarLimiteCategoriaDespesa(remanejarCategoriaDespesa);
    }
    
    public List<LimiteSubcategoria> getRemanejarSubcategoriaByUsuario(long idUsuario){
        return limiteDAO.getLimiteSubcategoriaByUsuario(idUsuario);
    }
    
    public List<LimiteCategoriaDespesa> getRemanejarCategoriaDespesaByUsuario(long idUsuario){
        return limiteDAO.getLimiteCategoriaDespesaByUsuario(idUsuario);
    }
    
    public boolean removerRemanejarSubcategoriaByUsuario(long idUsuario) {
        return limiteDAO.removerLimiteSubcategoriaByUsuario(idUsuario);
    }
    
    public boolean removerRemanejarCategoriaDespesaByUsuario(long idUsuario) {
        return limiteDAO.removerLimiteCategoriaDespesaByUsuario(idUsuario);
    }
    
    public void inserirValorPadrao(Usuario usuario){
        limiteDAO.inserirValorPadrao(usuario);
    }
}
