/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import model.CategoriaDespesa;
import model.LimiteCategoriaDespesa;
import model.LimiteSubcategoria;
import model.Subcategoria;
import model.Usuario;
import util.JPAUtil;


public class LimiteDAO {
    private final UsuarioDAO usuarioDao = new UsuarioDAO();
    
    public LimiteSubcategoria inserirLimiteSubcategoria(LimiteSubcategoria limiteSubcategoria){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            Usuario usuario = em.find(Usuario.class, limiteSubcategoria.getUsuario().getId());
            Subcategoria subcategoria = em.find(Subcategoria.class, limiteSubcategoria.getSubcategoria().getId());
            
            em.getTransaction().begin();
            limiteSubcategoria.setUsuario(usuario);
            limiteSubcategoria.setSubcategoria(subcategoria);
            em.persist(limiteSubcategoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("INSERT: " + e.getMessage());
            e.printStackTrace();
        }finally {
            em.close();
        }

        return limiteSubcategoria;
    }
    
    public LimiteCategoriaDespesa inserirLimiteCategoriaDespesa(LimiteCategoriaDespesa limiteCategoria){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            Usuario usuario = em.find(Usuario.class, limiteCategoria.getUsuario().getId());
            CategoriaDespesa subcategoria = em.find(CategoriaDespesa.class, limiteCategoria.getCategoria().getId());
            
            em.getTransaction().begin();
            limiteCategoria.setUsuario(usuario);
            limiteCategoria.setCategoria(subcategoria);
            em.persist(limiteCategoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("INSERT: " + e.getMessage());
            e.printStackTrace();
        }finally {
            em.close();
        }

        return limiteCategoria;
    }
    
    public void inserirValorPadrao(Usuario usuario){
        
        EntityManager em = JPAUtil.getEntityManager();
        BigDecimal porcentagem;
        Usuario usuarioBanco;
        CategoriaDespesa categoria;
        LimiteCategoriaDespesa limiteCategoria;
 
        try{
            for (int i = 1; i <= 3; i++){

                usuarioBanco = em.find(Usuario.class, usuario.getId());
                categoria = em.find(CategoriaDespesa.class,(long) i);
                
                switch(i){
                    case 1:
                        porcentagem = new BigDecimal("35");
                        break;
                    case 2:
                        porcentagem = new BigDecimal("50");
                        break;
                    case 3:
                        porcentagem = new BigDecimal("15");
                        break;
                    default:
                        porcentagem = null;
                }
                
                limiteCategoria = new LimiteCategoriaDespesa();
                limiteCategoria.setCategoria(categoria);
                limiteCategoria.setUsuario(usuario);
                limiteCategoria.setPorcentagem(porcentagem);
                
                em.getTransaction().begin();
                em.persist(limiteCategoria);
                em.getTransaction().commit();
            }
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("INSERT: " + e.getMessage());
            e.printStackTrace();
        }finally {
            em.close();
        }
    }

    public LimiteSubcategoria alterarLimiteSubcategoria(LimiteSubcategoria limiteSubcategoria){

        EntityManager em = JPAUtil.getEntityManager();
        limiteSubcategoria.setUsuario(usuarioDao.getUsuarioById(limiteSubcategoria.getUsuario().getId()));
        try{
            em.getTransaction().begin();
            em.merge(limiteSubcategoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());
            
        }finally {
            em.close();
        }
        return limiteSubcategoria;
    }
    
    public LimiteCategoriaDespesa alterarLimiteCategoriaDespesa(LimiteCategoriaDespesa limiteCategoria){

        EntityManager em = JPAUtil.getEntityManager();
        limiteCategoria.setUsuario(usuarioDao.getUsuarioById(limiteCategoria.getUsuario().getId()));

        try{
            em.getTransaction().begin();
            em.merge(limiteCategoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());
            
        }finally {
            em.close();
        }
        return limiteCategoria;
    }

    /*Retorna todos os registros por usuário*/
    public List<LimiteSubcategoria> getLimiteSubcategoriaByUsuario(long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        
        TypedQuery<LimiteSubcategoria> query = em.
                createQuery("SELECT l FROM LimiteSubcategoria l WHERE l.usuario.id = :id", 
                LimiteSubcategoria.class);
        return query.setParameter("id", idUsuario).getResultList();
    }
    
    /*Retorna todos os registros por usuário*/
    public List<LimiteCategoriaDespesa> getLimiteCategoriaDespesaByUsuario(long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        
        TypedQuery<LimiteCategoriaDespesa> query = em.
                createQuery("SELECT l FROM LimiteCategoriaDespesa l WHERE l.usuario.id = :id", 
                LimiteCategoriaDespesa.class);
        return query.setParameter("id", idUsuario).getResultList();
    }

    public boolean removerLimiteSubcategoria(long idLimite){

        EntityManager em = JPAUtil.getEntityManager();

        try{

            em.getTransaction().begin();
            em.remove(em.getReference(LimiteSubcategoria.class,idLimite));
            em.getTransaction().commit();
            return true;

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE: " + e.getMessage());
            return false;

        }finally {
            em.close();
        }
    }
      
    public boolean removerLimiteCategoriaDespesa(long idLimite){

        EntityManager em = JPAUtil.getEntityManager();

        try{

            em.getTransaction().begin();
            em.remove(em.getReference(LimiteCategoriaDespesa.class,idLimite));
            em.getTransaction().commit();
            return true;

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE: " + e.getMessage());
            return false;

        }finally {
            em.close();
        }
    }
     
    public boolean removerLimiteSubcategoriaByUsuario(long idUsuario){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
                
                em.createQuery("delete from LimiteSubcategoria l where l.usuario.id=:id")
                        .setParameter("id", idUsuario)
                        .executeUpdate();
                
            em.getTransaction().commit();
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE LimiteCategoria BY Usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            em.close();
        }
        
        return true;
    }
      
    public boolean removerLimiteCategoriaDespesaByUsuario(long idUsuario){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
                em.createQuery("delete from LimiteCategoriaDespesa l where l.usuario.id=:id")
                        .setParameter("id", idUsuario)
                        .executeUpdate();
                
            em.getTransaction().commit();
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE LimiteCategoriaDespesa BY Usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            em.close();
        }
        
        return true;
    }

}
