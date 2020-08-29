/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb001_usuario")
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tb001_id")
    private long id;
    
    @Column(name="tb001_nome_completo", length = 150)
    private String nomeCompleto;
    
    @Column(name="tb001_email", length = 50)
    private String email;
    
    @Column(name="tb001_foto", length=100)
    private String foto;
    
    @Column(name="tb001_senha",length=255)
    private String senha;
    
    @Column(name="tb001_data_nascimento")
    private LocalDate dtNascimento;
    
    @Column(name="tb001_genero")
    private char genero;


    public Usuario() {
        
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
    
    /*
    public List<CategoriaReceita> getCategoriasReceita() {
        return categoriasReceita;
    }

    public void setCategoriasReceita(List<CategoriaReceita> categoriasReceita) {
        this.categoriasReceita = categoriasReceita;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "\n\tid=" + id + ", "
                + "\n\tnomeCompleto=" + nomeCompleto + ", "
                + "\n\temail=" + email + ", "
                + "\n\tfoto=" + foto + ", "
                + "\n\tsenha=" + senha + ", "
                + "\n\tdtNascimento=" + dtNascimento + ", "
                + "\n\tgenero=" + genero + ", "
                + "\n\t}";
    }
 
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Usuario other = (Usuario) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
