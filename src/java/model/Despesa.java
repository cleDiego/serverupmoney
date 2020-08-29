/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="tb005_despesa")
public class Despesa implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="tb005_id")
    private long id;
    
    @Column(name="tb005_descricao_despesa",length=50)
    private String descDespesa;
    
    @Column(name="tb005_dt_lancamento")
    private LocalDate dtLancamento;
    
    @Column(name="tb005_valor_total_despesa")
    private BigDecimal valorTotalDespesa;
    
    @Column(name="tb005_repeticao")
    private boolean repeticao;
    
    @Column (name="tb005_numero_parcela")
    private int numParcela;
    
    @Column(name="tb005_qtde_parcela")
    private int qtdeParcelas;
    
    @Column(name="tb005_id_repeticao")
    private long idRepeticao;
    
    @Column(name= "tb005_valor_parcela")
    private BigDecimal valorParcela;
    
    @ManyToOne
    @JoinColumn(name = "tb005_tipo_repeticao_id")
    private TipoRepeticao tipoRepeticao;
    
    @ManyToOne()
    @JoinColumn(name = "tb005_conta_id")
    private Conta conta;
    
    @ManyToOne()
    @JoinColumn(name = "tb005_subcategoria_id")
    private Subcategoria categoria;

    public Despesa() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescDespesa() {
        return descDespesa;
    }

    public void setDescDespesa(String descDespesa) {
        this.descDespesa = descDespesa;
    }

    public LocalDate getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(LocalDate dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public BigDecimal getValorTotalDespesa() {
        return valorTotalDespesa;
    }

    public void setValorTotalDespesa(BigDecimal valorTotalDespesa) {
        this.valorTotalDespesa = valorTotalDespesa;
    }

    public boolean isRepeticao() {
        return repeticao;
    }

    public void setRepeticao(boolean repeticao) {
        this.repeticao = repeticao;
    }

    public int getNumParcela() {
        return numParcela;
    }

    public void setNumParcela(int numParcela) {
        this.numParcela = numParcela;
    }

    public int getQtdeParcelas() {
        return qtdeParcelas;
    }

    public void setQtdeParcelas(int qtdeParcela) {
        this.qtdeParcelas = qtdeParcela;
    }

    public long getIdRepeticao() {
        return idRepeticao;
    }

    public void setIdRepeticao(long idRepeticao) {
        this.idRepeticao = idRepeticao;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public TipoRepeticao getTipoRepeticao() {
        return tipoRepeticao;
    }

    public void setTipoRepeticao(TipoRepeticao tipoRepeticao) {
        this.tipoRepeticao = tipoRepeticao;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Subcategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Subcategoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Despesa other = (Despesa) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Despesa{" + "id=" + id + ", descDespesa=" + descDespesa + ", dtLancamento=" + dtLancamento + ", valorTotalDespesa=" + valorTotalDespesa + ", repeticao=" + repeticao + ", numParcela=" + numParcela + ", qtdeParcelas=" + qtdeParcelas + ", idRepeticao=" + idRepeticao + ", valorParcela=" + valorParcela + ", tipoRepeticao=" + tipoRepeticao + ", conta=" + conta + ", categoria=" + categoria + '}';
    }
    
    
}
