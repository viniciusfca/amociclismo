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
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name = "usuarioMB")
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
        usuario = new Usuario();

        usuarioDAO = new UsuarioDAO();

        System.out.println("IP: " + Util.getIp());
    }

    public String validarCampos() {
        String msg = "";

        if (!Util.isCPF(usuario.getCpf())) {
            msg += "CPF informado é inválido!";
            Util.saveMessage("Atenção", "CPF informado é inválido!");
        }

        if (Util.isEmailValido(usuario.getEmail())) {

            if (!usuario.getEmail().trim().equals(validaEmail.trim())) {
                msg += "Os e-mails não são iguais.";
                Util.saveMessage("Atenção", "Os e-mails não são iguais.");
            }
        } else {
            msg += "E-mail informado é inválido.";
            Util.saveMessage("Atenção", "E-mail informado é inválido.");

        }

        if (!usuario.getSenha().trim().equals(validaSenha.trim())) {
            msg += "Senhas não conferem.";
            Util.saveMessage("Atenção", "Senhas não conferem.");
        }

        if (usuario.getDataNascimento() == null) {
            msg += "Data de nascimento é um campo obrigatório.";
            Util.saveMessage("Atenção", "Data de nascimento é um campo obrigatório.");
        }

        if (usuario.getId() > 0) {
            msg += "O CPF informado já está cadastrado na nossa base.";
            Util.saveMessage("Atenção", "O CPF informado já está cadastrado na nossa base.");
        }

        if (usuario.getEndereco().equals("") || usuario.getCep().equals("") || usuario.getNumero().equals("")) {
            msg += "O CPF informado já está cadastrado na nossa base.";
            Util.saveMessage("Atenção", "Os dados de endereço são obrigatórios.");
        }

        Usuario userAux = usuarioDAO.getUsuarioByCpf(usuario.getCpf());

        if (userAux.getId() > 0) {
            msg += "O CPF informado já está cadastrado na nossa base.";
            Util.saveMessage("Atenção", "O CPF informado já está cadastrado na nossa base.");
        }

        return msg;
    }

    /**
     * Metodo que salva usuario
     */
    public void salvarUsuario() throws InterruptedException {
        String msg = validarCampos();
        if (msg.equals("")) {
            usuario = usuarioDAO.inserirUsuario(usuario);
        }

        if (usuario.getId() > 0 && msg.equals("")) {
            Util.saveMessage("Sucesso!", "Cadastro efetuado com sucesso.");
            RequestContext.getCurrentInstance().execute("PF('dlgCadastro').show()");
            Util.enviarEmail(usuario);

        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgCadastro').hide()");
            Util.saveMessage("Atenção", "Falha ao efetuar cadastro.");
        }

    }
    
    public void redirecionar(){
        Util.redirecionar(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.jsf");
    }
    
    public void limpar(){
        usuario = new Usuario();
    }

    /**
     * Metodo que retorna o um usuario pelo cpf
     */
    public void buscarUsuarioNovoByCpf() {
        String cpf = usuario.getCpf();

        if (!Util.isCPF(usuario.getCpf())) {
            Util.saveMessage("Atenção", "CPF informado é inválido!");
        } else {
            usuario = usuarioDAO.getUsuarioByCpf(usuario.getCpf());
            usuario.setCpf(cpf);
        }

        if (usuario.getId() > 0) {
            Util.saveMessage("Atenção", "O CPF informado já está cadastrado na nossa base.");
            usuario = new Usuario();
            usuario.setCpf(cpf);
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
