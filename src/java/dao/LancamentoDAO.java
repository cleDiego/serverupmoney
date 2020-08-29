/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.CategoriaReceita;
import model.Conta;
import model.Despesa;
import model.Receita;
import model.Subcategoria;

import model.TipoRepeticao;
import util.JPAUtil;
import ws.exception.BancoDadosException;



public class LancamentoDAO {
    
    public List<TipoRepeticao> getTiposRepeticao(){
         EntityManager em = JPAUtil.getEntityManager();
        
        TypedQuery<TipoRepeticao> query = em.createQuery("SELECT tr FROM TipoRepeticao tr", 
                TipoRepeticao.class);
        return query.getResultList();
    }
    
    public TipoRepeticao getTipoRepeticaoById(long id){
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<TipoRepeticao> query = em.createQuery("SELECT tr FROM TipoRepeticao tr where tr.id = :id", 
                TipoRepeticao.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
    
    public Receita inserirReceita(Receita receita){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        CategoriaReceita categoria = em.find(CategoriaReceita.class, receita.getCategoria().getId());
        Conta conta = em.find(Conta.class, receita.getConta().getId());
        
        receita.setCategoria(categoria);
        receita.setConta(conta);

        if (receita.isRepeticao()){

            receita.setNumParcela(1);
            TipoRepeticao tpRepeticao = em.find(TipoRepeticao.class, receita.getTipoRepeticao().getId());
            receita.setTipoRepeticao(tpRepeticao);
            this.inserirReceitaBancodeDados(receita);

            if (receita.getTipoRepeticao().getId() == 8) {
                receita.setValorTotalReceita(receita.getValorParcela());
            }else {
                receita.setValorParcela(calculaParcela(receita));
            }

            /*Seta o valor do id para o idRepeticao*/
            receita.setIdRepeticao(receita.getId());
            receita.setTipoRepeticao(getTipoRepeticaoById(receita.getTipoRepeticao().getId()));
            this.alterarReceita(receita);


            for (int i = 1; i < obterQtdeParcelas(receita); i++) {

                /*Obtém informações do objeto repetido exceto id*/
                Receita novaReceita = new Receita();
                novaReceita.setDescReceita(receita.getDescReceita());
                novaReceita.setIdRepeticao(receita.getId());
                novaReceita.setTipoRepeticao(receita.getTipoRepeticao());
                novaReceita.setRepeticao(receita.isRepeticao());
                novaReceita.setNumParcela(i+1);
                novaReceita.setQtdeParcelas(receita.getQtdeParcelas());
                novaReceita.setValorParcela(receita.getValorParcela());
                novaReceita.setValorTotalReceita(receita.getValorTotalReceita());
                novaReceita.setCategoria(receita.getCategoria());
                novaReceita.setConta(receita.getConta());

                if(novaReceita.getTipoRepeticao().getId() != 8) {
                    novaReceita.setValorParcela(calculaParcela(novaReceita));
                }

                novaReceita.setDtLancamento(obterData(receita, i));
                inserirReceitaBancodeDados(novaReceita);
            }
        }else{
            receita.setNumParcela(1);
            receita.setQtdeParcelas(1);
            receita.setIdRepeticao(0);
            receita.setValorParcela(receita.getValorTotalReceita());
            inserirReceitaBancodeDados(receita);
        }
        
        return this.getReceitaById(receita.getId());
    }
    
    public Despesa inserirDespesa(Despesa despesa) {

        EntityManager em = JPAUtil.getEntityManager();

        Subcategoria subcategoria = em.find(Subcategoria.class, despesa.getCategoria().getId());
        Conta conta = em.find(Conta.class, despesa.getConta().getId());
        
        despesa.setCategoria(subcategoria);
        despesa.setConta(conta);
        
        if (despesa.isRepeticao()){

            despesa.setNumParcela(1);
            TipoRepeticao tpRepeticao = em.find(TipoRepeticao.class, despesa.getTipoRepeticao().getId());
            despesa.setTipoRepeticao(tpRepeticao);
            this.inserirDespesaBancodeDados(despesa);

            if (despesa.getTipoRepeticao().getId() == 8) {
                despesa.setValorTotalDespesa(despesa.getValorParcela());
            } else {
                despesa.setValorParcela(calculaParcela(despesa));
            }

            /*Seta o valor do id para o idRepeticao*/
            despesa.setIdRepeticao(despesa.getId());
            despesa.setTipoRepeticao(getTipoRepeticaoById(despesa.getTipoRepeticao().getId()));
            this.alterarDespesa(despesa);


                for (int i = 1; i < obterQtdeParcelas(despesa); i++) {

                    /*Obtém informações do objeto repetido exceto id*/
                    Despesa novaDespesa = new Despesa();
                    novaDespesa.setDescDespesa(despesa.getDescDespesa());
                    novaDespesa.setIdRepeticao(despesa.getId());
                    novaDespesa.setTipoRepeticao(despesa.getTipoRepeticao());
                    novaDespesa.setRepeticao(despesa.isRepeticao());
                    novaDespesa.setNumParcela(i+1);
                    novaDespesa.setQtdeParcelas(despesa.getQtdeParcelas());
                    novaDespesa.setValorParcela(despesa.getValorParcela());
                    novaDespesa.setValorTotalDespesa(despesa.getValorTotalDespesa());
                    novaDespesa.setCategoria(despesa.getCategoria());
                    novaDespesa.setConta(despesa.getConta());

                    if(novaDespesa.getTipoRepeticao().getId() != 8) {
                        novaDespesa.setValorParcela(calculaParcela(novaDespesa));
                    }


                    novaDespesa.setDtLancamento(obterData(despesa, i));
                    inserirDespesaBancodeDados(novaDespesa);
                }
        }else{
            despesa.setNumParcela(1);
            despesa.setQtdeParcelas(1);
            despesa.setIdRepeticao(0);
            despesa.setValorParcela(despesa.getValorTotalDespesa());
            inserirDespesaBancodeDados(despesa);
        }

        return this.getDespesaById(despesa.getId());
    }
    
    public Receita alterarReceita(Receita receita){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        if(!receita.isRepeticao()) {
            receita.setNumParcela(1);
            receita.setQtdeParcelas(1);
            receita.setIdRepeticao(0);
            receita.setValorParcela(receita.getValorTotalReceita());
        }
        try{
            em.getTransaction().begin();
            em.merge(receita);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());
            return null;
        }finally {
            em.close();
        }
        return this.getReceitaById(receita.getId());
    }
    
    public Despesa alterarDespesa(Despesa despesa){

        EntityManager em = JPAUtil.getEntityManager();

        /*Garante que se a despesa que é a primeira parcela e a quantidade de repetições
            é repetições for 1, não tenha repetições
         */
        /*Se houver mais parcelas, estas são apagadas*/
        if (obterQtdeParcelas(despesa) == 1 && despesa.isRepeticao()){
            EntityManager em1 = JPAUtil.getEntityManager();

            try{

                em1.getTransaction().begin();
                em1.createQuery("delete from Despesa d where d.idRepeticao = :id and d.dtLancamento > :data")
                        .setParameter("id", despesa.getIdRepeticao())
                        .setParameter("data", despesa.getDtLancamento())
                        .executeUpdate();
                em1.getTransaction().commit();

            }catch (Exception e){
                em1.getTransaction().rollback();
                System.out.println("REMOVE: " + e.getMessage());

            }finally {
                em1.close();
            }

            /*Realiza mudanças nos atributos para que a despesa seja única*/
            despesa.setRepeticao(false);
            despesa.setNumParcela(1);
            despesa.setIdRepeticao(0);
            despesa.setValorParcela(despesa.getValorTotalDespesa());
            despesa.setTipoRepeticao(null);
        }
        
        if(!despesa.isRepeticao()) {
            despesa.setNumParcela(1);
            despesa.setQtdeParcelas(1);
            despesa.setIdRepeticao(0);
            despesa.setValorParcela(despesa.getValorTotalDespesa());
        }
        
        try{
            em.getTransaction().begin();
            em.merge(despesa);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: " + e.getMessage());

        }finally {
            em.close();
        }
        return this.getDespesaById(despesa.getId());
    }
    
    public Receita alterarReceitas(Receita receita){

        EntityManager entity = JPAUtil.getEntityManager();
        Receita auxReceita = null;

        try{
            /*Obtém a despesa não alterada*/
            auxReceita = entity.find(Receita.class, receita.getId());

        }catch (Exception e){
            entity.getTransaction().rollback();
            System.out.println("FIND: " + e.getMessage());

        }finally {
            entity.close();
        }
        
        if(auxReceita == null) {
            throw new BancoDadosException("Receita não encontrada");
        }
        
        if(receita.getValorParcela() != null) {
            if(receita.getValorParcela().compareTo(auxReceita.getValorParcela()) != 0){
                this.alterarValorParcela(receita,auxReceita);
            }
        }

        if(receita.isRepeticao() != auxReceita.isRepeticao()){
           this.alterarRepeticao(receita);
        }
        
        if(receita.getValorTotalReceita() == null) {
            if(receita.getTipoRepeticao().getId() == 8) {
                /* Receita Infinita */
                receita.setValorTotalReceita(receita.getValorParcela()); /*Setar o valor da parcela infinita */
            }
            
        }else {
            if(receita.getNumParcela() == 0) {
                receita.setNumParcela(1);
            }
            
            if(receita.getValorTotalReceita().compareTo(auxReceita.getValorTotalReceita()) != 0){
                this.alterarValorTotalReceita(receita, auxReceita);
            } 
        }
        
        if(!receita.getDtLancamento().toString().equalsIgnoreCase(auxReceita.getDtLancamento().toString())){
            this.alterarDtLancamento(receita, auxReceita);
        }
        
        if(receita.getTipoRepeticao() != null){
            if(!receita.getTipoRepeticao().equals(auxReceita.getTipoRepeticao())){
                this.alterarTipoRepeticao(receita, auxReceita);
            }
        }
        if(!receita.getCategoria().equals(auxReceita.getCategoria())){
            this.alterarCategoria(receita);
        }
        if(!receita.getConta().equals(auxReceita.getConta())){
            this.alterarConta(receita);
        }
        if(!receita.getDescReceita().equalsIgnoreCase(auxReceita.getDescReceita()) ){
            this.alterarDescricaoReceitas(receita);
        }
        
        if(receita.getQtdeParcelas() != auxReceita.getQtdeParcelas()){
            this.alterarNumeroRepeticoes(auxReceita, receita);
        }

        return this.getReceitaById(receita.getId());
    }
    
    public Despesa alterarDespesas(Despesa despesa){

        EntityManager entity = JPAUtil.getEntityManager();
        Despesa auxDespesa = null;

        try{
            /*Obtém a despesa não alterada*/
            auxDespesa = entity.find(Despesa.class, despesa.getId());

        }catch (Exception e){
            entity.getTransaction().rollback();
            System.out.println("FIND: " + e.getMessage());

        }finally {
            entity.close();
        }
        
        if(auxDespesa == null) {
            throw new BancoDadosException("Despesa não encontrada");
        }
        
        if(despesa.getValorParcela() != null) {
            if(despesa.getValorParcela().compareTo(auxDespesa.getValorParcela()) != 0){
                System.out.println("Alterando Valor Parcela de:"+auxDespesa.getValorParcela() + " Para:"+despesa.getValorParcela());
                this.alterarValorParcela(despesa,auxDespesa);
            }
        }
        
        if(despesa.isRepeticao() != auxDespesa.isRepeticao()){
            this.alterarRepeticao(despesa);
        }
        
        if(despesa.getValorTotalDespesa() == null) {
            if(despesa.getTipoRepeticao() != null) {
                if(despesa.getTipoRepeticao().getId() == 8) {
                    /* Despesa Infinita */
                    despesa.setValorTotalDespesa(despesa.getValorParcela()); /*Setar o valor da parcela infinita */
                }
            } 
        }else {
            if(despesa.getNumParcela() == 0) {
                despesa.setNumParcela(1);
            }
            
            if(despesa.getValorTotalDespesa().compareTo(auxDespesa.getValorTotalDespesa()) != 0){
                this.alterarValorTotalDespesa(despesa, auxDespesa);
            }
        }
        
        if(!despesa.getDtLancamento().toString().equalsIgnoreCase(auxDespesa.getDtLancamento().toString())){
            this.alterarDtLancamento(despesa, auxDespesa);
        }
        
        if(despesa.getTipoRepeticao() != null){
            if(!despesa.getTipoRepeticao().equals(auxDespesa.getTipoRepeticao())){
                this.alterarTipoRepeticao(despesa, auxDespesa);
            }
        }
        if(!despesa.getCategoria().equals(auxDespesa.getCategoria())){
            this.alterarSubcategoria(despesa);
        }
        if(!despesa.getConta().equals(auxDespesa.getConta())){
            this.alterarConta(despesa);
        }
        if(!despesa.getDescDespesa().equalsIgnoreCase(auxDespesa.getDescDespesa()) ){
            this.alterarDescricaoDespesas(despesa);
        }
        if(despesa.getQtdeParcelas() != auxDespesa.getQtdeParcelas()){
            this.alterarNumeroRepeticoes(auxDespesa, despesa);
        }

        return this.getDespesaById(despesa.getId());
    }
    
    public boolean removerReceita(long idReceita){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.remove(em.getReference(Receita.class, idReceita));
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
    
    public boolean removerDespesa(long id){

        EntityManager em = JPAUtil.getEntityManager();

        try{

            em.getTransaction().begin();
            em.remove(em.getReference(Despesa.class,id));
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
    
    public boolean removerReceitas(long idReceita){
        EntityManager em = JPAUtil.getEntityManager();
        Receita receita = this.getReceitaById(idReceita);
        try{
            em.getTransaction().begin();
            em.createQuery("delete from Receita  re "
                    + "where re.idRepeticao = :id and re.dtLancamento >= :data")
                    .setParameter("id", receita.getIdRepeticao())
                    .setParameter("data", receita.getDtLancamento())
                    .executeUpdate();
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
    
    public boolean removerDespesas(long id){
        
        EntityManager em = JPAUtil.getEntityManager();
        Despesa despesa;

        try{
            despesa = em.find(Despesa.class, id);
            em.getTransaction().begin();
            em.createQuery("delete from Despesa  d where d.idRepeticao = :id and d.dtLancamento >= :data")
                    .setParameter("id", despesa.getIdRepeticao())
                    .setParameter("data", despesa.getDtLancamento())
                    .executeUpdate();
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
    
    public List<Receita> getReceitasByConta(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Receita> receitas = new ArrayList<>();
        
        try{
            TypedQuery<Receita> query = em.createQuery(
                "SELECT r FROM Receita r WHERE r.conta.id = :id", Receita.class);
            receitas = query.setParameter("id", id).getResultList();
        }catch(Exception e){
            System.out.println("BUSCA RECEITAS: " + e.getMessage());
        }finally{
            em.close();
        }
        return receitas;
    }
    
    public List<Despesa> getDespesaByConta(long id){
        
        EntityManager em = JPAUtil.getEntityManager();
        List<Despesa> despesas = null;
        
        try{
            TypedQuery<Despesa> query = em.createQuery(
                "SELECT d FROM Despesa d WHERE d.conta.id = :id", Despesa.class);
            despesas = query.setParameter("id", id).getResultList();
            
        }catch(Exception e){
            System.out.println("BUSCA RECEITAS: " + e.getMessage());
            e.printStackTrace();
            
        }finally{
            em.close();
            
        }
        return despesas;
    }
    
    public List<Receita> getReceitaByCategoria(long idCategoriaReceita){
        
        EntityManager em = JPAUtil.getEntityManager();
        List<Receita> receitas = new ArrayList<>();
        
        try{
            
            TypedQuery<Receita> query = 
                em.createQuery("SELECT re FROM Receita re WHERE re.categoria.id = :id", 
                Receita.class);
            receitas = query.setParameter("id", idCategoriaReceita).getResultList();
        
        }catch(Exception e){
            System.out.println("BUSCA RECEITAS BY CATEGORIA: " + e.getMessage());
        }finally{
            em.close();
        }
        
        return receitas;
    }
    
    public List<Despesa> getDespesaBySubCategoria(long idSubcategoria){
        
        EntityManager em = JPAUtil.getEntityManager();
        List<Despesa> despesas = new ArrayList<>();
        
        try{
            
            TypedQuery<Despesa> query = 
                em.createQuery("SELECT de FROM Despesa de WHERE de.categoria.id = :id", 
                Despesa.class);
            despesas = query.setParameter("id", idSubcategoria).getResultList();
        
        }catch(Exception e){
            System.out.println("BUSCA DESPESAS BY CATEGORIA: " + e.getMessage());
        }finally{
            em.close();
        }
        
        return despesas;
    }
    
    public Receita getReceitaById(long idReceita){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        Receita receita = new Receita();

        try{
            
            receita = em.find(Receita.class, idReceita);
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("FIND_ID: " + e.getMessage());
        }finally {
            em.close();
        }
        
        return receita;
    }
    
    public Despesa getDespesaById(long idDespesa){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        Despesa despesa = null;

        try{
            
            despesa = em.find(Despesa.class, idDespesa);
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("FIND_ID: " + e.getMessage());
        }finally {
            em.close();
        }
        
        return despesa;
    }

    public List<Receita> getReceitasByUsuario(long idUsuario){
        
        EntityManager em = JPAUtil.getEntityManager();
        List<Receita> receitas = new ArrayList<>();
        try{
            
        TypedQuery<Receita> typedQuery = 
                    em.createQuery("SELECT re FROM Receita re JOIN re.conta co WHERE co.usuario.id = :id", 
                        Receita.class);
        receitas = typedQuery.setParameter("id", idUsuario).getResultList();
        
        }catch(Exception e){
            System.out.println("BUSCA RECEITAS: " + e.getMessage());
        }finally{
            em.close();
        }
        
        return receitas;
    } 
    
    public List<Despesa> getDespesaByUsuario(long idUsuario){
        
        EntityManager em = JPAUtil.getEntityManager();
        List<Despesa> despesas = null;
        try{
            
        TypedQuery<Despesa> typedQuery = 
                    em.createQuery("SELECT d FROM Despesa d JOIN d.conta co WHERE co.usuario.id = :id", 
                        Despesa.class);
        despesas = typedQuery.setParameter("id", idUsuario).getResultList();
        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            em.close();
        }
        
        return despesas;
    } 
    
    public List<Receita> getReceitasByMes(Integer mes, Integer ano, long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        List<Receita> receitas = new ArrayList<>();
        
        try{
            
        TypedQuery<Receita> typedQuery = 
                    em.createQuery("SELECT re FROM Receita re "
                            + "JOIN re.conta co "
                            + "WHERE MONTH(re.dtLancamento) = :mes "
                            + "and YEAR(re.dtLancamento) = :ano "
                            + "and co.usuario.id = :idUsuario", 
                        Receita.class);
        receitas = typedQuery.setParameter("mes", mes)
                .setParameter("ano",ano)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
        
        }catch(Exception e){
            System.out.println("BUSCA RECEITAS POR DATA: " + e.getMessage());
        }finally{
            em.close();
        }
        
        return receitas;
    }
    
    public List<Despesa> getDespesaByMes(Integer mes, Integer ano, long idUsuario){
        EntityManager em = JPAUtil.getEntityManager();
        List<Despesa> despesas = null;
        
        try{
            
        TypedQuery<Despesa> typedQuery = 
                    em.createQuery("SELECT d FROM Despesa d "
                            + "JOIN d.conta co "
                            + "WHERE MONTH(d.dtLancamento) = :mes "
                            + "and YEAR(d.dtLancamento) = :ano "
                            + "and co.usuario.id = :idUsuario", 
                        Despesa.class);
        despesas = typedQuery.setParameter("mes", mes)
                .setParameter("ano",ano)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            em.close();
        }
        
        return despesas;
    }
    
    public List<Receita> getReceitasContaByMes(long idConta, Integer mes){
        EntityManager em = JPAUtil.getEntityManager();
        List<Receita> receitas = new ArrayList<>();
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        
        try{
            
        TypedQuery<Receita> typedQuery = 
                    em.createQuery("SELECT re FROM Receita re "
                            + "JOIN re.conta co "
                            + "WHERE MONTH(re.dtLancamento) = :mes "
                            + "and YEAR(re.dtLancamento) = :ano "
                            + "and co.id = :idConta", 
                        Receita.class);
        receitas = typedQuery.setParameter("mes", mes)
                .setParameter("ano",ano)
                .setParameter("idConta", idConta)
                .getResultList();
        
        }catch(Exception e){
            System.out.println("BUSCA RECEITAS POR DATA: " + e.getMessage());
        }finally{
            em.close();
        }
        
        return receitas;
    }
    
    public List<Despesa> getDespesaContaByMes(long idConta, Integer mes){
        EntityManager em = JPAUtil.getEntityManager();
        List<Despesa> despesas = null;
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        
        try{
            
        TypedQuery<Despesa> typedQuery = 
                    em.createQuery("SELECT d FROM Despesa d "
                            + "JOIN d.conta co "
                            + "WHERE MONTH(d.dtLancamento) = :mes "
                            + "and YEAR(d.dtLancamento) = :ano "
                            + "and co.id = :idConta", 
                        Despesa.class);
        despesas = typedQuery.setParameter("mes", mes)
                .setParameter("ano",ano)
                .setParameter("idConta", idConta)
                .getResultList();
        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            em.close();
        }
        
        return despesas;
    }
    
    public List<Despesa> getDespesaSubcategoriaContaByMes(long idSubcategoria, long idConta, Integer mes){
        
        EntityManager em = JPAUtil.getEntityManager();
        List<Despesa> despesas = null;
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        
        try{
            
        TypedQuery<Despesa> typedQuery = 
                    em.createQuery("SELECT d FROM Despesa d "
                            + "JOIN d.conta co "
                            + "WHERE MONTH(d.dtLancamento) = :mes "
                            + "and YEAR(d.dtLancamento) = :ano "
                            + "and co.id = :idConta "
                            + "and d.categoria.id =: idSubcategoria", 
                        Despesa.class);
        despesas = typedQuery.setParameter("mes", mes)
                .setParameter("ano",ano)
                .setParameter("idConta", idConta)
                .setParameter("idSubcategoria", idSubcategoria)
                .getResultList();
        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            em.close();
        }
        
        return despesas;
    }
    
    public boolean removerReceitasByCategoria(long idCategoria){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
                
                em.createQuery("delete from Receita re where re.categoria.id=:id")
                        .setParameter("id", idCategoria)
                        .executeUpdate();
                
            em.getTransaction().commit();
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE RECEITA BY CATEGORIA: " + e.getMessage());
            return false;
        }finally {
            em.close();
        }
        
        return true;
    }
    
    public boolean removerDespesasByCategoria(long idCategoria){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
                
                em.createQuery("delete from Despesa d where d.categoria.id=:id")
                        .setParameter("id", idCategoria)
                        .executeUpdate();
                
            em.getTransaction().commit();
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE DESPESA BY CATEGORIA: " + e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            em.close();
        }
        
        return true;
    }
    
    public boolean removerReceitasByConta(long idConta){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
                
                em.createQuery("delete from Receita re where re.conta.id=:id")
                        .setParameter("id", idConta)
                        .executeUpdate();
                
            em.getTransaction().commit();
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE RECEITA BY CONTA: " + e.getMessage());
            return false;
        }finally {
            em.close();
        }
        
        return true;
    }
    
    public boolean removerDespesasByConta(long idConta){
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
                
                em.createQuery("delete from Despesa d where d.conta.id=:id")
                        .setParameter("id", idConta)
                        .executeUpdate();
                
            em.getTransaction().commit();
            
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("REMOVE RECEITA BY CONTA: " + e.getMessage());
            return false;
        }finally {
            em.close();
        }
        
        return true;
    }  
    
    public BigDecimal getTotalGastoMes(long idUsuario){
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        BigDecimal totalGasto = null;
        
         EntityManager em = JPAUtil.getEntityManager();
        
            try {
                Query query = em.createQuery(
                        "select sum(de.valorParcela) from Despesa de " +
                                "where de.conta.usuario.id = :idUsuario "
                                + "and MONTH(de.dtLancamento) = :mes "
                                + "and YEAR(de.dtLancamento) = :ano "
                );
                totalGasto =  (BigDecimal) query
                        .setParameter("idUsuario", idUsuario)
                        .setParameter("mes", mes)
                        .setParameter("ano", ano)
                        .getSingleResult()
                        ;

            } catch (NoResultException e) {
                //e.printStackTrace();
                //saldoReceita = null;
            }catch(Exception e){
                e.printStackTrace();
            } 
            finally {
                   em.close();
            }
            
            if(totalGasto == null){
                totalGasto = BigDecimal.ZERO;
            }
            
            return totalGasto;
    }
    
    public BigDecimal getTotalGastoMes(long idUsuario, int mes){
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        //int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        BigDecimal totalGasto = null;
        
         EntityManager em = JPAUtil.getEntityManager();
        
            try {
                Query query = em.createQuery(
                        "select sum(de.valorParcela) from Despesa de " +
                                "where de.conta.usuario.id = :idUsuario "
                                + "and MONTH(de.dtLancamento) = :mes "
                                + "and YEAR(de.dtLancamento) = :ano "
                );
                totalGasto =  (BigDecimal) query
                        .setParameter("idUsuario", idUsuario)
                        .setParameter("mes", mes)
                        .setParameter("ano", ano)
                        .getSingleResult()
                        ;

            } catch (NoResultException e) {
                //e.printStackTrace();
                //saldoReceita = null;
            }catch(Exception e){
                e.printStackTrace();
            } 
            finally {
                   em.close();
            }
            
            if(totalGasto == null){
                totalGasto = BigDecimal.ZERO;
            }
            
            return totalGasto;
    }
    
    public BigDecimal getTotalReceitaMes(long idUsuario){
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        BigDecimal totalReceita = null;
        
         EntityManager em = JPAUtil.getEntityManager();
        
            try {
                Query query = em.createQuery(
                        "select sum(r.valorParcela) from Receita r " +
                                "where r.conta.usuario.id = :idUsuario "
                                + "and MONTH(r.dtLancamento) = :mes "
                                + "and YEAR(r.dtLancamento) = :ano "
                );
                totalReceita =  (BigDecimal) query
                        .setParameter("idUsuario", idUsuario)
                        .setParameter("mes", mes)
                        .setParameter("ano", ano)
                        .getSingleResult()
                        ;

            } catch (NoResultException e) {
                //e.printStackTrace();
                //saldoReceita = null;
            }catch(Exception e){
                e.printStackTrace();
            } 
            finally {
                   em.close();
            }
            
            if(totalReceita == null){
                totalReceita = BigDecimal.ZERO;
            }
            
            return totalReceita;
    }
    
    private void alterarSubcategoria(Despesa despesa){
        
        EntityManager em = JPAUtil.getEntityManager();
        try{

            em.getTransaction().begin();
            em.createQuery("update from Despesa d " +
                    "set d.categoria = :categoria " +
                    "where d.idRepeticao = :id " +
                    "and d.dtLancamento >= :data")
                    .setParameter("categoria", despesa.getCategoria())
                    .setParameter("id", despesa.getIdRepeticao())
                    .setParameter("data", despesa.getDtLancamento())
                    .executeUpdate();
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();

        }finally {
            em.close();
        }
        
    }
   
    private void alterarCategoria(Receita receita){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try{
            em.getTransaction().begin();
            em.createQuery("update from Receita r " +
                    "set r.categoria = :categoria " +
                    "where r.idRepeticao = :id " +
                    "and r.dtLancamento >= :data")
                    .setParameter("categoria", receita.getCategoria())
                    .setParameter("id", receita.getIdRepeticao())
                    .setParameter("data", receita.getDtLancamento())
                    .executeUpdate();
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();

        }finally {
            em.close();
        }
        
    }
    
    private void alterarConta(Despesa despesa){
        
        EntityManager em = JPAUtil.getEntityManager();
        try{

            em.getTransaction().begin();
            em.createQuery("update from Despesa d " +
                    "set d.conta = :conta " +
                    "where d.idRepeticao = :id " +
                    "and d.dtLancamento >= :data")
                    .setParameter("conta", despesa.getConta())
                    .setParameter("id", despesa.getIdRepeticao())
                    .setParameter("data", despesa.getDtLancamento())
                    .executeUpdate();
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();

        }finally {
            em.close();
        }
        
    }
    
    private void alterarConta(Receita receita){
        
        EntityManager em = JPAUtil.getEntityManager();
        try{

            em.getTransaction().begin();
            em.createQuery("update from Receita r " +
                    "set r.conta = :conta " +
                    "where r.idRepeticao = :id " +
                    "and r.dtLancamento >= :data")
                    .setParameter("conta", receita.getConta())
                    .setParameter("id", receita.getIdRepeticao())
                    .setParameter("data", receita.getDtLancamento())
                    .executeUpdate();
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();

        }finally {
            em.close();
        }
        
    }
 
    private void alterarDescricaoDespesas(Despesa despesa){
        EntityManager em = JPAUtil.getEntityManager();

        System.out.println(despesa.getDescDespesa());

        try{

            em.getTransaction().begin();
            em.createQuery("update from Despesa d " +
                    "set d.descDespesa = :descricao " +
                    "where d.idRepeticao = :id " +
                    "and d.dtLancamento >= :data")
                    .setParameter("descricao", despesa.getDescDespesa())
                    .setParameter("id", despesa.getIdRepeticao())
                    .setParameter("data", despesa.getDtLancamento())
                    .executeUpdate();
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();

        }finally {
            em.close();
        }
    }
    
    private void alterarDescricaoReceitas(Receita receita){
        EntityManager em = JPAUtil.getEntityManager();

        System.out.println(receita.getDescReceita());

        try{

            em.getTransaction().begin();
            em.createQuery("update from Receita r " +
                    "set r.descReceita = :descricao " +
                    "where r.idRepeticao = :id " +
                    "and r.dtLancamento >= :data")
                    .setParameter("descricao", receita.getDescReceita())
                    .setParameter("id", receita.getIdRepeticao())
                    .setParameter("data", receita.getDtLancamento())
                    .executeUpdate();
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();

        }finally {
            em.close();
        }
    }
    
    private void alterarValorTotalDespesa(Despesa despesa, Despesa despesaOriginal){

        BigDecimal valorPago;

        if (despesa.getNumParcela() == 1){
            valorPago = new BigDecimal("0.00");
        }else{
            valorPago = obterValorPago(despesa);
        }

        System.out.println("Valor Pago: " + valorPago);
        BigDecimal valorASerParcelado = despesa.getValorTotalDespesa().subtract(valorPago);
        System.out.println("Valor a ser pago: " + valorASerParcelado);
        int qtdeParcelasAbertas = (obterQtdeParcelas(despesa) - despesa.getNumParcela() + 1);
        System.out.println("Quantidade de Parcelas restantes: " + qtdeParcelasAbertas);

        int j = 0;
        if (despesaOriginal.getIdRepeticao() != 0){
            for (int i = despesa.getNumParcela(); i <= obterQtdeParcelas(despesa); i++) {

                EntityManager em = JPAUtil.getEntityManager();

                BigDecimal valorParcela = calculaParcela(qtdeParcelasAbertas, valorASerParcelado, j);

                /*Obtém informações do objeto repetido exceto id*/
                Despesa novaDespesa;

                try {
                    novaDespesa = em.find(Despesa.class, despesa.getId() + (long) j);
                    novaDespesa.setValorTotalDespesa(despesa.getValorTotalDespesa());
                    if(novaDespesa.getTipoRepeticao() != null) {
                        if(novaDespesa.getTipoRepeticao().getId() != 8) {
                            novaDespesa.setValorParcela(calculaParcela(qtdeParcelasAbertas, valorASerParcelado, j));
                        }
                    }else{
                        novaDespesa.setValorParcela(calculaParcela(qtdeParcelasAbertas, valorASerParcelado, j));
                    }
                    

                    this.alterarDespesa(novaDespesa);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }

                j = j + 1;
            }
        }
    }
    
    private void alterarValorTotalReceita(Receita receita, Receita receitaOriginal){

        BigDecimal valorPago;

        if (receita.getNumParcela() == 1){
            valorPago = new BigDecimal("0.00");
        }else{
            valorPago = obterValorPago(receita);
        }
        
        BigDecimal valorASerParcelado = receita.getValorTotalReceita().subtract(valorPago);
        int qtdeParcelasAbertas = (obterQtdeParcelas(receita) - receita.getNumParcela() + 1);

        int j = 0;
        if (receitaOriginal.getIdRepeticao() != 0){
            for (int i = receita.getNumParcela(); i <= obterQtdeParcelas(receita); i++) {

                EntityManager em = JPAUtil.getEntityManager();

                /*Obtém informações do objeto repetido exceto id*/
                Receita novaReceita = new Receita();

                try {
                    novaReceita = em.find(Receita.class, receita.getId() + (long) j);
                    novaReceita.setValorTotalReceita(receita.getValorTotalReceita());
                    if(receita.getTipoRepeticao() != null) {
                        if(receita.getTipoRepeticao().getId() != 8) {
                            novaReceita.setValorParcela(calculaParcela(qtdeParcelasAbertas, valorASerParcelado, j));
                        }
                    }else {
                        novaReceita.setValorParcela(receita.getValorParcela());
                    }

                    this.alterarReceita(novaReceita);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }

                j = j + 1;
            }
        }
    }
    
    private void alterarDtLancamento(Despesa despesa, Despesa despesaOriginal){
        int j = 0;

        if (despesaOriginal.getIdRepeticao() != 0){
            
            for (int i = despesa.getNumParcela(); i <= obterQtdeParcelas(despesa); i++){
                EntityManager em = JPAUtil.getEntityManager();

                Despesa novaDespesa = new Despesa();
                try {
                    /*Busca despesa a ser alterada*/
                    TypedQuery<Despesa> query = em.createQuery("select d from Despesa d " +
                        "where d.idRepeticao = :id and d.numParcela = :numParcela", Despesa.class);
                      novaDespesa = query
                        .setParameter("id", despesa.getIdRepeticao())
                        .setParameter("numParcela", i)
                        .getSingleResult();
                      
                    novaDespesa.setDtLancamento(obterData(despesa, j));
                    
                    this.alterarDespesa(novaDespesa);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }

                j = j + 1;
            }
        }
    }
    
    private void alterarDtLancamento(Receita receita, Receita receitaOriginal){
        int j = 0;
        
        if (receitaOriginal.getIdRepeticao() != 0){         
          
            for (int i = receita.getNumParcela(); i <= obterQtdeParcelas(receita); i++){
                EntityManager em = JPAUtil.getEntityManager();

                Receita novaReceita = new Receita();
                try {
                    
                    /*Busca receita a ser alterada*/
                    TypedQuery<Receita> query = em.createQuery("select r from Receita r " +
                        "where r.idRepeticao = :id and r.numParcela = :numParcela", Receita.class);
                      novaReceita = query
                        .setParameter("id", receita.getIdRepeticao())
                        .setParameter("numParcela", i)
                        .getSingleResult();
                      
                    novaReceita.setDtLancamento(obterData(receita, j));

                    this.alterarReceita(novaReceita);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }

                j = j + 1;
            }
        }
    }
    
    private void alterarNumeroRepeticoes(Despesa despesaOriginal, Despesa despesaAlterada) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityManager entityManager = JPAUtil.getEntityManager();
        
        Despesa novaDespesa = null;
        int totalParcelasPagas = despesaAlterada.getNumParcela() - 1;
        int qtdeParcelasAbertas = (obterQtdeParcelas(despesaAlterada) - totalParcelasPagas);
        
        /*Obtem valor total a pagar*/
        BigDecimal valorTotal = obterValorTotalParcelasRestantes(despesaAlterada);

        if (obterQtdeParcelas(despesaAlterada) < obterQtdeParcelas(despesaOriginal)) {

            try {
                /*Deleta as parcelas excedentes*/
                em.getTransaction().begin();
                em.createQuery("delete from Despesa  d where d.idRepeticao = :id and d.numParcela > :num")
                        .setParameter("id", despesaAlterada.getIdRepeticao())
                        .setParameter("num", obterQtdeParcelas(despesaAlterada))
                        .executeUpdate();
                em.getTransaction().commit();

            }catch (Exception ex){
                em.getTransaction().rollback();
                System.out.println("DELETE: " + ex.getMessage());
            }finally {
                em.close();
            }

            int j = 0;

            if(obterQtdeParcelas(despesaAlterada) != 1){
                try{
                    /*Altera o valor da parcela nas parcelas restantes*/
                    for (int i = despesaAlterada.getNumParcela(); i <= obterQtdeParcelas(despesaAlterada); i++) {

                        BigDecimal valor = calculaParcela (qtdeParcelasAbertas, valorTotal, j);

                        entityManager.getTransaction().begin();

                        /*Obtém a despesa a ser alterada*/
                        TypedQuery<Despesa> query = entityManager.createQuery("select d from Despesa d " +
                                "where d.idRepeticao = :id and d.numParcela = :numParcela", Despesa.class);
                        novaDespesa = query.setParameter("id", despesaAlterada.getIdRepeticao())
                                .setParameter("numParcela", i)
                                .getSingleResult();
                        
                        novaDespesa.setQtdeParcelas(despesaAlterada.getQtdeParcelas());
                        novaDespesa.setValorParcela(valor);

                        this.alterarDespesa(novaDespesa);

                        entityManager.getTransaction().commit();

                        j = j + 1;

                    }
                }catch (Exception e){
                    entityManager.getTransaction().rollback();
                    System.out.println("UPDATE: " + e.getMessage());
                    e.printStackTrace();
                }finally {
                    entityManager.close();
                }
            }
        }else{
             int qtdeParcelasExcedentes = obterQtdeParcelas(despesaAlterada) - obterQtdeParcelas(despesaOriginal);
             System.out.println("Quantidade registros a ser inseridos: " + qtdeParcelasExcedentes);
             Despesa ultimaParcela = null;

             /*Altera os registros existentes*/
            int j = 0;

            try{
                /*Altera o valor da parcela nas parcelas restantes*/
                for (int i = despesaAlterada.getNumParcela(); i <= obterQtdeParcelas(despesaOriginal); i++) {

                    BigDecimal valor = calculaParcela(qtdeParcelasAbertas, valorTotal, j);
                    System.out.println("Parcela " + i + " valor: " + valor);

                    entityManager.getTransaction().begin();

                    /*Obtém a despesa a ser alterada*/
                    TypedQuery<Despesa> query = entityManager.createQuery("select d from Despesa d " +
                            "where d.idRepeticao = :id and d.numParcela = :numParcela", Despesa.class);
                    novaDespesa = query.setParameter("id", despesaAlterada.getIdRepeticao())
                            .setParameter("numParcela", i)
                            .getSingleResult();

                    System.out.println("Id da despesa alterada: " + novaDespesa.getId());
                    novaDespesa.setQtdeParcelas(obterQtdeParcelas(despesaAlterada));
                    novaDespesa.setValorParcela(valor);

                    this.alterarDespesa(novaDespesa);

                    entityManager.getTransaction().commit();

                    j = j + 1;

                }
            }catch (Exception e){
                entityManager.getTransaction().rollback();
                System.out.println("UPDATE: " + e.getMessage());
                e.printStackTrace();
            }finally {
                entityManager.close();
            }

            /*Insere as parcelas excedentes*/
            try{
                /*Obtém a última parcela da despesa*/
               int maxParcela = obterNumMaxParcela(despesaAlterada);
               
               System.out.println("Ultima parcela: " + maxParcela);

                TypedQuery<Despesa> query = em.createQuery("select d from Despesa d " +
                        "where d.idRepeticao = :idRepeticao and d.numParcela = :numParcela", Despesa.class);
                ultimaParcela = query
                        .setParameter("idRepeticao", despesaAlterada.getIdRepeticao())
                        .setParameter("numParcela", maxParcela)
                        .getSingleResult();

            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                em.close();
            }

            /*Insere Despesas Excedentes*/
            EntityManager novaEntity = JPAUtil.getEntityManager();

            for (int i = 1; i <=qtdeParcelasExcedentes; i++) {
                    
                System.out.println("Valor Total: " + valorTotal);

                BigDecimal valor = calculaParcela(qtdeParcelasAbertas, valorTotal, j);
                    
                System.out.println("Valor Parcela: " + valor);

                /*Obtém informações da última parcela e adiciona as informações da nova Parcela*/
                Despesa despesaNova = new Despesa();
                despesaNova.setDescDespesa(ultimaParcela.getDescDespesa());
                despesaNova.setIdRepeticao(ultimaParcela.getIdRepeticao());
                despesaNova.setTipoRepeticao(ultimaParcela.getTipoRepeticao());
                despesaNova.setRepeticao(ultimaParcela.isRepeticao());
                despesaNova.setNumParcela(ultimaParcela.getNumParcela()+i);
                despesaNova.setQtdeParcelas(ultimaParcela.getQtdeParcelas());
                despesaNova.setValorParcela(calculaParcela(qtdeParcelasAbertas, valorTotal, j));
                despesaNova.setValorTotalDespesa(ultimaParcela.getValorTotalDespesa());
                despesaNova.setDtLancamento(obterData(ultimaParcela,i));
                despesaNova.setCategoria(ultimaParcela.getCategoria());
                despesaNova.setConta(ultimaParcela.getConta());

                /*Insere a nova parcela no banco de dados*/
                inserirDespesaBancodeDados(despesaNova);

                j = j + 1;
            }
        }
    }
    
    private void alterarNumeroRepeticoes(Receita receitaOriginal, Receita receitaAlterada) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityManager entityManager = JPAUtil.getEntityManager();
        Receita novaReceita = null;
        int totalParcelasPagas = receitaAlterada.getNumParcela() - 1;
        int qtdeParcelasAbertas = (obterQtdeParcelas(receitaAlterada) - totalParcelasPagas);
        /*Obtem valor total a pagar*/
        BigDecimal valorTotal = obterValorTotalParcelasRestantes(receitaAlterada);
        
        System.out.println("Quantidade de Parcelas Despesa alterada: " + obterQtdeParcelas(receitaAlterada));
        System.out.println("Quantidade de Parcelas Despesa original: " + obterQtdeParcelas(receitaOriginal));

        if (obterQtdeParcelas(receitaAlterada) < obterQtdeParcelas(receitaOriginal)) {

            System.out.println("obterQtdeParcelas(receitaAlterada) < obterQtdeParcelas(receitaOriginal) ");
            try {
                
                /*Deleta as parcelas excedentes*/
                em.getTransaction().begin();
                em.createQuery("delete from Receita r where r.idRepeticao = :id and r.numParcela > :num")
                        .setParameter("id", receitaAlterada.getIdRepeticao())
                        .setParameter("num", obterQtdeParcelas(receitaAlterada))
                        .executeUpdate();
                em.getTransaction().commit();

            }catch (Exception ex){
                em.getTransaction().rollback();
                System.out.println("DELETE: " + ex.getMessage());
            }finally {
                em.close();
            }

            int j = 0;

            if(obterQtdeParcelas(receitaAlterada) != 1){
                try{
                    /*Altera o valor da parcela nas parcelas restantes*/
                    for (int i = receitaAlterada.getNumParcela(); i <= obterQtdeParcelas(receitaAlterada); i++) {

                        BigDecimal valor = calculaParcela (qtdeParcelasAbertas, valorTotal, j);

                        entityManager.getTransaction().begin();

                        /*Obtém a despesa a ser alterada*/
                        TypedQuery<Receita> query = entityManager.createQuery("select r from Receita r " +
                                "where r.idRepeticao = :id and r.numParcela = :numParcela", Receita.class);
                        novaReceita = query.setParameter("id", receitaAlterada.getIdRepeticao())
                                .setParameter("numParcela", i)
                                .getSingleResult();

                        novaReceita.setQtdeParcelas(receitaAlterada.getQtdeParcelas());
                        novaReceita.setValorParcela(valor);

                        this.alterarReceita(novaReceita);

                        entityManager.getTransaction().commit();

                        j = j + 1;

                    }
                }catch (Exception e){
                    entityManager.getTransaction().rollback();
                    System.out.println("UPDATE: " + e.getMessage());
                    e.printStackTrace();
                }finally {
                    entityManager.close();
                }
            }
        }else{
            System.out.println("else ");
             int qtdeParcelasExcedentes = obterQtdeParcelas(receitaAlterada) - obterQtdeParcelas(receitaOriginal);
             System.out.println("Quantidade registros a ser inseridos: " + qtdeParcelasExcedentes);
             Receita ultimaParcela = null;

             /*Altera os registros existentes*/
            int j = 0;

            try{
                /*Altera o valor da parcela nas parcelas restantes*/
                for (int i = receitaAlterada.getNumParcela(); i <= obterQtdeParcelas(receitaOriginal); i++) {

                    BigDecimal valor = calculaParcela(qtdeParcelasAbertas, valorTotal, j);
                    
                    System.out.println("for - idRepeticao: " + receitaAlterada.getIdRepeticao());
                    System.out.println("for - numero parcela: " + i);

                    entityManager.getTransaction().begin();

                    /*Obtém a receita a ser alterada*/
                    TypedQuery<Receita> query = entityManager.createQuery("select r from Receita r " +
                            "where r.idRepeticao = :id and r.numParcela = :numParcela", Receita.class);
                    novaReceita = query.setParameter("id", receitaAlterada.getIdRepeticao())
                            .setParameter("numParcela", i)
                            .getSingleResult();

                    System.out.println("Id da despesa alterada: " + novaReceita.getId());
                    novaReceita.setQtdeParcelas(receitaAlterada.getQtdeParcelas());
                    novaReceita.setValorParcela(valor);

                    this.alterarReceita(novaReceita);

                    entityManager.getTransaction().commit();

                    j = j + 1;

                }
            }catch (Exception e){
                entityManager.getTransaction().rollback();
                System.out.println("UPDATE: " + e.getMessage());
                e.printStackTrace();
            }finally {
                entityManager.close();
            }

            /*Insere as parcelas excedentes*/
            try{
                /*Obtém a última parcela da despesa*/
                int maxParcela = obterNumMaxParcela(receitaAlterada);
                System.out.println("maxParcela:"+maxParcela);

                TypedQuery<Receita> query = em.createQuery("select r from Receita r " +
                        "where r.idRepeticao = :idRepeticao and r.id = :id and r.numParcela = :numParcela", Receita.class);
                ultimaParcela = query
                        .setParameter("idRepeticao", receitaAlterada.getIdRepeticao())
                        .setParameter("numParcela", maxParcela)
                        .setParameter("id", receitaAlterada.getId())
                        .getSingleResult();

            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                em.close();
            }

            /*Insere Despesas Excedentes*/
            EntityManager novaEntity = JPAUtil.getEntityManager();

                for (int i = 1; i <=qtdeParcelasExcedentes; i++) {

                    /*Obtém informações da última parcela e adiciona as informações da nova Parcela*/
                    Receita receitaNova = new Receita();
                    receitaNova.setDescReceita(ultimaParcela.getDescReceita());
                    receitaNova.setIdRepeticao(ultimaParcela.getIdRepeticao());
                    receitaNova.setTipoRepeticao(ultimaParcela.getTipoRepeticao());
                    receitaNova.setRepeticao(ultimaParcela.isRepeticao());
                    receitaNova.setNumParcela(ultimaParcela.getNumParcela()+i);
                    receitaNova.setQtdeParcelas(ultimaParcela.getQtdeParcelas());
                    receitaNova.setValorParcela(calculaParcela (qtdeParcelasAbertas, valorTotal, j));
                    receitaNova.setValorTotalReceita(ultimaParcela.getValorTotalReceita());
                    receitaNova.setDtLancamento(obterData(ultimaParcela,i));
                    receitaNova.setCategoria(ultimaParcela.getCategoria());
                    receitaNova.setConta(ultimaParcela.getConta());

                    /*Insere a nova parcela no banco de dados*/
                    this.inserirReceitaBancodeDados(receitaNova);
                    
                    j = j + 1;

                }
        }
    }
    
    private void alterarRepeticao(Despesa despesa){
        EntityManager em = JPAUtil.getEntityManager();
        if(despesa.isRepeticao()){
            if(despesa.getIdRepeticao() == 0){
                despesa.setIdRepeticao(despesa.getId());
            }
        }else{
            if(despesa.getNumParcela() == 1){
                long idRepeticao = despesa.getIdRepeticao();
                despesa.setTipoRepeticao(null);
                despesa.setIdRepeticao(0);
                despesa.setValorParcela(despesa.getValorTotalDespesa());
                despesa.setQtdeParcelas(1);
                Despesa proximaDespesa = null;
                
                try{
                    //Despesa proximaDespesa = em.find(Despesa.class, despesa.getId() + (long)1);
                     TypedQuery<Despesa> query = em.createQuery("select d from Despesa d " +
                        "where d.idRepeticao = :id and d.numParcela = :numParcela", Despesa.class);
                    proximaDespesa = query
                            .setParameter("id", idRepeticao)
                            .setParameter("numParcela", despesa.getNumParcela() + 1)
                            .getSingleResult();
                    
                    if (proximaDespesa != null){
                        this.removerDespesas(proximaDespesa.getId());
                    }

                }catch (Exception e){
                   // em.getTransaction().rollback();
                    e.printStackTrace();

                }finally {
                    em.close();
                }
            }else{
                despesa.setQtdeParcelas(despesa.getNumParcela());
                try{
                    Despesa proximaDespesa = em.find(Despesa.class, despesa.getId() + (long)1);
                    if (proximaDespesa != null){
                        this.removerDespesas(proximaDespesa.getId());
                    }

                }catch (Exception e){
                    em.getTransaction().rollback();
                    e.printStackTrace();

                }finally {
                    em.close();
                }
            }
        }
        this.alterarDespesa(despesa);
    }
    
    private void alterarRepeticao(Receita receita){
        
        EntityManager em = JPAUtil.getEntityManager();
        
        if(receita.isRepeticao()){
            if(receita.getIdRepeticao() == 0){
                receita.setIdRepeticao(receita.getId());
                System.out.println("Método alterar repeticao -  receita repeticao: "  + receita.getIdRepeticao());
            }
        }else{
            if(receita.getNumParcela() == 1){
                receita.setTipoRepeticao(null);
                receita.setIdRepeticao(0);
                receita.setValorParcela(receita.getValorTotalReceita());
                receita.setQtdeParcelas(1);

                try{
                    Receita proximaReceita = em.find(Receita.class, receita.getId() + (long)1);
                    if (proximaReceita != null){
                        this.removerReceitas(proximaReceita.getId());
                    }

                }catch (Exception e){
                    em.getTransaction().rollback();
                    e.printStackTrace();

                }finally {
                    em.close();
                }
            }else{
                receita.setQtdeParcelas(receita.getNumParcela());
                try{
                    Receita proximaReceita = em.find(Receita.class, receita.getId() + (long)1);
                    if (proximaReceita != null){
                        this.removerReceitas(proximaReceita.getId());
                    }

                }catch (Exception e){
                    em.getTransaction().rollback();
                    e.printStackTrace();

                }finally {
                    em.close();
                }
            }
        }
        this.alterarReceita(receita);
    }
    
    private void alterarTipoRepeticao(Despesa despesa, Despesa despesaOriginal){
        int j = 0;
        if (despesaOriginal.getIdRepeticao() != 0){

            for (int i = despesa.getNumParcela(); i <= obterQtdeParcelas(despesa); i++){
                EntityManager em = JPAUtil.getEntityManager();

                Despesa novaDespesa = new Despesa();
                System.out.println("Quantidade de parcelas: " + obterQtdeParcelas(despesa));
                System.out.println("IdRepeticao de parcelas: " + despesa.getIdRepeticao());

                try {
                    novaDespesa = em.find(Despesa.class, despesa.getId() + (long) j);
                    novaDespesa.setTipoRepeticao(despesa.getTipoRepeticao());
                    novaDespesa.setDtLancamento(obterData(despesa, j));

                    this.alterarDespesa(novaDespesa);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }

                j = j + 1;
            }
        }

    }
    
    private void alterarTipoRepeticao(Receita receita, Receita receitaOriginal){
        
        int j = 0;
        if (receitaOriginal.getIdRepeticao() != 0){

            for (int i = receita.getNumParcela(); i <= obterQtdeParcelas(receita); i++){
                EntityManager em = JPAUtil.getEntityManager();

                Receita novaReceita = new Receita();
                System.out.println("Quantidade de parcelas: " + obterQtdeParcelas(receita));
                System.out.println("IdRepeticao de parcelas: " + receita.getIdRepeticao());
                
                System.out.println("Repeticao: " + receita.getTipoRepeticao().getDescTipoRepeticao());
                

                try {
                    novaReceita = em.find(Receita.class, receita.getId() + (long) j);
                    novaReceita.setTipoRepeticao(this.getTipoRepeticaoById(receita.getTipoRepeticao().getId()));
                    novaReceita.setDtLancamento(obterData(receita, j));

                    this.alterarReceita(novaReceita);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }

                j = j + 1;
            }
        }

    }
    
    private void alterarValorParcela(Despesa despesaAlterada, Despesa despesaOriginal){
       if (obterQtdeParcelas(despesaAlterada) == despesaAlterada.getNumParcela()){
            try {
                despesaAlterada.setValorTotalDespesa(
                     obterValorPago(despesaAlterada).add(despesaAlterada.getValorParcela())
                );
            }catch (NullPointerException e) {
                System.out.println("alterarDespesas -> alterarValorParcelas -> setValorTotalReceita: "+e.getMessage());
            }
           
           this.alterarDespesa(despesaAlterada);

       }else{
           BigDecimal valorTotalParcelas = obterValorTotalParcelasRestantes(despesaOriginal);
           BigDecimal valorTotalRestante = valorTotalParcelas.subtract(despesaAlterada.getValorParcela());
           int qtdeParcelasRestantes = obterQtdeParcelas(despesaAlterada) - despesaAlterada.getNumParcela();
           this.alterarDespesa(despesaAlterada);
           
           int j = 0;
           for (int i = despesaAlterada.getNumParcela()+1; i <= obterQtdeParcelas(despesaAlterada); i++){
               
               EntityManager em = JPAUtil.getEntityManager();
               Despesa novaDespesa = null;

               try{
                   
                   TypedQuery<Despesa> query = em.createQuery("select d from Despesa d " +
                       "where d.idRepeticao = :id and d.numParcela = :numParcela", Despesa.class);
                   novaDespesa = query
                       .setParameter("id", despesaAlterada.getIdRepeticao())
                       .setParameter("numParcela", i)
                       .getSingleResult();
                   
                   if(despesaAlterada.getTipoRepeticao().getId() != 8) { //infinita
                       novaDespesa.setValorParcela(calculaParcela(qtdeParcelasRestantes,valorTotalRestante,j));
                   }
                   novaDespesa.setValorTotalDespesa(despesaAlterada.getValorTotalDespesa());
                   this.alterarDespesa(novaDespesa);

               }catch (Exception e){
                   e.printStackTrace();
               }finally {
                   em.close();
               }
               j = j + 1;
           }
       }
   }
    
    private void alterarValorParcela(Receita receitaAlterada, Receita receitaOriginal){

        if (obterQtdeParcelas(receitaAlterada) == receitaAlterada.getNumParcela()){
            try {
                receitaAlterada.setValorTotalReceita(
                    obterValorPago(receitaAlterada).add(receitaAlterada.getValorParcela())
                );
            }catch (NullPointerException e) {
                System.out.println("alterarReceitas -> alterarValorParcelas -> setValorTotalReceita: "+e.getMessage());
            }
            
            this.alterarReceita(receitaAlterada);

        }else{
            BigDecimal valorTotalParcelas = obterValorTotalParcelasRestantes(receitaOriginal);
            
            System.out.println("Valor restante a pagar: " + valorTotalParcelas);
            System.out.println("Novo valor parcela: " + receitaAlterada.getValorParcela());
            BigDecimal valorTotalRestante = valorTotalParcelas.subtract(receitaAlterada.getValorParcela());
            System.out.println("Valor Total Parcelas: " + valorTotalRestante);
            int qtdeParcelasRestantes = obterQtdeParcelas(receitaAlterada) - receitaAlterada.getNumParcela();
            System.out.println("Qtde Parcelas restantes: " + qtdeParcelasRestantes);

            this.alterarReceita(receitaAlterada);
            
            int j = 0;
            
            for (int i = receitaAlterada.getNumParcela()+1; i <= obterQtdeParcelas(receitaAlterada);i++){
                
                EntityManager em = JPAUtil.getEntityManager();
                Receita novaReceita;

                try{
                    //novaReceita = em.find(Receita.class, (long)i);
                    TypedQuery<Receita> query = em.createQuery("select re from Receita re " +
                        "where re.idRepeticao = :id and re.numParcela = :numParcela", Receita.class);
                    novaReceita = query
                        .setParameter("id", receitaAlterada.getIdRepeticao())
                        .setParameter("numParcela", i)
                        .getSingleResult();
                    
                    novaReceita.setValorParcela(calculaParcela(qtdeParcelasRestantes,valorTotalRestante,j));
                    System.out.println("Valor total da compra: " + receitaAlterada.getValorTotalReceita());
                    novaReceita.setValorTotalReceita(receitaAlterada.getValorTotalReceita());
                    this.alterarReceita(novaReceita);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    em.close();
                }
                j = j + 1;
            }
        }
    }
    
    private static BigDecimal calculaParcela(int qtdeRestanteParcela, 
            BigDecimal valorParcelaRestante, int iteracao){

        /*Multiplica por 100 pra tirar as casas decimais*/
        BigDecimal valorASerDividido = valorParcelaRestante.multiply(new BigDecimal("100"));
        BigDecimal restoDivisao = valorASerDividido.remainder(new BigDecimal(Integer.toString(qtdeRestanteParcela)));
        BigDecimal auxParcela = valorASerDividido.divide
                (new BigDecimal(Integer.toString(qtdeRestanteParcela)),0, RoundingMode.HALF_EVEN);

        /* O compareTo tem 3 retornos possíveis: -1 quando o número é menor,
                0 quando é igual  e 1 quando for maior
         */
        BigDecimal valorParcela;
        if (restoDivisao.compareTo(new BigDecimal("0.00")) == 1  && iteracao == 0){
            System.out.println("iteracao: " + iteracao);
            valorParcela = (auxParcela.add(restoDivisao)).divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN
            );
        }else{
            valorParcela = auxParcela.divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        }

        return valorParcela;
    }
    
    private static BigDecimal calculaParcela(Despesa despesa){
        BigDecimal valorParcela = new BigDecimal("0");

        /*Multiplica por 100 pra tirar as casas decimais*/
        BigDecimal valorRecebido = despesa.getValorTotalDespesa().multiply(new BigDecimal("100"));
        BigDecimal restoDivisao = valorRecebido.remainder(new BigDecimal(Integer.toString(obterQtdeParcelas(despesa))));
        BigDecimal auxParcela = valorRecebido.divide
                (new BigDecimal(Integer.toString(obterQtdeParcelas(despesa))),0, RoundingMode.HALF_EVEN);

        /* O compareTo tem 3 retornos possíveis: -1 quando o número é menor,
                0 quando é igual  e 1 quando for maior
         */
        if (restoDivisao.compareTo(new BigDecimal("0.00")) == 1  && despesa.getNumParcela() == 1){
            valorParcela = (auxParcela.add(restoDivisao)).divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN
            );
        }else{
            valorParcela = auxParcela.divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        }

        return valorParcela;
    }
    
