/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.UsuarioDAO;
import br.com.amociclismo.entity.Usuario;
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
    
    private UsuarioDAO usuarioDAO;
    /**
     * Construtor
     */
    public UsuarioBean() {
        usuario =  new Usuario();
        
        usuarioDAO =  new UsuarioDAO();
    }
    
    /**
     * Metodo que salva usuario
     */
    public void salvarUsuario(){
        
        usuarioDAO.inserirUsuario(usuario);
        
    }
    
    //Getters and Setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
}
