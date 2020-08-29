/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb004_tipo_repeticao")
public class TipoRepeticao implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="tb004_id")
    private long id;
    
    @Column(name="tb004_descricao_tipo_repeticao",length=20)
    private String descTipoRepeticao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescTipoRepeticao() {
        return descTipoRepeticao;
    }

    public void setDescTipoRepeticao(String descTipoRepeticao) {
        this.descTipoRepeticao = descTipoRepeticao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TipoRepeticao other = (TipoRepeticao) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
}
