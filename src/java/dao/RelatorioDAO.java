/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import facade.LancamentoFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Despesa;
import model.Receita;
import model.Relatorio;
import util.JPAUtil;


public class RelatorioDAO {
    
    /*Obtém a porcentagem da Despesa sobre a Receita por mês*/
    public List<Relatorio> getAnaliseMensal(long idUsuario){
           
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);

        List<Relatorio> informacoes = new ArrayList<>();
        Relatorio novoDado;
        BigDecimal saldoReceita;
        BigDecimal saldoDespesa; 
        BigDecimal pReceita = null;
        BigDecimal pDespesa = null;
        EntityManager em;
        EntityManager entityManager;
        
        for(int i = 1; i <= 12 ; i++){
            
            novoDado = new Relatorio();
            
            em = JPAUtil.getEntityManager();
            
            saldoReceita = null;
            saldoDespesa = null;
        
            try {
                Query query = em.createQuery(
                        "select sum(r.valorParcela) from  Receita r " +
                                "where r.conta.usuario.id = :idUsuario "
                                + "and MONTH(r.dtLancamento) = :mes "
                                + "and YEAR(r.dtLancamento) = :ano "
                                + "group by MONTH(r.dtLancamento)");
                saldoReceita =  (BigDecimal) query
                        .setParameter("idUsuario", idUsuario)
                        .setParameter("mes", i)
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
            
            entityManager = JPAUtil.getEntityManager();

            try{
                Query query = entityManager.createQuery(
                        "select sum(d.valorParcela) from Despesa d " +
                                "where d.conta.usuario.id = :idUsuario "
                                 + "and MONTH(d.dtLancamento) = :mes "
                                + "and YEAR(d.dtLancamento) = :ano");

                saldoDespesa = (BigDecimal) query
                        .setParameter("idUsuario", idUsuario)
                        .setParameter("mes", i)
                        .setParameter("ano", ano)
                        .getSingleResult();

            } catch (NoResultException e) {
                //e.printStackTrace();
                //saldoReceita = null;
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                entityManager.close();
            }
            
            if(saldoDespesa == null){
                if(saldoReceita == null){
                    pDespesa = BigDecimal.ZERO;
                    pReceita = BigDecimal.ZERO;
                }else{
                    pReceita = new BigDecimal("100.00");
                    pDespesa = BigDecimal.ZERO;
                }
            }else if(saldoReceita == null){
                pDespesa = new BigDecimal("100.00");
                pReceita = BigDecimal.ZERO;
            }else{
                
                if(saldoDespesa.compareTo(saldoReceita) == 1){
                    pReceita = BigDecimal.ZERO;
                    pDespesa = new BigDecimal("100.00");
                }else{
                    BigDecimal aux = saldoDespesa.multiply(new BigDecimal("100"));
                    pDespesa = aux.divide(saldoReceita,2, RoundingMode.HALF_EVEN);
                    pReceita = new BigDecimal("100.00").subtract(pDespesa);
                }
            }
            
            novoDado.setPorcentagemReceita(pReceita);
            novoDado.setPorcentagemDespesa(pDespesa);
            novoDado.setMes(i);
            informacoes.add(novoDado);
            
            System.out.println("Mes: " + i + "% Receita: " + pReceita + " - % Despesa: " + pDespesa);
        }
        
        return informacoes;
    }
    
    public BigDecimal obterSaldoByUsuario(long idUsuario){
        
        /*Obtém a data atual*/
        LocalDate data = LocalDate.now();
         
        EntityManager entity = JPAUtil.getEntityManager();
        BigDecimal saldoInicialConta = null;
        BigDecimal saldoReceita = null;
        BigDecimal saldoDespesa = null;
        
         try {
            Query query = entity.createQuery(
                    "select sum(co.valorInicial) from  Conta co " +
                            "where co.usuario.id = :idUsuario ");
            saldoInicialConta = (BigDecimal) query
                    .setParameter("idUsuario", idUsuario)
                    .getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
               entity.close();
        }
        
        if (saldoInicialConta == null){
            saldoInicialConta = BigDecimal.ZERO;
        }
        
        System.out.println("Saldo ContaInicial: " + saldoInicialConta);

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
        
        return saldoInicialConta.add(saldoReceita.subtract(saldoDespesa));
    }
    
