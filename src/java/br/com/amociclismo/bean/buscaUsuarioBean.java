/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.BikeDAO;
import br.com.amociclismo.dao.UsuarioDAO;
import br.com.amociclismo.entity.Bike;
import br.com.amociclismo.entity.Usuario;
import br.com.amociclismo.util.Util;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name = "buscaUsuarioMB")
@ViewScoped
public class buscaUsuarioBean {

    private Usuario usuario;
    private Bike bike;

    private UsuarioDAO usuarioDAO;
    private BikeDAO bikeDAO;

    private List<Bike> bikes;
    private List<Usuario> usuarios;

    private String tipoPesquisa = "";
    private String valorPesquisa = "";

    private boolean habilitarSalvar = true;

    /**
     * Construtor
     */
    public buscaUsuarioBean() {
        bikeDAO = new BikeDAO();
        usuarioDAO = new UsuarioDAO();

        bike = new Bike();
        usuario = new Usuario();

        bikes = new ArrayList<Bike>();
        usuarios = new ArrayList<>();
        tipoPesquisa = "2";
    }

    /**
     * Metodo que busca usuarios
     */
    public void buscarUsuario() {

        if (tipoPesquisa.equals("1")) {
            usuario = usuarioDAO.getUsuarioByCpf(valorPesquisa);

            if (usuario.getId() > 0) {
                RequestContext.getCurrentInstance().execute("PF('dlgBuscaUsuario').hide()");
                RequestContext.getCurrentInstance().update("formCadastro");
                habilitarSalvar = false;
                bikes = bikeDAO.getBikesByIdUsuario(usuario.getId());
                valorPesquisa = "";
                tipoPesquisa = "";
                usuarios.clear();
            }

            tipoPesquisa = "";
            valorPesquisa = "";
        } else {
            usuarios = usuarioDAO.getUsuarioByNome(valorPesquisa);
            tipoPesquisa = "";
            valorPesquisa = "";
        }

    }

    /**
     * Meotodo que atualiza usuario
     */
    public void salvarUsuario() {
        usuario = usuarioDAO.updateUsuario(usuario);
        Util.saveMessage("Sucesso!", "Usuário atualizado com sucesso!");
    }
    
    /**
     * Metodo que exclui usuario
     */
    public void excluirUsuario(){
        if(usuarioDAO.excluirUsuario(usuario.getId())){
            Util.saveMessage("Sucesso!", "Usuário excluído com sucesso!");
            RequestContext.getCurrentInstance().execute("PF('dlgExcluir').hide()");
            usuario = new Usuario();
        }else{
            Util.saveMessage("Atenção", "Falhar ao excluir usuário!");
        }
    }
 
    
    ///Getters and Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        habilitarSalvar = false;
        bikes = bikeDAO.getBikesByIdUsuario(usuario.getId());
        valorPesquisa = "";
        tipoPesquisa = "2";
        usuarios.clear();
    }
    
    

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public String getValorPesquisa() {
        return valorPesquisa;
    }

    public void setValorPesquisa(String valorPesquisa) {
        this.valorPesquisa = valorPesquisa;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public boolean isHabilitarSalvar() {
        return habilitarSalvar;
    }

    public void setHabilitarSalvar(boolean habilitarSalvar) {
        this.habilitarSalvar = habilitarSalvar;
    }

}
