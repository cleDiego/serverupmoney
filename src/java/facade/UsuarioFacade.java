/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import dao.UsuarioDAO;
import java.util.List;
import model.Avatar;
import model.NovaSenha;
import model.Usuario;

public class UsuarioFacade {
    
    private UsuarioDAO usuarioDAO;

    public UsuarioFacade() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public Usuario autenticarLogin(Usuario usuario){
        return usuarioDAO.validaLogin(usuario.getEmail(), usuario.getSenha());
    }
    
    public Usuario inserirNovoUsuario(Usuario usuario, String base64){
        return usuarioDAO.inserirUsuario(usuario);
    }
    
    public Usuario atualizarUsuario(Usuario usuario, String base64){
        return usuarioDAO.alterarUsuario(usuario);
    }
    
    public Usuario getUsuarioById(long id) {
        return usuarioDAO.getUsuarioById(id);
    }

    public boolean recuperarSenha(String email){
        return usuarioDAO.recuperarSenha(email);
    }
    
    public boolean alterarSenha(NovaSenha novaSenha){
        return usuarioDAO.alterarSenha(novaSenha);
    }
    
    public List<Avatar> getAvatares(){
        return usuarioDAO.getAvatares();
    }
    
}