    /*Retorna todos os registros por usuário*/
    public List<Relatorio> getPorcentagemLimiteGastoSubcategoria(long idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();

        /*Obtém a data atual
        java.util.Date data1 = new java.util.Date();
        java.sql.Date data = new java.sql.Date(data1.getTime());*/
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        System.out.println("Id usuario: " + idUsuario);

        List<Object[]> results = em.createQuery("select t3.id as idLimite, "
                + "t1.descSubcategoria as categoria, "
                + "sum(t2.valorParcela) as soma, "
                + "coalesce(sum(t2.valorParcela)/t3.valorMaximo,1) *100, "
                + "coalesce(t3.valorMaximo,sum(t2.valorParcela)),"
                + "t1.icone, "
                + "t1.categoriaDespesa.cor, "
                + "t1.id as idSubcategoria "
                + "from Subcategoria t1 "
                + "left join Despesa t2 "
                + "on t2.categoria.id = t1.id "
                + "left join LimiteSubcategoria t3 "
                + "on t3.subcategoria.id = t1.id and t3.usuario.id = :idUsuario "
                + "where (t1.usuario.id = :idUsuario or t1.usuario.id is null) "
                + "and t2.conta.usuario.id = :idUsuario "
                + "and month(t2.dtLancamento) = :mes "
                + "and year(t2.dtLancamento) = :ano "
                + "group by t1.id  "
                + "order by soma desc ")
                .setParameter("idUsuario", idUsuario)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();
        
        System.out.println("tamanho: " + results.size());

        List<Relatorio> valores = new ArrayList<>();

        for (Object[] result : results) {
            System.out.println(result[0] + " " + result[1] + " " + result[2]  + " " + result[3] + 
                    " " + result[4] + " " + result[5] + result[6]);
            Relatorio relatorio = new Relatorio();
            if (result[0] != null) {
                relatorio.setId((long) result[0]);
            }
            relatorio.setDescricao((String) result[1]);
            relatorio.setSaldo((BigDecimal) result[2]);
            BigDecimal porcentagem = (BigDecimal) result[3];
            relatorio.setPorcentagemReceita(porcentagem.setScale(2, RoundingMode.HALF_EVEN));
            relatorio.setValorMaximo((BigDecimal) result[4]);
            BigDecimal valorDisponivel = relatorio.getValorMaximo().subtract(relatorio.getSaldo());
            relatorio.setValorDisponivel(valorDisponivel.setScale(2,RoundingMode.HALF_EVEN));
            relatorio.setIcone((String) result[5]);
            relatorio.setCor((String) result[6]);
            relatorio.setIdSubcategoria((long) result[7]);
            valores.add(relatorio);
        }

        return valores;
    }

    public List<Relatorio> getPorcentagemLimiteGastoSubcategoriaGroup(long idUsuario, long idSubcategoria) {
        EntityManager em = JPAUtil.getEntityManager();
        System.out.println("Porcentagem Subbcategoria Agrupada:"+idSubcategoria);
        
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        List<Object[]> results = em.createQuery("select t3.id as idLimite, "
                + "t1.id as idSubcategoria, "
                + "t1.descSubcategoria as categoria, "
                + "sum(t2.valorParcela) as soma, "
                + "coalesce(sum(t2.valorParcela)/t3.valorMaximo,1) *100, "
                + "coalesce(t3.valorMaximo,sum(t2.valorParcela)),"
                + "t1.icone, "
                + "t1.categoriaDespesa.cor "
                + "from Subcategoria t1 "
                + "left join Despesa t2 "
                + "on t2.categoria.id = t1.id  "
                + "left join LimiteSubcategoria t3 "
                + "on t1.id = t3.subcategoria.id "
                + "where (t1.usuario.id = :idUsuario or t1.usuario.id = null) "
                + "and t2.conta.usuario.id = :idUsuario "
                + "and t1.id = :idSubcategoria "
                + "and month(t2.dtLancamento) = :mes "
                + "and year(t2.dtLancamento) = :ano "
                + "group by t1.id  "
                + "order by soma desc ")
                .setParameter("idUsuario", idUsuario)
                .setParameter("idSubcategoria", idSubcategoria)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();
        
        System.out.println("tamanho: " + results.size());

        List<Relatorio> valores = new ArrayList<>();

        for (Object[] result : results) {
            System.out.println(result[0] + " " + result[1] + " " + result[2]  + " " + result[3] + 
                    " " + result[4] + " " + result[5] + result[6] + result[7]);
            Relatorio relatorio = new Relatorio();
            if (result[0] != null) {
                relatorio.setId((long) result[0]);
            }
            if (result[1] != null) {
                relatorio.setIdSubcategoria((long) result[1]);
            }
            
            relatorio.setDescricao((String) result[2]);
            relatorio.setSaldo((BigDecimal) result[3]);
            BigDecimal porcentagem = (BigDecimal) result[4];
            relatorio.setPorcentagemReceita(porcentagem.setScale(2, RoundingMode.HALF_EVEN));
            relatorio.setValorMaximo((BigDecimal) result[5]);
            BigDecimal valorDisponivel = relatorio.getValorMaximo().subtract(relatorio.getSaldo());
            relatorio.setValorDisponivel(valorDisponivel.setScale(2,RoundingMode.HALF_EVEN));
            relatorio.setIcone((String) result[6]);
            relatorio.setCor((String) result[7]);
            valores.add(relatorio);
        }

        return valores;
    }
    
