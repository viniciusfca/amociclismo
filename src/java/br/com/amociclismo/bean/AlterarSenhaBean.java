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
@ManagedBean(name="alterarSenhaMB")
@ViewScoped
public class AlterarSenhaBean {
    
    private Usuario usuario;

    private UsuarioDAO usuarioDAO;

    private String senhaNova;
    private String senhaConfirmar;
    private String senhaAtual;

    /**
     * Construtor
     */
    public AlterarSenhaBean() {
        usuario = Util.getUsuarioLogado();

        usuarioDAO = new UsuarioDAO();
    }

    public void alterarSenha() {

        if (!senhaAtual.trim().equals(usuario.getSenha().trim())) {
            Util.saveMessage("Atenção", "A senha atual digitada não confere com a cadastrada na base de dados.");
        } else if (!senhaNova.trim().equals(senhaConfirmar.trim())) {
            Util.saveMessage("Atenção", "A senha de confirmação está diferente da senha nova.");
        } else if (usuarioDAO.alterarSenha(senhaNova, usuario.getId())) {
            Util.saveMessage("Sucesso", "Senha alterada com sucesso.");
        } else {
            Util.saveMessage("Falha", "Falha ao atualizar senha.");
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public String getSenhaConfirmar() {
        return senhaConfirmar;
    }

    public void setSenhaConfirmar(String senhaConfirmar) {
        this.senhaConfirmar = senhaConfirmar;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }
    
    
    
    
}
