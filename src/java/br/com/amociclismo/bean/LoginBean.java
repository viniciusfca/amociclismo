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
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author vinicius
 */
@ManagedBean(name = "loginMB")
@SessionScoped
@ViewScoped
public class LoginBean {
    
    private String username;
    private String senha;
    private String erroLogar;
    
    private boolean mostrarErro = false;
    
    /**
     * Construtor
     */
    public LoginBean() {
        this.username = "";
        this.senha = "";
        erroLogar = "";
        
        String tUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("user");
        String tSenha = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pass");

        if (tUser != null){
            this.username = tUser;
            this.senha = tSenha;
            doLogin();
        }
    }
    
    
    /**
     * Metodo que retorna usuario logado
     *
     * @return
     */
    public Usuario retornarUsuarioLogado() {
        //RequestContext.getCurrentInstance().execute("PF('dlgComunicado').show()");
        return Util.getUsuarioLogado();
    }
    
    /**
     * Meotodo que efetua login
     */
    public void doLogin(){
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        try{
         Usuario usuario  = usuarioDAO.getUsuarioByLogin(username, senha);
         
         if(usuario.getId() > 0){
             Util.colocarUsuarioSessao(usuario);
             Util.redirecionar(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/restrito/principal.jsf");
         }else{
             erroLogar = "Usuário ou senha incorreto.";
             mostrarErro = true;
             Util.redirecionar(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.jsf");
         }
         
        }catch(Exception e){
            Util.saveMessage("Falha ao logar: ", e.getMessage());
        }
        
    }
    
    /**
     * Metodo que realiza o logout
     */
    public void doLogout() {
        
        Util.retirarUsuarioSessao();
        Util.redirecionar(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getErroLogar() {
        return erroLogar;
    }

    public void setErroLogar(String erroLogar) {
        this.erroLogar = erroLogar;
    }

    public boolean isMostrarErro() {
        return mostrarErro;
    }

    public void setMostrarErro(boolean mostrarErro) {
        this.mostrarErro = mostrarErro;
    }
    
    
}