    public List<Relatorio> getPorcentagemLimiteGastoSubcategoriaByCategoriaGroup(long idUsuario, long idCategoria) {
        EntityManager em = JPAUtil.getEntityManager();
        
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        
        System.out.println("Porcentagem Subcategoria Agrupada By Categoria:"+idCategoria);
        List<Object[]> results = em.createQuery("select "
                + "t3.id as idLimite, "
                + "t1.id as idSubcategoria, "
                + "t1.descSubcategoria as categoria, "
                + "sum(t2.valorParcela) as soma, "
                + "coalesce(sum(t2.valorParcela)/t3.valorMaximo,1), "
                + "coalesce(t3.valorMaximo,sum(t2.valorParcela)), "
                + "t1.icone, "
                + "t1.categoriaDespesa.cor "
                + "from Subcategoria t1 "
                + "left join Despesa t2 "
                + "on t2.categoria.id = t1.id  "
                + "left join LimiteSubcategoria t3 "
                + "on t1.id = t3.subcategoria.id "
                + "where (t1.usuario.id = :idUsuario or t1.usuario.id = null) "
                + "and t2.conta.usuario.id = :idUsuario "
                + "and t1.categoriaDespesa.id = :idCategoria "
                + "and month(t2.dtLancamento) = :mes " 
                + "and year(t2.dtLancamento) = :ano "
                + "group by t1.id  "
                + "order by soma desc ")
                .setParameter("idUsuario", idUsuario)
                .setParameter("idCategoria", idCategoria)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();
        
        System.out.println("tamanho: " + results.size());

        List<Relatorio> valores = new ArrayList<>();

        for (Object[] result : results) {
            System.out.println(result[0] + " " + result[1] + " " + result[2]  + " " + result[3] + 
                    " " + result[4] + " " + result[5] + result[6] + result[7]);
            Relatorio relatorio = new Relatorio();
            if (result[0] != null) {
                relatorio.setId((long) result[0]);
            }
            if (result[1] != null) {
                relatorio.setIdSubcategoria((long) result[1]);
            }
            
            relatorio.setDescricao((String) result[2]);
            relatorio.setSaldo((BigDecimal) result[3]);
            BigDecimal porcentagem = (BigDecimal) result[4];
            relatorio.setPorcentagemReceita(porcentagem.setScale(2, RoundingMode.HALF_EVEN));
            relatorio.setValorMaximo((BigDecimal) result[5]);
            BigDecimal valorDisponivel = relatorio.getValorMaximo().subtract(relatorio.getSaldo());
            relatorio.setValorDisponivel(valorDisponivel.setScale(2,RoundingMode.HALF_EVEN));
            relatorio.setIcone((String) result[6]);
            relatorio.setCor((String) result[7]);
            valores.add(relatorio);
        }

        return valores;
    }
    