    private static BigDecimal obterValorTotalParcelasRestantes(Despesa despesa){

        EntityManager em = JPAUtil.getEntityManager();
        BigDecimal totalParcela = new BigDecimal("0.0");
        if (despesa.getNumParcela() == 1){
            totalParcela = despesa.getValorTotalDespesa();

        }else {
            try {
                Query query = em.createQuery(
                        "select sum(d.valorParcela) from  Despesa d " +
                                "where d.idRepeticao = :id and d.dtLancamento >= :data");
                totalParcela = (BigDecimal) query.setParameter("id", despesa.getIdRepeticao())
                        .setParameter("data", despesa.getDtLancamento())
                        .getSingleResult();
            } catch (Exception e) {

            } finally {
                em.close();
            }
        }

        return totalParcela;
    }
    
    private static BigDecimal obterValorTotalParcelasRestantes(Receita receita){

        EntityManager em = JPAUtil.getEntityManager();
        BigDecimal totalParcela = new BigDecimal("0.0");
        if (receita.getNumParcela() == 1){
            totalParcela = receita.getValorTotalReceita();

        }else {
            try {
                Query query = em.createQuery(
                        "select sum(r.valorParcela) from  Receita r " +
                                "where r.idRepeticao = :id and r.dtLancamento >= :data");
                totalParcela = (BigDecimal) query.setParameter("id", receita.getIdRepeticao())
                        .setParameter("data", receita.getDtLancamento())
                        .getSingleResult();
            } catch (Exception e) {

            } finally {
                em.close();
            }
        }
        

        return totalParcela;
    }
    
