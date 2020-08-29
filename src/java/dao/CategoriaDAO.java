
package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import model.CategoriaDespesa;
import model.CategoriaReceita;
import model.Subcategoria;
import model.Usuario;
import util.JPAUtil;


public class CategoriaDAO {
    
    public CategoriaReceita inserirCategoriaReceita(CategoriaReceita categoria){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            Usuario usuario = em.find(Usuario.class, categoria.getUsuario().getId());
            
            em.getTransaction().begin();
            //usuario.getCategoriasReceita().add(categoria);
            categoria.setUsuario(usuario);
            em.persist(categoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("INSERT: " + e.getMessage());
        }finally {
            em.close();
        }

        return categoria;
    }

    public CategoriaReceita getCategoriaReceitaById(long id){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        CategoriaReceita categoriaReceita = new CategoriaReceita();

        try{
            
            categoriaReceita = em.find(CategoriaReceita.class, id);
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("FIND_ID: " + e.getMessage());
        }finally {
            em.close();
        }
        
        return categoriaReceita;
    }

    public boolean removerCategoriaReceita(long id){

        EntityManager em = JPAUtil.getEntityManager();       

        try{
           
            em.getTransaction().begin();
            em.remove(em.getReference(CategoriaReceita.class,id));
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("REMOVE: " + e.getMessage());

            return  false;
        }finally {
            
            em.close();
        }

        return true;
    }
    
    public CategoriaReceita alterarCategoriaReceita(CategoriaReceita categoria){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.merge(categoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());

        }finally {
            em.close();
        }
        return categoria;
    }
    
    public List<CategoriaReceita> getCategoriasByUsuario(long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        
        TypedQuery<CategoriaReceita> query = em.createQuery("SELECT c FROM CategoriaReceita c WHERE c.usuario.id = :id or c.usuario.id is null", 
                CategoriaReceita.class);
        return query.setParameter("id", idUsuario).getResultList();
    }
    
    public List<CategoriaReceita> getCategoriaDoUsuario(long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        List<CategoriaReceita> categorias = null;
        try{
            TypedQuery<CategoriaReceita> query = em
                    .createQuery("SELECT c FROM CategoriaReceita c "
                            + "WHERE c.usuario.id = :id", 
                CategoriaReceita.class);
        categorias = query.setParameter("id", idUsuario).getResultList();
        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            em.close();
        }
        
        return categorias;
    }
    
    /*VERIFICAR SE FAZ SENTIDO ESSE MÉTODO*/
    public List<CategoriaDespesa> getCategoriasDespesa(){
        EntityManager em = JPAUtil.getEntityManager();
        List<CategoriaDespesa> categorias = new ArrayList<>();
        
        try{
            
            TypedQuery<CategoriaDespesa> query = 
                em.createQuery("SELECT c FROM CategoriaDespesa c", 
                CategoriaDespesa.class);
            categorias = query.getResultList();
            
        }catch(Exception e){
            
            System.out.println("BUSCA CATEGORIAS DE DESPESA: " + e.getMessage());
            
        }finally{
            
            em.close();
            
        }

        return categorias;
    }
    
    public CategoriaDespesa alterarCategoriaDespesa(CategoriaDespesa categoria){
          EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.merge(categoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE CATEGORIA DESPESA: " + e.getMessage());

        }finally {
            em.close();
        }
        return categoria;
    }
    
    public CategoriaDespesa getCategoriaDespesaById(long id){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        CategoriaDespesa categoriaDespesa = new CategoriaDespesa();

        try{
            
            categoriaDespesa = em.find(CategoriaDespesa.class, id);
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("FIND_ID: " + e.getMessage());
        }finally {
            em.close();
        }
        
        return categoriaDespesa;
    }
    
    public Subcategoria inserirSubcategoria(Subcategoria subcategoria){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            CategoriaDespesa categoriaDespesa = em.find(CategoriaDespesa.class, subcategoria.getCategoriaDespesa().getId());
            Usuario usuario = em.find(Usuario.class, subcategoria.getUsuario().getId());
            
            em.getTransaction().begin();
            subcategoria.setCategoriaDespesa(categoriaDespesa);
            subcategoria.setUsuario(usuario);
            em.persist(subcategoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("INSERT: " + e.getMessage());
        }finally {
            em.close();
        }

        return subcategoria;
    }
    
    public List<Subcategoria> getSubcategoriaByUsuario(long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        List<Subcategoria> subcategorias = null;
        
        try{
            TypedQuery<Subcategoria> query = 
                    em.createQuery("SELECT c FROM Subcategoria c "
                            + "WHERE c.usuario.id = :idUsuario or c.usuario.id is null", 
                Subcategoria.class);
            subcategorias = query.setParameter("idUsuario", idUsuario).getResultList();
            System.out.println(subcategorias.get(0).getDescSubcategoria());
        }catch(Exception e){
            System.out.println("GETSUBCATEGORIABYUSUARIO: " + e.getMessage());
            e.printStackTrace();
            
        }finally{
            em.close();
        }
        
        return subcategorias;
    }
    
    public List<Subcategoria> getSubcategoriasDoUsuario(long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        List<Subcategoria> subcategorias = null;
        
        try{
            TypedQuery<Subcategoria> query = 
                    em.createQuery("SELECT c FROM Subcategoria c "
                            + "WHERE c.usuario.id = :id", 
                Subcategoria.class);
            subcategorias = query.setParameter("id", idUsuario).getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            em.close();
        }
        
        return subcategorias;
    }
    
    public Subcategoria alterarSubcategoria(Subcategoria subcategoria){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.merge(subcategoria);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());

        }finally {
            em.close();
        }
        return subcategoria;
    }
    
    public boolean removerSubcategoria(long id){

        EntityManager em = JPAUtil.getEntityManager(); 
        
        try{
           
            em.getTransaction().begin();
            em.remove(em.getReference(Subcategoria.class,id));
            em.getTransaction().commit();
            
           
            
        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("REMOVE: " + e.getMessage());

            return  false;
        }finally {
            
            em.close();
        }

        return true;
    }
    
    public List<Subcategoria> getSubcategoriaByCategoriaDespesa(long idCategoriaDespesa, long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        List<Subcategoria> subcategorias = new ArrayList<>();
        
        try{
            TypedQuery<Subcategoria> query = 
                    em.createQuery("SELECT c FROM Subcategoria c WHERE c.categoriaDespesa.id = :id "
                            + "and (c.usuario.id = :idUsuario or c.usuario.id = null)", 
                Subcategoria.class);
            subcategorias = query
                                .setParameter("id", idCategoriaDespesa)
                                .setParameter("idUsuario", idUsuario)
                                .getResultList();
        }catch(Exception e){
            System.out.println("GETSUBCATEGORIABYCATEGORIADESPESA: " + e.getMessage());
            
        }finally{
            em.close();
        }
        
        return subcategorias;
    }
    
    public Subcategoria getSubcategoriaById(long id){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        Subcategoria subcategoria = new Subcategoria();

        try{
            
            subcategoria = em.find(Subcategoria.class, id);
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("FIND_ID: " + e.getMessage());
        }finally {
            em.close();
        }
        
        return subcategoria;
    }
 
}
