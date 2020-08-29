/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import facade.LimiteFacade;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import model.Avatar;
import model.NovaSenha;
import util.AES;
import model.Usuario;
import sun.misc.BASE64Decoder;
import util.JPAUtil;
import util.JavaMail;
import util.PasswordGenerator;
import ws.exception.BancoDadosException;

/**
 *
 * @author amand
 */
public class UsuarioDAO {
    
    public Usuario inserirUsuario(Usuario usuario) {
        
        existEmail(usuario);
        validaUsuario(usuario);
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try{
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit(); 
            
        }catch(Exception e){
            em.getTransaction().rollback();
            System.out.println("INSERT: Exceção em UsuarioDAO.inserirUsuario: " + e.getMessage());
            
        }finally{
            em.close();
        }
        
        /*Adiciona Porcentagens de CategoriaDespesas Padrões ao Perfil do Usuario*/
        LimiteFacade limiteFacade = new LimiteFacade();
        limiteFacade.inserirValorPadrao(usuario);
        
        return usuario;
    }
    
    public Usuario validaLogin(String email, String senha ) {
        EntityManager em = JPAUtil.getEntityManager();
        try{
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email and u.senha = :senha", 
                    Usuario.class
            );
            return query.setParameter("email",email)
                              .setParameter("senha", AES.encrypt(senha.trim(), email))
                              .getSingleResult();
            
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("VALIDA: Exceção em UsuarioDAO.validaLogin: "+e.getMessage());
            return null;
        }
    }
    
    public Usuario getUsuarioById(long id){
        Usuario usuario = new Usuario();
        EntityManager em = JPAUtil.getEntityManager();
        try{
            usuario = em.find(Usuario.class, id);
        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("FIND: Exceção em UsuarioDAO.getUsuarioById: " + e.getMessage());
        }finally {
            em.close();
        }
        return usuario;
    }
        
    public Usuario getUsuariobyEmail(String email){
        EntityManager em = JPAUtil.getEntityManager();
        Usuario u = null;
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            u = query.setParameter("email",email)
                                .getSingleResult();
        }catch (NoResultException e) {
            /* Está dando exceção quando não encontra resultado */
        }
        return u;
    }

    public Usuario alterarUsuario(Usuario usuario){
        
        validaUsuarioAtualizar(usuario);
        //usuario.setFoto(decodeToImage(base64, usuario.getId()));
        
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
            System.out.println("UPDATE: Exceção em UsuarioDAO.alterarUsuario: " + e.getMessage());

        }finally {
            em.close();
        }
        return usuario;
    }
    
    public boolean recuperarSenha(String email){
        
        Usuario usuario = getUsuariobyEmail(email);
        if(usuario == null) {
            return false;
        }else{
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(false)
                    .build();
            String newpassword = passwordGenerator.generate(6);
            System.out.println("Recuperando a senha:"+newpassword);
            usuario.setSenha(AES.encrypt(newpassword, usuario.getEmail()));
            alterarUsuario(usuario);
            
            JavaMail jm = new JavaMail();
            //sendMail(String to, String subject, String bodyHTML)
            jm.sendMail(email, "Recuração de Senha - Não Responda UpMoney",
                "Olá, você solicitou a recuperação de senha, utilize a senha abaixo para acessar sua conta:"
                    + "<br><b>"+newpassword+"</b>"
                    + "<br><br>Você poderá alterar se desejar quando logar no aplicativo!"
                    + "<br><br><br>Atenciosamente,"
                    + "<br>Equipe UpMoney"
            );
            
        }
        return true;
    }
    
    public boolean alterarSenha(NovaSenha novaSenha){
        
        if(novaSenha.getNovaSenha().equals(novaSenha.getConfirmaSenha())) {
            Usuario usuario = getUsuarioById(novaSenha.getIdUsuario());
            usuario.setSenha(novaSenha.getSenhaAtual());

            Usuario usuarioAutenticado = validaLogin(usuario.getEmail(), usuario.getSenha());

            if(usuarioAutenticado != null) {
                
                usuario.setSenha(AES.encrypt(novaSenha.getNovaSenha(), usuario.getEmail()));
                Usuario usuarioAlterado = alterarUsuario(usuario);
                if(usuarioAlterado != null) {
                   return true; 
                }else {
                    return false;
                }
                
            }else{
                return false;
            }
        }else {
            return false;
        }
        
    }
    
    private void existEmail(Usuario usuario){
       /* Valida se o email a cadastrar já Existe */
        if(getUsuariobyEmail(usuario.getEmail()) != null){
            throw new BancoDadosException("E-mail já cadastrado");
        } 
    }
    
    private void validaUsuario(Usuario usuario){
        
        /*Valida se o e-mail é válido*/
        try {
            InternetAddress email = new InternetAddress(usuario.getEmail());
            email.validate();
        } catch (AddressException e) {
            throw new BancoDadosException("E-mail inválido");
        }
        
        /*Valida Gênero se está nos valores M e F*/
        if(Character.isWhitespace(usuario.getGenero()) || (usuario.getGenero() != 'M' && usuario.getGenero() != 'F') ) {
            throw new BancoDadosException("Gênero do usuário inválido(F,M)");
        }
        /*Criptografa a senha*/
        usuario.setSenha(AES.encrypt(usuario.getSenha(), usuario.getEmail()));
    }
    
    private void validaUsuarioAtualizar(Usuario usuario){
        Usuario usuarioBanco = getUsuarioById(usuario.getId());

        /*Valida se o e-mail é válido*/
        try {
            InternetAddress email = new InternetAddress(usuario.getEmail());
            email.validate();
        } catch (AddressException e) {
            throw new BancoDadosException("E-mail inválido");
        }
        
        /*Valida Gênero se está nos valores M e F*/
        if(Character.isWhitespace(usuario.getGenero()) || (usuario.getGenero() != 'M' && usuario.getGenero() != 'F') ) {
            throw new BancoDadosException("Gênero do usuário inválido(F,M)");
        }
        
        if(usuario.getSenha() == null) {
            usuario.setSenha(usuarioBanco.getSenha());
        }
        
        
    }
    
    public List<Avatar> getAvatares(){
        EntityManager em = JPAUtil.getEntityManager();
        List<Avatar> avatares = new ArrayList<>();
        try{
            TypedQuery<Avatar> query = em.createQuery(
                "SELECT av FROM Avatar av", Avatar.class);
            avatares = query.getResultList();
        }catch(Exception e){
            System.out.println("BUSCA AVATARES: " + e.getMessage());
        }finally{
            em.close();
        }
        return avatares;
    }
    
    public static String decodeToImage(String imageString, long idUsuario) {
        
        String url = null;
        if(imageString.length() > 0) {
            String[] parts = imageString.split(",");
            if(parts.length > 1) {
                imageString = parts[1];
            }

            BufferedImage image = null;
            byte[] imageByte;           

            try {
                BASE64Decoder decoder = new BASE64Decoder();
                imageByte = decoder.decodeBuffer(imageString);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(bis);
                url = "images/users/foto_"+idUsuario+".png";
                ImageIO.write(
                        image, 
                        "png", 
                        new File("C://Users/amand/OneDrive/Documents/NetBeansProjects/upmoney/web/"+url)
                );
                bis.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}
