/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import facade.LancamentoFacade;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.CategoriaReceita;
import model.Conta;
import model.Despesa;
import model.Receita;
import model.Subcategoria;
import model.TipoConta;
import model.Usuario;
import util.JPAUtil;

/**
 *
 * @author brunajeniferf
 */
public class ContaDAO {

       
    public Conta inserirConta(Conta conta){
        
        EntityManager em1 = JPAUtil.getEntityManager();
        EntityManager em2 = JPAUtil.getEntityManager();
        
        
        try{
            em1.getTransaction().begin();
            
            Usuario usuario = em1.find(Usuario.class, conta.getUsuario().getId());
            TipoConta tipoConta = em1.find(TipoConta.class, conta.getTipoConta().getId());
           
            conta.setUsuario(usuario);
            conta.setTipoConta(tipoConta);
            
            em1.persist(conta);
            em1.getTransaction().commit(); 
            
            LancamentoFacade lancFacade = new LancamentoFacade();
            
            
            
            if (conta.getValorInicial().compareTo(BigDecimal.ZERO)!= 0 
                    && conta.getValorInicial() != null){
                if (tipoConta.getId() != 4){
                    Receita receita = new Receita();
                    CategoriaReceita categoria = em2.find(CategoriaReceita.class, 5L); 
                    receita.setCategoria(categoria);
                    receita.setConta(conta);
                    receita.setDescReceita("Valor Inicial Conta " + conta.getDescConta());
                    receita.setDtLancamento(LocalDate.now());
                   // receita.setTipoRepeticao(null);
                    receita.setRepeticao(false);
                    receita.setValorTotalReceita(conta.getValorInicial());
                    lancFacade.inserirReceita(receita);
                }else{
                    Despesa despesa = new Despesa();
                    Subcategoria subcategoria = em2.find(Subcategoria.class, 13L);
                    despesa.setCategoria(subcategoria);
                    despesa.setConta(conta);
                    despesa.setDtLancamento(LocalDate.now());
                    despesa.setRepeticao(false);
                    despesa.setDescDespesa("Valor Inicial Conta " + conta.getDescConta());
                    despesa.setValorTotalDespesa(conta.getValorInicial());
                    lancFacade.inserirDespesa(despesa);

                }
            }
            
            
        }catch(Exception e){
            em1.getTransaction().rollback();
            System.out.println("INSERT: Exceção em UsuarioDAO.inserirUsuario: " + e.getMessage());
            
        }finally{
           em1.close();
           em2.close();
        }
        
        return conta;
    }
    
    public Conta getContaById(long id){
        
        EntityManager em = JPAUtil.getEntityManager();
        Conta conta = new Conta();
        
         try{
            conta = em.find(Conta.class, id);
        }catch(Exception e){
            System.out.println("GETBYID: " + e.getMessage());
            
        }finally{
           em.close();
        }
         
         return conta;
        
    }
    
    public TipoConta getTipoContaById(long id){
   
        EntityManager em = JPAUtil.getEntityManager();
        TipoConta tipoConta = new TipoConta();
        
         try{
            tipoConta = em.find(TipoConta.class, id);
            
        }catch(Exception e){
            System.out.println("GETBYID: " + e.getMessage());
            
        }finally{
           em.close();
        }
         
         return tipoConta;
    }
     
    public List<Conta> getContasByUsuario (Usuario usuario){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        List<Conta> contas = new ArrayList<>();
        
        try{
            TypedQuery<Conta> query = em.createQuery(
                "SELECT c FROM Conta c WHERE c.usuario.id = :id",Conta.class);
            contas = query.setParameter("id", usuario.getId()).getResultList();
        }catch(Exception e){
            
            System.out.println("BUSCA CONTAS: " + e.getMessage());
            
        }finally{
            
            em.close();
            
        }
        
        return contas;
    }
    
    public List<TipoConta> getTipoContas(){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        List<TipoConta> tipoContas = new ArrayList<>();
        
        try{
            
            TypedQuery<TipoConta> query = em.createQuery(
                "SELECT tc FROM TipoConta tc",TipoConta.class);
            tipoContas = query.getResultList();
            
        }catch(Exception e){
            
            System.out.println("BUSCA TIPOCONTAS: " + e.getMessage());
            
        }finally{
            
            em.close();
            
        }
        
        return tipoContas;
    }
    
