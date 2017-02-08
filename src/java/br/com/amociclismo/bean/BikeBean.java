/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.BikeDAO;
import br.com.amociclismo.dao.BoletimDAO;
import br.com.amociclismo.entity.Bike;
import br.com.amociclismo.entity.Boletim;
import br.com.amociclismo.util.Util;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Vinicius
 */

@ManagedBean(name="bikeMB")
@ViewScoped
public class BikeBean {
    
    private Bike bike;
    private Boletim boletim;
    
    private BikeDAO bikeDAO;
    private BoletimDAO boletimDAO;
    
    private int idBike = 0;
    
    private boolean habilitarBO = true;
    
    private List<Boletim> boletins;
    
    
    /**
     * Construtor
     */
    public BikeBean() {
        bikeDAO = new BikeDAO();
        boletimDAO = new BoletimDAO();
        
        bike = new Bike();
        boletim =  new Boletim();
        
        boletins =  new ArrayList<Boletim>();
    }
    
    /**
     * Metodo que salva uma nova bicicleta
     */
    public void salvarBike(){
        
        bike = bikeDAO.inserirBike(bike);
        
        if(bike.getId() > 0){
            Util.saveMessage("Sucesso!", "Bicicleta cadastra com sucesso.");
            habilitarBO = false;
            boletins = boletimDAO.getListaBoletim(bike.getId());
            
        }
    }
    
    /**
     * Metodo que salva boletim
     */
    public void salvarBoletim(){
        boletim = boletimDAO.inserirBoletim(boletim, bike.getId());
        
        if(boletim.getId() > 0){
            Util.saveMessage("Sucesso!", "Boletim cadastrado com sucesso!");
            boletins = boletimDAO.getListaBoletim(bike.getId());
        }else{
            Util.saveMessage("Atenção!", "Falha ao cadastrar boletim!");
        }
        
        
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public boolean isHabilitarBO() {
        return habilitarBO;
    }

    public void setHabilitarBO(boolean habilitarBO) {
        this.habilitarBO = habilitarBO;
    }

    public List<Boletim> getBoletins() {
        return boletins;
    }

    public void setBoletins(List<Boletim> boletins) {
        this.boletins = boletins;
    }

    public Boletim getBoletim() {
        return boletim;
    }

    public void setBoletim(Boletim boletim) {
        this.boletim = boletim;
    }

    public int getIdBike() {
        return idBike;
    }

    public void setIdBike(int idBike) {
        this.idBike = idBike;
    }
    
    
    
    
}