    public List<Relatorio> getPorcentagemCategoriaDespesa(long idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();

        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        System.out.println("Id usuario: " + idUsuario);
        /*Obtem o saldo total gasto*/
        LancamentoFacade lancamentoFacade = new LancamentoFacade();
        BigDecimal valorTotalGasto = lancamentoFacade.getValorGastoMes(idUsuario);
        
        System.out.println("Valor Total: " + valorTotalGasto);
        
        List<Object[]> results = em.createQuery("select "
                + "case when t3.id = 2 then :string "
                + "else t3.descCategoria end as descricao,"
                + "sum(t2.valorParcela), 100 * sum(t2.valorParcela)/:valorTotalGasto,"
                + "t1.categoriaDespesa.cor "
                + "from Subcategoria t1 "
                + "left join Despesa t2 "
                + "on t1.id = t2.categoria.id "
                + "left join CategoriaDespesa t3 "
                + "on t1.categoriaDespesa.id = t3.id "
                + "where t2.conta.usuario.id = :idUsuario "
                + "and MONTH(t2.dtLancamento) = :mes "
                + "and YEAR(t2.dtLancamento) = :ano "
                + "group by t3.id"
                ).setParameter("string", "Essenciais")
                .setParameter("valorTotalGasto", valorTotalGasto)
                .setParameter("idUsuario", idUsuario)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();
        
        
        System.out.println("tamanho: " + results.size());

        List<Relatorio> valores = new ArrayList<>();

        for (Object[] result : results) {
            //System.out.println(result[0] + " " + result[1] + " " + result[2]);
            Relatorio relatorio = new Relatorio();
            
            relatorio.setDescricao((String) result[0]);
            relatorio.setSaldo((BigDecimal) result[1]);
            BigDecimal porcentagem = (BigDecimal) result[2];
            relatorio.setPorcentagemDespesa(porcentagem.setScale(2, RoundingMode.HALF_EVEN));
            relatorio.setCor((String) result[3]);
            valores.add(relatorio);
        }

        return valores;
    }
     
    public List<Relatorio> getAnaliseMensalGastos(long idUsuario) {
        
        EntityManager em = JPAUtil.getEntityManager();

        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        BigDecimal saldoReceita = null;
        
        try {
                Query query = em.createQuery(
                        "select sum(r.valorParcela) from  Receita r " +
                                "where r.conta.usuario.id = :idUsuario "
                                + "and MONTH(r.dtLancamento) = :mes "
                                + "and YEAR(r.dtLancamento) = :ano "
                                + "group by MONTH(r.dtLancamento)");
                saldoReceita =  (BigDecimal) query
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
        
        if (saldoReceita == null){
            saldoReceita = BigDecimal.ZERO;
        }
        
        System.out.println("Id usuario: " + idUsuario);
        em = JPAUtil.getEntityManager();
        
        /*Obtem o saldo total gasto*/
        LancamentoFacade lancamentoFacade = new LancamentoFacade();
        BigDecimal valorTotalGasto = lancamentoFacade.getValorGastoMes(idUsuario);
        BigDecimal estiloVida = new BigDecimal("35");
        BigDecimal investimento = new BigDecimal("15");
        BigDecimal gastosEssencias = new BigDecimal("50");
        
        System.out.println("Valor Total: " + valorTotalGasto);
        
        List<Object[]> results = new ArrayList<>();
        
       // List<Object[]> results = em.createQuery("").getResultList;
        
       try {
        results = em.createQuery("select t3.id, t3.descCategoria, "
                + "sum(t2.valorParcela), "
                + "t4.porcentagem "
                + "from Subcategoria t1 "
                + "left join Despesa t2 "
                + "on t1.id = t2.categoria.id "
                + "left join CategoriaDespesa t3 "
                + "on t1.categoriaDespesa.id = t3.id "
                + "left join LimiteCategoriaDespesa t4 "
                + "on t3.id = t4.categoria.id and t2.conta.usuario.id = t4.usuario.id "
                + "where t2.conta.usuario.id = :idUsuario "
                + "and MONTH(t2.dtLancamento) = :mes "
                + "and YEAR(t2.dtLancamento) = :ano "
                + "group by t3.id"
                )
                .setParameter("idUsuario", idUsuario)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();
        
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            em.close();
        }

        System.out.println("tamanho: " + results.size());

        List<Relatorio> valores = new ArrayList<>();

        for (Object[] result : results) {
            System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3]);
            Relatorio relatorio = new Relatorio();
            
            relatorio.setId((long)result[0]);
            relatorio.setDescricao((String) result[1]);
            BigDecimal porcentagemCategoria = (BigDecimal) result[3];
            relatorio.setPorcentagemDespesa(porcentagemCategoria.divide(new BigDecimal("100.00")));  
            BigDecimal valorMaximo = saldoReceita
                    .multiply(porcentagemCategoria.setScale(2, RoundingMode.HALF_EVEN)
                    .divide(new BigDecimal("100.00")));
            relatorio.setValorMaximo(valorMaximo.setScale(2, RoundingMode.HALF_EVEN));
            BigDecimal valorGasto = (BigDecimal) result[2];
            relatorio.setValorGasto(valorGasto);
            
            BigDecimal porcentagem;
            if(valorMaximo.compareTo(BigDecimal.ZERO) != 0){
                porcentagem = valorGasto
                        .divide(valorMaximo,8,BigDecimal.ROUND_HALF_EVEN);
            }else{
                porcentagem = BigDecimal.ZERO;
            }
            relatorio.setPorcentagem(porcentagem.setScale(2,RoundingMode.HALF_EVEN));
            
            /*Verifica se o valor gasto está dentro do limite estimado*/
            if(valorGasto.compareTo(valorMaximo) == -1){
                relatorio.setFlag(false);
            }else{
                relatorio.setFlag(true);
            }
            
            valores.add(relatorio);
        }
        
