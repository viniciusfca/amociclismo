/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.BikeDAO;
import br.com.amociclismo.dao.BoletimDAO;
import br.com.amociclismo.dao.UsuarioDAO;
import br.com.amociclismo.entity.Bike;
import br.com.amociclismo.entity.Boletim;
import br.com.amociclismo.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name="buscaBikeMB")
@ViewScoped
public class buscaBikeBean {
    
    private Bike bike;
    private Usuario usuario;
    
    private List<Bike> bikes;
    private List<Boletim> boletins;
    
    private BikeDAO bikeDAO;
    private UsuarioDAO usuarioDAO;
    private BoletimDAO boletimDAO;
    
    private String valorPesquisa;
    private String tipoPesquisa;

    public buscaBikeBean() {
        this.bike = new Bike();
        bike.setUsuario(new Usuario());
        this.usuario = new Usuario();
        this.bikes = new ArrayList<>();
        this.boletins = new ArrayList<>();
        
        
        usuarioDAO =  new UsuarioDAO();
        bikeDAO = new BikeDAO();
        boletimDAO = new BoletimDAO();
    }
    
    /**
     * Metodo que retorna uma lista de bikes
     */
    public void buscarBike(){
        
        if(tipoPesquisa.equals("1")){
            valorPesquisa = " chassi=" + valorPesquisa;
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("2")){
            valorPesquisa = " cores like '%" + valorPesquisa + "%'";
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("3")){
            valorPesquisa = " marca like '%" + valorPesquisa + "%'";
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("4")){
            valorPesquisa = " modelo like '%" + valorPesquisa + "%'";
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("5")){
            usuario = usuarioDAO.getUsuarioByCpf(valorPesquisa);
            bikes = bikeDAO.getBikesByIdUsuario(usuario.getId());
            
        }
        
        
        
    }
    
    
    ///Getters and Setters

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
        boletins =  boletimDAO.getListaBoletim(bike.getId());
        valorPesquisa = "";
        tipoPesquisa = "";
        bikes.clear();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    public List<Boletim> getBoletins() {
        return boletins;
    }

    public void setBoletins(List<Boletim> boletins) {
        this.boletins = boletins;
    }

    public String getValorPesquisa() {
        return valorPesquisa;
    }

    public void setValorPesquisa(String valorPesquisa) {
        this.valorPesquisa = valorPesquisa;
    }

    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }
    
    
   
}
