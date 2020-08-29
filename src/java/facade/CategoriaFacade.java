/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import dao.CategoriaDAO;
import java.util.List;
import model.CategoriaDespesa;
import model.CategoriaReceita;
import model.Despesa;
import model.Receita;
import model.Subcategoria;


public class CategoriaFacade {
  
    private CategoriaDAO categoriaDAO;
    private LancamentoFacade lancamentoFacade;

    public CategoriaFacade() {
        this.categoriaDAO = new CategoriaDAO();
        this.lancamentoFacade = new LancamentoFacade();
    }
    
    public List<CategoriaReceita> getCategoriasReceitasByUsuario(long id){
        return categoriaDAO.getCategoriasByUsuario(id);
    }
    
    public List<CategoriaReceita> getCategoriaReceitaDoUsuario(long id){
        return categoriaDAO.getCategoriaDoUsuario(id);
    }
    
    public CategoriaReceita getCategoriasReceitasById(long id){
        return categoriaDAO.getCategoriaReceitaById(id);
    }
       
    public CategoriaReceita inserirCategoriaReceita(CategoriaReceita categoria){
        return categoriaDAO.inserirCategoriaReceita(categoria);
    }
    
    public boolean removeCategoriaReceita(long id){
        
        List<Receita> receitas = lancamentoFacade.getReceitasByCategoria(id);
        
        if(receitas != null) {
            lancamentoFacade.removerReceitasByCategoria(id);
        }
        return categoriaDAO.removerCategoriaReceita(id);
    }
    
    public CategoriaReceita alterarCategoriaReceita(CategoriaReceita categoria){
        return categoriaDAO.alterarCategoriaReceita(categoria);
    }
    
    public List<CategoriaDespesa> getCategoriasDespesa(){
       return categoriaDAO.getCategoriasDespesa();
    }
    
    public CategoriaDespesa alterarCategoriaDespesa(CategoriaDespesa categoria){
        return categoriaDAO.alterarCategoriaDespesa(categoria);
    }
    
    public CategoriaDespesa getCategoriaDespesaById(long id){
        return categoriaDAO.getCategoriaDespesaById(id);
    }
    
    public Subcategoria inserirSubcategoria(Subcategoria subcategoria){
        return categoriaDAO.inserirSubcategoria(subcategoria);
    }
    
    public List<Subcategoria> getSubcategoriasByUsuario(long idUsuario){
        return categoriaDAO.getSubcategoriaByUsuario(idUsuario);
    }
    
    public List<Subcategoria> getSubcategoriasDoUsuario(long id){
        return categoriaDAO.getSubcategoriasDoUsuario(id);
    }
    
    public Subcategoria alterarSubcategoria(Subcategoria subcategoria){
        return categoriaDAO.alterarSubcategoria(subcategoria);
    }
    
    public boolean removerSubcategoria(long id){
        
        List<Despesa> despesas = lancamentoFacade.getDespesaBySubCategoria(id);
        
        if (despesas != null){
            lancamentoFacade.removerDespesasByCategoria(id);
        }
        
        return categoriaDAO.removerSubcategoria(id);
    }
    
    public List<Subcategoria> getSubcategoriasByCategoriaDespesa(long idCategoriaDespesa, long idUsuario){
        return categoriaDAO.getSubcategoriaByCategoriaDespesa(idCategoriaDespesa, idUsuario);
    }
    
    public Subcategoria getSubcategoriaById(long id){
        return categoriaDAO.getSubcategoriaById(id);
    }
   
}