        System.out.println(valores.size());
        
        if(valores.size() < 3){
            /*Caso não exista despesas cadastradas pra uma categoria*/
            List<Object[]> dadosCategoria = null;
            if(valores.size() == 2){
                /*Encontrar qual categoria está faltando*/
                int total = (int)valores.get(0).getId() + (int)valores.get(1).getId();
                System.out.println("Total: " + total);

                switch (total){
                    case 3:
                        /*Se o resultado é 3, falta a categoria investimento*/
                        /*Busca no Banco a porcentagem e os dados da categoria*/
                        em = JPAUtil.getEntityManager();
                        try{
                            dadosCategoria = em.createQuery("select ca.id, ca.descCategoria, l.porcentagem "
                                + "from LimiteCategoriaDespesa l "
                                + "right join l.categoria ca "
                                + "where ca.id = :id and l.usuario.id = :idUsuario ")
                                    /*l.usuario.id = :idUsuario and */
                                    .setParameter("idUsuario", idUsuario)
                                    .setParameter("id", (long) 3)
                                    .getResultList();

                        }catch(Exception e){
                            System.out.println("BUSCA RECEITAS: " + e.getMessage());
                        }finally{
                            em.close();
                            }
                        break;
                    case 4:
                        /*Se o resultado é 4, falta a categoria gastos essenciais*/
                        /*Busca no Banco a porcentagem e os dados da categoria*/
                        em = JPAUtil.getEntityManager();
                        try{
                            dadosCategoria = em.createQuery("select ca.id, ca.descCategoria, l.porcentagem "
                                + "from LimiteCategoriaDespesa l "
                                + "right join l.categoria ca "
                                + "where ca.id = :id and l.usuario.id = :idUsuario ")
                                    /*l.usuario.id = :idUsuario and */
                                    .setParameter("idUsuario", idUsuario)
                                    .setParameter("id", (long) 2)
                                    .getResultList();

                        }catch(Exception e){
                            System.out.println("BUSCA RECEITAS: " + e.getMessage());
                        }finally{
                            em.close();
                        }
                        break;
                    case 5:
                        /*Se o resultado é 5, falta a categoria estilo de vida*/
                        /*Busca no Banco a porcentagem e os dados da categoria*/
                        em = JPAUtil.getEntityManager();
                        try{
                            dadosCategoria = em.createQuery("select ca.id, ca.descCategoria, l.porcentagem "
                                + "from LimiteCategoriaDespesa l "
                                + "right join l.categoria ca "
                                + "where ca.id = :id and l.usuario.id = :idUsuario ")
                                    /*l.usuario.id = :idUsuario and */
                                    .setParameter("idUsuario", idUsuario)
                                    .setParameter("id", (long) 1)
                                    .getResultList();

                        }catch(Exception e){
                            System.out.println("BUSCA RECEITAS: " + e.getMessage());
                        }finally{
                            em.close();
                        }
                        break;
                }
                
                for (Object[] result : dadosCategoria) {
                    System.out.println(result[0] + " " + result[1] + " " + result[2]);
                    Relatorio relatorio = new Relatorio();

                    relatorio.setId((long)result[0]);
                    relatorio.setDescricao((String) result[1]);
                    BigDecimal porcentagemCategoria = (BigDecimal)result[2];
                    BigDecimal valorMaximo = saldoReceita
                            .multiply(porcentagemCategoria.setScale(2, RoundingMode.HALF_EVEN)
                            .divide(new BigDecimal("100.00")));
                    relatorio.setValorMaximo(valorMaximo.setScale(2, RoundingMode.HALF_EVEN));
                    relatorio.setValorGasto(BigDecimal.ZERO);
                    relatorio.setPorcentagemDespesa(porcentagemCategoria);
                    
                    BigDecimal porcentagem;
                    if(valorMaximo.compareTo(BigDecimal.ZERO) != 0){
                        porcentagem = relatorio.getValorGasto()
                                .divide(valorMaximo,8,BigDecimal.ROUND_HALF_EVEN)
                                .multiply(new BigDecimal("100.00"));
                    }else{
                        porcentagem = BigDecimal.ZERO;
                    }
            relatorio.setPorcentagem(porcentagem.setScale(2,RoundingMode.HALF_EVEN));

                    /*Verifica se o valor gasto está dentro do limite estimado*/
                    if(relatorio.getValorGasto().compareTo(relatorio.getValorMaximo()) == -1){
                        relatorio.setFlag(false);
                    }else{
                        relatorio.setFlag(true);
                    }

                    valores.add(relatorio);
                }
             /*Busca as categorias que não possui despesas cadastradas*/
            }else if(valores.size() == 1){
                /*Busca no Banco a porcentagem e os dados da categoria*/
                        em = JPAUtil.getEntityManager();
                        try{
                            dadosCategoria = em.createQuery("select ca.id, ca.descCategoria, l.porcentagem "
                                + "from LimiteCategoriaDespesa l "
                                + "right join l.categoria ca "
                                + "where ca.id != :id and l.usuario.id = :idUsuario ")
                                    /*l.usuario.id = :idUsuario and */
                                    .setParameter("idUsuario", idUsuario)
                                    .setParameter("id", valores.get(0).getId())
                                    .getResultList();

                    }catch(Exception e){
                        System.out.println("BUSCA RECEITAS: " + e.getMessage());
                    }finally{
                        em.close();
                    }
                        
                    for (Object[] result : dadosCategoria) {
                        System.out.println(result[0] + " " + result[1] + " " + result[2]);
                        Relatorio relatorio = new Relatorio();

                        relatorio.setId((long)result[0]);
                        relatorio.setDescricao((String) result[1]);
                        BigDecimal porcentagemCategoria = (BigDecimal)result[2];
                        BigDecimal valorMaximo = saldoReceita
                                .multiply(porcentagemCategoria.setScale(2, RoundingMode.HALF_EVEN)
                                .divide(new BigDecimal("100.00")));
                        relatorio.setValorMaximo(valorMaximo.setScale(2, RoundingMode.HALF_EVEN));
                        relatorio.setValorGasto(BigDecimal.ZERO);
                        relatorio.setPorcentagemDespesa(porcentagemCategoria);

                        /*Verifica se o valor gasto está dentro do limite estimado*/
                        if(relatorio.getValorGasto().compareTo(relatorio.getValorMaximo()) == -1){
                            relatorio.setFlag(false);
                        }else{
                            relatorio.setFlag(true);
                        }

                        valores.add(relatorio);
                    }
             /*Caso não exista nenhuma despesa cadastrada*/
            }else {
                /*Busca no Banco a porcentagem e os dados da categoria*/
                        em = JPAUtil.getEntityManager();
                        try{
                            dadosCategoria = em.createQuery("select ca.id, ca.descCategoria, l.porcentagem "
                                + "from LimiteCategoriaDespesa l "
                                + "right join l.categoria ca "
                                + "where l.usuario.id = :idUsuario ")
                                    .setParameter("idUsuario", idUsuario)
                                    .getResultList();

                    }catch(Exception e){
                        System.out.println("BUSCA RECEITAS: " + e.getMessage());
                    }finally{
                        em.close();
                    }
                        
                    for (Object[] result : dadosCategoria) {
                        System.out.println(result[0] + " " + result[1] + " " + result[2]);
                        Relatorio relatorio = new Relatorio();

                        relatorio.setId((long)result[0]);
                        relatorio.setDescricao((String) result[1]);
                        BigDecimal porcentagemCategoria = (BigDecimal)result[2];
                        BigDecimal valorMaximo = saldoReceita
                                .multiply(porcentagemCategoria.setScale(2, RoundingMode.HALF_EVEN)
                                .divide(new BigDecimal("100.00")));
                        relatorio.setValorMaximo(valorMaximo.setScale(2, RoundingMode.HALF_EVEN));
                        relatorio.setValorGasto(BigDecimal.ZERO);
                        relatorio.setPorcentagemDespesa(porcentagemCategoria);

                        /*Verifica se o valor gasto está dentro do limite estimado*/
                        if(relatorio.getValorGasto().compareTo(relatorio.getValorMaximo()) == -1){
                            relatorio.setFlag(false);
                        }else{
                            relatorio.setFlag(true);
                        }

                        valores.add(relatorio);
                    }
            }
        }

