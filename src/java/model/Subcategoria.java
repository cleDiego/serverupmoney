
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
@Table (name="tb007_subcategoria")
public class Subcategoria implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tb007_id_subcategoria")
    private long id;
    
    @Column(name="tb007_descricao_subcategoria", length=50)
    private String descSubcategoria;
    
    @Column(name="tb007_icone", length=50)
    private String icone;
    
    @ManyToOne()
    @JoinColumn(name = "tb007_categoria_despesa_id")
    private CategoriaDespesa categoriaDespesa;
    
    @ManyToOne()
    @JoinColumn(name = "tb007_usuario_id")
    private Usuario usuario;
    
    public Subcategoria(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescSubcategoria() {
        return descSubcategoria;
    }

    public void setDescSubcategoria(String descSubcategoria) {
        this.descSubcategoria = descSubcategoria;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public CategoriaDespesa getCategoriaDespesa() {
        return categoriaDespesa;
    }

    public void setCategoriaDespesa(CategoriaDespesa categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Subcategoria other = (Subcategoria) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    } 
    
}
