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
 * @author vinicius
 */
@ManagedBean(name="principalMB")
@ViewScoped
public class PrincipalBean {

    private List<Bike> bikes;
    private List<Usuario> usuarios;
    private List<Boletim> boletins;
    
    private BikeDAO bikeDAO;
    private UsuarioDAO usuarioDAO;
    private BoletimDAO boletimDAO;
    
    /**
     * 
     */
    public PrincipalBean() {
        bikes = new ArrayList<Bike>();
        usuarios = new ArrayList<Usuario>();
        boletins =  new ArrayList<Boletim>();
        
        bikeDAO = new BikeDAO();
        usuarioDAO =  new UsuarioDAO();
        boletimDAO =  new BoletimDAO();
        
        bikes = bikeDAO.listarBikeAll();
        usuarios = usuarioDAO.getUsuarioAll();
        boletins = boletimDAO.getListaBoletimAll();
        
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Boletim> getBoletins() {
        return boletins;
    }

    public void setBoletins(List<Boletim> boletins) {
        this.boletins = boletins;
    }
    
    
    
}