    private static BigDecimal obterValorPago(Despesa despesa){

        EntityManager em = JPAUtil.getEntityManager();
        BigDecimal totalPago = new BigDecimal("0.0");

        try{
            Query query = em.createQuery(
                    "select sum(d.valorParcela) from Despesa d " +
                            "where d.idRepeticao = :idRepeticao " +
                            "and d.numParcela < :numParcela"
            );

            totalPago = (BigDecimal) query
                    .setParameter("idRepeticao", despesa.getIdRepeticao())
                    .setParameter("numParcela", despesa.getNumParcela())
                    .getSingleResult();
        }catch (Exception e){

        }finally {
            em.close();
        }

        return totalPago;
    }
    
    private static BigDecimal obterValorPago(Receita receita){

        EntityManager em = JPAUtil.getEntityManager();
        BigDecimal totalPago = new BigDecimal("0.0");

        try{
            Query query = em.createQuery(
                    "select sum(r.valorParcela) from Receita r " +
                            "where r.idRepeticao = :idRepeticao " +
                            "and r.numParcela < :numParcela"
            );

            totalPago = (BigDecimal) query
                    .setParameter("idRepeticao", receita.getIdRepeticao())
                    .setParameter("numParcela", receita.getNumParcela())
                    .getSingleResult();
        }catch (Exception e){
             e.printStackTrace();
        }finally {
            em.close();
        }

        return totalPago;
    }
    
