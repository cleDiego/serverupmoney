/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author amand
 */
public class Relatorio {
    
    private long id;
    private long idDespesa;
    private long idSubcategoria;
    private String descricao;
    private String categoria;
    private String icone;
    private String cor;
    private BigDecimal saldo;
    private BigDecimal porcentagemDespesa;
    private BigDecimal valorMaximo;
    private BigDecimal porcentagemReceita;
    private BigDecimal porcentagem;
    private BigDecimal valorGasto;
    private BigDecimal valorDisponivel;
    private int mes;
    private int qtde;
    private boolean flag;

    public Relatorio() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(long idDespesa) {
        this.idDespesa = idDespesa;
    }
    
    public long getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(long idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public BigDecimal getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(BigDecimal porcentagem) {
        this.porcentagem = porcentagem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getPorcentagemDespesa() {
        return porcentagemDespesa;
    }

    public void setPorcentagemDespesa(BigDecimal porcentagemDespesa) {
        this.porcentagemDespesa = porcentagemDespesa;
    }

    public BigDecimal getPorcentagemReceita() {
        return porcentagemReceita;
    }

    public void setPorcentagemReceita(BigDecimal porcentagemReceita) {
        this.porcentagemReceita = porcentagemReceita;
    }

    public BigDecimal getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(BigDecimal valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public BigDecimal getValorDisponivel() {
        return valorDisponivel;
    }

    public void setValorDisponivel(BigDecimal valorDisponivel) {
        this.valorDisponivel = valorDisponivel;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }
    
    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }
    
    
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public BigDecimal getValorGasto() {
        return valorGasto;
    }

    public void setValorGasto(BigDecimal valorGasto) {
        this.valorGasto = valorGasto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Relatorio other = (Relatorio) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }  
}
