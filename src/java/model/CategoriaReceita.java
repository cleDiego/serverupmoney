
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tb006_categoria_receita")
public class CategoriaReceita implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "tb006_id_categoria_receita")
    private long id;
    
    @Column(name = "tb006_descricao_categoria", length = 50)
    private String descCategoria;
    
    @Column(name = "tb006_icone", length=20)
    private String icone;
    
    @ManyToOne()
    @JoinColumn(name = "tb006_usuario_id")
    private Usuario usuario;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescCategoria() {
        return descCategoria;
    }

    public void setDescCategoria(String descCategoria) {
        this.descCategoria = descCategoria;
    }
    
    public String getIcone(){
        return this.icone;
    }
    
    public void setIcone(String icone){
        this.icone = icone;
    }
    
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
   
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CategoriaReceita other = (CategoriaReceita) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
