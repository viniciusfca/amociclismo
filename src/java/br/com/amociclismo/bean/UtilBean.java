/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.util.Util;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name="utilMB")
@ViewScoped
public class UtilBean {
    
    private String cpf;
    
    public void reenviarSenha(){
       String retorno = Util.reenviarSenha(cpf);
        RequestContext.getCurrentInstance().execute("PF('dlgSenha').hide()");
        Util.saveMessage("Atenção", retorno );
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    
}
