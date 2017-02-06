/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.UsuarioDAO;
import br.com.amociclismo.entity.Usuario;
import br.com.amociclismo.util.Util;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name="usuarioMB")
@ViewScoped
public class UsuarioBean {
    
    private Usuario usuario;
    
    private String validaSenha;
    private String validaEmail;
    
    private UsuarioDAO usuarioDAO;
    /**
     * Construtor
     */
    public UsuarioBean() {
        usuario =  new Usuario();
        
        usuarioDAO =  new UsuarioDAO();
        
        System.out.println("IP: "+Util.getIp());
    }
    
    
    
    public String validarCampos(){
        String msg = "";
        
        if(!Util.isCPF(usuario.getCpf())){
            msg += "CPF informado é inválido!";
        }
        
        if(Util.isEmailValido(usuario.getEmail())){
            
            if(!usuario.getEmail().trim().equals(validaEmail.trim())){
                msg += "Os e-mails não são iguais.";
            }
        }else{
            msg += "E-mail informado é inválido.";
                    
        }
        
        return msg;
    }
    
    /**
     * Metodo que salva usuario
     */
    public void salvarUsuario(){
        String msg = validarCampos();
        if(msg.equals("")){
            usuario =  usuarioDAO.inserirUsuario(usuario);
        }else{
            Util.saveMessage("Atenção", msg);
        }
        
        
        if(usuario.getId() > 0 ){
            Util.saveMessage("Sucesso!", "Cadastro efetuado com sucesso.");
        }else{
            Util.saveMessage("Atenção", "Falha ao efetuar cadastro.");
        }
        
    }
    
    //Getters and Setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getValidaSenha() {
        return validaSenha;
    }

    public void setValidaSenha(String validaSenha) {
        this.validaSenha = validaSenha;
    }

    public String getValidaEmail() {
        return validaEmail;
    }

    public void setValidaEmail(String validaEmail) {
        this.validaEmail = validaEmail;
    }
    
    
    
}