    private LocalDate obterData(Receita receita, int numParcela) {
        
        /*Transforma data em calendar*/
        Calendar c = Calendar.getInstance();
        c.setTime(Date.from(receita.getDtLancamento().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        int qtdeSomada;
        
        if(receita.getTipoRepeticao() != null) {
            switch (new Long(receita.getTipoRepeticao().getId()).intValue()) {
                case 1: //diaria
                    qtdeSomada = numParcela;
                    break;
                case 2: //Semanal
                    qtdeSomada = numParcela * 7;
                    break;
                case 3: //Mensal
                    qtdeSomada = numParcela;
                    break;
                case 4: //Bimestral
                    qtdeSomada = numParcela * 2;
                    break;
                case 5: //Trimestral
                    qtdeSomada = numParcela * 3;
                    break;
                case 6: //Semanal
                    qtdeSomada = numParcela * 6;
                    break;
                case 7: //Anual
                    qtdeSomada = numParcela;
                    break;
                case 8: //Infinita
                    qtdeSomada = numParcela;
                    break;
                default:
                    qtdeSomada = 0;
            }
            
            if (receita.getTipoRepeticao().getId() <= 2) {
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + qtdeSomada);
            } else if (receita.getTipoRepeticao().getId() == 7){
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + qtdeSomada);
            }else {
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + qtdeSomada);
            }
        }else {
          qtdeSomada = 0;  
        }
        