        return valores;
    }
    
    public List<Relatorio> getSaldoSubcategoriasByConta (long idUsuario, long idConta, int mes) {
        EntityManager em = JPAUtil.getEntityManager();

        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        /*Obtem o saldo total gasto em um mês*/
        LancamentoFacade lancamentoFacade = new LancamentoFacade();
        if(idUsuario == 2) {
            idUsuario = 52;
        }
        BigDecimal valorTotalGasto = lancamentoFacade.getValorGastoMes(idUsuario, mes);
        
        System.out.println("Valor Total: " + valorTotalGasto);
        
        List<Object[]> results = em.createQuery("select sum(d.valorParcela), count(d), "
                + "d.categoria.id, s.descSubcategoria, "
                + "s.icone, "
                + "s.categoriaDespesa.descCategoria, "
                + "s.categoriaDespesa.cor, "
                + "d.id as idDespesa, "
                + "s.id as idSubcategoria "
                + "from Despesa d "
                + "left join d.categoria s "
                + "where d.conta.id = :idConta "
                + "and MONTH(d.dtLancamento) = :mes "
                + "and YEAR(d.dtLancamento) = :ano "
                + "group by d.categoria"
                )
                .setParameter("idConta", idConta)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();
        
        
        System.out.println("tamanho: " + results.size());

        List<Relatorio> valores = new ArrayList<>();

        for (Object[] result : results) {
            System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3] + result[4]);
            Relatorio relatorio = new Relatorio();
            
            /*Obtém valor gasto na subcategoria*/
            BigDecimal valorGastoSub = (BigDecimal) result[0];
            relatorio.setSaldo(valorGastoSub.setScale(2,RoundingMode.HALF_EVEN));
            System.out.println("ValorGasto: " + valorGastoSub);
            BigDecimal porcentagem = null;
            relatorio.setCategoria((String) result[5]);
            /*Obtém a porcentagem de gasto da subcategoria, com relação ao total gasto*/
            if (valorTotalGasto.compareTo(BigDecimal.ZERO) == 1){
                porcentagem = valorGastoSub.divide(valorTotalGasto,8,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(new BigDecimal("100.00"));
            }else{
                porcentagem = BigDecimal.ZERO;
            }
            
            relatorio.setPorcentagemDespesa(porcentagem.setScale(2, RoundingMode.HALF_EVEN));
            System.out.println("Porcentagem: " + relatorio.getPorcentagemDespesa());
            
            /*Obtém a quantidade de registros na subcategoria*/
            Long qtdeDespesas = (Long) result[1];
            relatorio.setQtde(qtdeDespesas.intValue());
            System.out.println("Quantidade de despesas: " + relatorio.getQtde());
            
            relatorio.setId((long) result[2]);
            relatorio.setDescricao((String) result[3]);
            System.out.println("Subcategoria: " + relatorio.getDescricao());
            
            relatorio.setIcone((String) result[4]);
            relatorio.setCor((String) result[6]);
            relatorio.setIdDespesa((long) result[7]);
            relatorio.setIdSubcategoria((long) result[8]);
            relatorio.setMes(mes);

            valores.add(relatorio);
        }

        return valores;
    }
    
    public BigDecimal getPorcentagem(BigDecimal valorLimite, long idUsuario){

        EntityManager em = JPAUtil.getEntityManager();
        
        System.out.println("Valor Limite: " + valorLimite);
        
        /*A partir da data atual, obtém o ano corrente*/
        Date dataAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        System.out.println("ano: " + ano +  " mes: " + mes);
        
        /*Obtém valor da receita do mês atual*/
        LancamentoFacade lancamentoFacade = new LancamentoFacade();
        BigDecimal totalReceita = lancamentoFacade.getTotalReceitaMes(idUsuario);
       
        System.out.println("Valor Total Receita: " + totalReceita);
        
        BigDecimal porcentagem = valorLimite.divide(totalReceita,8,BigDecimal.ROUND_HALF_EVEN)
                .multiply(new BigDecimal("100.00")).setScale(2,RoundingMode.HALF_EVEN);

        System.out.println("Porcentagem: " + porcentagem);
       
        return porcentagem;
    }
    
    public Relatorio getSituacaoGeral(long idUsuario){
        
        Relatorio relatorio = new Relatorio();
        
        /*Obtém valor da receita do mês atual*/
        LancamentoFacade lancamentoFacade = new LancamentoFacade();
        BigDecimal totalReceita = lancamentoFacade.getTotalReceitaMes(idUsuario);
       
        System.out.println("Valor Total Receita: " + totalReceita);
        
        /*Obtém o valor total gasto*/
        BigDecimal valorTotalGasto = lancamentoFacade.getValorGastoMes(idUsuario);
        
        System.out.println("Valor Total Gasto: " + valorTotalGasto);
        
         BigDecimal porcentagem;
        /*Calcula da porcentagem da despesa sob a receita*/
        if(totalReceita.compareTo(BigDecimal.ZERO) == 0){
            porcentagem = BigDecimal.ZERO;
        }else{
            porcentagem = valorTotalGasto.multiply(new BigDecimal("100.00"))
                .divide(totalReceita,8,BigDecimal.ROUND_HALF_EVEN)
                .setScale(2,RoundingMode.HALF_EVEN);
        }
      
        System.out.println("porcentagem: " + porcentagem);
        
        relatorio.setPorcentagemDespesa(porcentagem);
        relatorio.setValorDisponivel(totalReceita);
        relatorio.setValorGasto(valorTotalGasto);
                
        return relatorio;
    }
    
    public BigDecimal getValorParcela(BigDecimal valorTotal, int qtdeParcelas){
        BigDecimal valorParcela = new BigDecimal("0");
        
        /*Multiplica por 100 pra tirar as casas decimais*/
        BigDecimal valorRecebido = valorTotal.multiply(new BigDecimal("100"));
        BigDecimal restoDivisao = valorRecebido.remainder(new BigDecimal(Integer.toString(qtdeParcelas)));
        
        BigDecimal auxParcela = valorRecebido.divide
                (new BigDecimal(Integer.toString(qtdeParcelas)),8, RoundingMode.HALF_EVEN);
        
        valorParcela = auxParcela.divide(
                    new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        
        return valorParcela;
    }
    
    public BigDecimal getValorParcelaAlterada(String tipo, long id, BigDecimal valorTotal, int qtdeParcelas){
        BigDecimal valorParcela = new BigDecimal("0");
        
        EntityManager entity = JPAUtil.getEntityManager();
        
        try{
        if (tipo.equalsIgnoreCase("despesa")){
            Despesa despesa = entity.find(Despesa.class, id);
            BigDecimal valorPago = this.obterValorPago(despesa);
            BigDecimal valorRestante = valorTotal.subtract(valorPago);
            int qtdePacelasRestantes = qtdeParcelas - despesa.getNumParcela()+1;
            valorParcela = this.getValorParcela(valorRestante, qtdePacelasRestantes);
            
        }else{
            Receita receita = entity.find(Receita.class, id);
            BigDecimal valorPago = this.obterValorPago(receita);
            BigDecimal valorRestante = valorTotal.subtract(valorPago);
            int qtdePacelasRestantes = qtdeParcelas - receita.getNumParcela()+1;
            valorParcela = this.getValorParcela(valorRestante, qtdePacelasRestantes);
        }
        }catch(Exception e ){
            e.printStackTrace();
        }finally{
            entity.close();
        }
        
        System.out.println(valorParcela);
        
        return valorParcela;
    }
    
     private BigDecimal obterValorPago(Despesa despesa){

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
    
    private BigDecimal obterValorPago(Receita receita){

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
}
