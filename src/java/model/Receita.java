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
@Table(name="tb008_receita")
public class Receita implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tb008_id_receita")
    private long id;
    
    @Column(name="tb008_descricao_receita",length=50)
    private String descReceita;
    
    @Column(name="tb008_valor_total_receita")
    private BigDecimal valorTotalReceita;
    
    @Column(name="tb008_data_lancamento")
    private LocalDate dtLancamento;
    
    @Column(name="tb008_repeticao")
    private boolean repeticao;
    
    @Column(name="tb008_numero_parcela")
    private int numParcela;
    
    @Column(name="tb008_qtde_parcelas")
    private int qtdeParcelas;
    
    @Column(name="tb008_valor_parcela")
    private BigDecimal valorParcela;
    
    @Column(name="tb008_id_repeticao")
    private long idRepeticao;
    
    @ManyToOne()
    @JoinColumn(name = "tb008_tipo_repeticao_id")
    private TipoRepeticao tipoRepeticao;
    
    @ManyToOne()
    @JoinColumn(name = "tb008_conta_id")
    private Conta conta;
    
    @ManyToOne()
    @JoinColumn(name = "tb008_categoria_id")
    private CategoriaReceita categoria;
 
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescReceita() {
        return descReceita;
    }

    public void setDescReceita(String descReceita) {
        this.descReceita = descReceita;
    }

    public BigDecimal getValorTotalReceita() {
        return valorTotalReceita;
    }

    public void setValorTotalReceita(BigDecimal valorTotalReceita) {
        this.valorTotalReceita = valorTotalReceita;
    }

    public LocalDate getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(LocalDate dtLancamento) {
        this.dtLancamento = dtLancamento;
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

    public void setQtdeParcelas(int qtdeParcelas) {
        this.qtdeParcelas = qtdeParcelas;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public long getIdRepeticao() {
        return idRepeticao;
    }

    public void setIdRepeticao(long idRepeticao) {
        this.idRepeticao = idRepeticao;
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

    public CategoriaReceita getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaReceita categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Receita other = (Receita) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
}
