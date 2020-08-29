package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="tb003_tipo_conta")
public class TipoConta implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="tb003_id")
    private long id;
    
    @Column(name="tb003_descricao",length=20)
    private String descTipoConta;
    
    public TipoConta() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescTipoConta() {
        return descTipoConta;
    }

    public void setDescTipoConta(String descTipoConta) {
        this.descTipoConta = descTipoConta;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoConta other = (TipoConta) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
       
    
}
