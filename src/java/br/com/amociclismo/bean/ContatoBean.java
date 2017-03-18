/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.util.Util;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name = "contatoMB")
@ViewScoped
public class ContatoBean {

    private String nome;
    private String email;
    private String assunto;
    private String mensagem;

    /**
     * Construtor
     */
    public ContatoBean() {
        nome = "";
        email = "";
        assunto = "";
        mensagem = "";
    }

    /**
     * Metodo que envia email
     */
    public void enviarEmail() {
        
        if (nome.equals("")) {
            Util.saveMessage("Atenção", "Campo nome é obrigatório.");
        } else if (!Util.isEmailValido(email)) {
            Util.saveMessage("Atenção!", "O E-mail informado é inválido.");
        }else if(assunto.equals("")){
            Util.saveMessage("Atenção!", "Campo assunto é obrigatório.");
        }else if(mensagem.equals("")){
            Util.saveMessage("Atenção!", "Campo mensagem é obrigatório.");
        }else{
           Util.saveMessage("",Util.envioPadrao(nome, assunto, email, mensagem));
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