    public Conta alterarConta(Conta conta){
        EntityManager em = JPAUtil.getEntityManager();

        try{
            
            em.getTransaction().begin();
            em.merge(conta);
            em.getTransaction().commit();
            
        }catch (Exception e){
            
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());

        }finally {
            em.close();
        }
        return conta;
    }
    
    public boolean removerConta(long id) {
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.remove(em.getReference(Conta.class, id));
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
    
    public BigDecimal obterSaldoContaByMesAtual(long idConta){
        
        /*A partir da data atual, obtém o mês corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(dataAtual);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH)+1; /*Soma-se um porque em Java Calendar Janeiro é 0*/
        int ano = calendar.get(Calendar.YEAR);
        
        System.out.println("DIA: " + dia + " - MES: " + mes + " - ANO: " + ano);
        
        BigDecimal saldoReceita = null;
        BigDecimal saldoDespesa = null;

        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createQuery(
                    "select sum(r.valorParcela) from  Receita r " +
                            "where r.conta.id = :idConta "
                           // + "and DAY(r.dtLancamento) <= :dia "
                            + "and MONTH(r.dtLancamento) = :mes "
                            + "and YEAR(r.dtLancamento) = :ano");
            saldoReceita = (BigDecimal) query.setParameter("idConta", idConta)
                   // .setParameter("dia", dia)
                    .setParameter("mes", mes)
                    .setParameter("ano", ano)
                    .getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
               em.close();
        }
        
        if (saldoReceita == null){
            saldoReceita = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo Receita: " + saldoReceita);
        
        EntityManager entityManager = JPAUtil.getEntityManager();

        try{
            Query query = entityManager.createQuery(
                    "select sum(d.valorParcela) from Despesa d " +
                            "where d.conta.id = :idConta "
                            + "and MONTH(d.dtLancamento) = :mes "
                            + "and YEAR(d.dtLancamento) = :ano"
            );

            saldoDespesa = (BigDecimal) query
                    .setParameter("idConta", idConta)
                    .setParameter("mes", mes)
                    .setParameter("ano", ano)
                    .getSingleResult();
            
        }catch (Exception e){
            e.printStackTrace();
            
        }finally {
            entityManager.close();
        }
        
         if (saldoDespesa == null){
            saldoDespesa = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo Despesa: " + saldoDespesa);
        
        return saldoReceita.subtract(saldoDespesa);
    }
    
    public BigDecimal obterSaldo(long idConta){
        
        /*Obtém a data atual*/
        //java.util.Date data1 = new java.util.Date();
        //java.sql.Date data = new java.sql.Date(data1.getTime());
        LocalDate data = LocalDate.now();
         
        BigDecimal saldoReceita = null;
        BigDecimal saldoDespesa = null;

        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Query query = em.createQuery(
                    "select sum(r.valorParcela) from  Receita r " +
                            "where r.conta.id = :idConta "
                            + "and r.dtLancamento <= :data");
            saldoReceita = (BigDecimal) query
                    .setParameter("idConta", idConta)
                    .setParameter("data", data)
                    .getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
               em.close();
        }
        
        if (saldoReceita == null){
            saldoReceita = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo Receita: " + saldoReceita);
        
        EntityManager entityManager = JPAUtil.getEntityManager();

        try{
            Query query = entityManager.createQuery(
                    "select sum(d.valorParcela) from Despesa d " +
                            "where d.conta.id = :idConta "
                            + "and d.dtLancamento <= :data"
            );

            saldoDespesa = (BigDecimal) query
                    .setParameter("idConta", idConta)
                    .setParameter("data", data)
                    .getSingleResult();
            
        }catch (Exception e){
            e.printStackTrace();
            
        }finally {
            entityManager.close();
        }
        
         if (saldoDespesa == null){
            saldoDespesa = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo Despesa: " + saldoDespesa);
        
        return saldoReceita.subtract(saldoDespesa);
    }
    
    public BigDecimal obterSaldoByUsuario(long idUsuario){
        
        /*Obtém a data atual*/
        /*
        java.util.Date data1 = new java.util.Date();
        java.sql.Date data = new java.sql.Date(data1.getTime());*/
        LocalDate data = LocalDate.now();
         
        BigDecimal saldoReceita = null;
        BigDecimal saldoDespesa = null;

        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Query query = em.createQuery(
                    "select sum(r.valorParcela) from  Receita r " +
                            "where r.conta.usuario.id = :idUsuario "
                            + "and r.dtLancamento <= :data");
            saldoReceita = (BigDecimal) query
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("data", data)
                    .getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
               em.close();
        }
        
        if (saldoReceita == null){
            saldoReceita = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo Receita: " + saldoReceita);
        
        EntityManager entityManager = JPAUtil.getEntityManager();

        try{
            Query query = entityManager.createQuery(
                    "select sum(d.valorParcela) from Despesa d " +
                            "where d.conta.usuario.id = :idUsuario "
                            + "and d.dtLancamento <= :data"
            );

            saldoDespesa = (BigDecimal) query
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("data", data)
                    .getSingleResult();
            
        }catch (Exception e){
            e.printStackTrace();
            
        }finally {
            entityManager.close();
        }
        
         if (saldoDespesa == null){
            saldoDespesa = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo Despesa: " + saldoDespesa);
        
        return saldoReceita.subtract(saldoDespesa);
    }
}