        LocalDate localDate = LocalDateTime.ofInstant(
                c.toInstant(), 
                c.getTimeZone().toZoneId()
        ).toLocalDate();
        return localDate;
    }
    
    private LocalDate obterData(Despesa despesa, int numParcela) {
        /*Transforma data em calendar*/
        Calendar c = Calendar.getInstance();
        c.setTime(Date.from(despesa.getDtLancamento().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        int qtdeSomada;
        
        if(despesa.getTipoRepeticao() != null) {
            switch (new Long(despesa.getTipoRepeticao().getId()).intValue()) {
                case 1: //diaria
                    qtdeSomada = numParcela;
                    break;
                case 2: //Semanal
                    qtdeSomada = numParcela * 7;
                    break;
                case 3: //Mensal
                    qtdeSomada = numParcela;
                    break;
                case 4: //Bimestral
                    qtdeSomada = numParcela * 2;
                    break;
                case 5: //Trimestral
                    qtdeSomada = numParcela * 3;
                    break;
                case 6: //Semanal
                    qtdeSomada = numParcela * 6;
                    break;
                case 7: //Anual
                    qtdeSomada = numParcela;
                    break;
                case 8: //Infinita
                    qtdeSomada = numParcela;
                    break;
                default:
                    qtdeSomada = 0;
            }
            
            if (despesa.getTipoRepeticao().getId() <= 2) {
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + qtdeSomada);
            } else if (despesa.getTipoRepeticao().getId() == 7){
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + qtdeSomada);
            }else {
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + qtdeSomada);
            }
        }else {
          qtdeSomada = 0;  
        }

        LocalDate localDate = LocalDateTime.ofInstant(
            c.toInstant(), 
            c.getTimeZone().toZoneId()
        ).toLocalDate();
        return localDate;
    }
    
    private Receita inserirReceitaBancodeDados(Receita receita){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.persist(receita);
            em.getTransaction().commit();
            
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("INSERT: " + e.getMessage());
            
        } finally {
            em.close();
            
        }

        return receita;
    }
    
    private static BigDecimal calculaParcela(Receita receita){
        BigDecimal valorParcela = new BigDecimal("0");

        /*Multiplica por 100 pra tirar as casas decimais*/
        BigDecimal valorRecebido = receita.getValorTotalReceita().multiply(new BigDecimal("100"));
        BigDecimal restoDivisao = valorRecebido.remainder(new BigDecimal(Integer.toString(obterQtdeParcelas(receita))));
        BigDecimal auxParcela = valorRecebido.divide
                (new BigDecimal(Integer.toString(obterQtdeParcelas(receita))),0, RoundingMode.HALF_EVEN);

        /* O compareTo tem 3 retornos possíveis: -1 quando o número é menor,
                0 quando é igual  e 1 quando for maior
         */
        if (restoDivisao.compareTo(new BigDecimal("0.00")) == 1  && receita.getNumParcela() == 1){
            valorParcela = (auxParcela.add(restoDivisao)).divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN
            );
        }else{
            valorParcela = auxParcela.divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        }

        return valorParcela;
    }
    
    private Despesa inserirDespesaBancodeDados(Despesa despesa){

        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.persist(despesa);
            em.getTransaction().commit();
            
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("INSERT: " + e.getMessage());
            
        } finally {
            em.close();
            
        }

        return despesa;
    }
    
    private static int obterQtdeParcelas(Despesa despesa){
        if(despesa.getTipoRepeticao() != null) {
            if(despesa.getTipoRepeticao().getId() == 8) {
                return 60;
            }else {
                return despesa.getQtdeParcelas();
            }
                
        }else {
            return despesa.getQtdeParcelas();
        } 
    }
    
    private static int obterQtdeParcelas(Receita receita){
        if(receita.getTipoRepeticao().getId() == 8)
            return 60;
        else
            return receita.getQtdeParcelas();
    }
    
    private static int obterNumMaxParcela(Despesa despesa){
        EntityManager em1 = JPAUtil.getEntityManager();
        int maxParcela = 0;

        try{
            Query query = em1.createQuery(
                    "select max(d.numParcela) from Despesa d " +
                            "where d.idRepeticao = :idRepeticao");
            maxParcela = (int) query
                    .setParameter("idRepeticao", despesa.getIdRepeticao())
                    .getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            em1.close();
        }

        return maxParcela;
    }
    
    private static int obterNumMaxParcela(Receita receita){
        EntityManager em1 = JPAUtil.getEntityManager();
        int maxParcela = 0;

        try{
            Query query = em1.createQuery(
                    "select max(r.numParcela) from Receita r " +
                            "where r.idRepeticao = :idRepeticao");
            maxParcela = (int) query
                    .setParameter("idRepeticao", receita.getIdRepeticao())
                    .getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            em1.close();
        }

        return maxParcela;
    }
    
}
