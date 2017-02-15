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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name = "bikeMB")
@ViewScoped
public class BikeBean {

    private Bike bike;
    private Boletim boletim;

    private BikeDAO bikeDAO;
    private BoletimDAO boletimDAO;

    private int idBike = 0;

    private boolean habilitarBO = true;

    private List<Boletim> boletins;
    private List<Bike> bikes;

    /**
     * Construtor
     */
    public BikeBean() {
        bikeDAO = new BikeDAO();
        boletimDAO = new BoletimDAO();

        bike = new Bike();
        boletim = new Boletim();

        boletins = new ArrayList<Boletim>();
        bikes = bikeDAO.getBikesByIdUsuario(Util.getUsuarioLogado().getId());
    }

    /**
     * Metodo que valida campos antes de salvar
     *
     * @return
     */
    public String validarCampos() {
        String msg = "";

        if (bike.getChassi().equals("") || bike.getChassi().length() < 1) {
            Util.saveMessage("Atenção", "O campo Chassi é obrigatório.");
            msg += "Erro";
        }

        if (bike.getMarca().equals("") || bike.getMarca().length() < 1) {
            Util.saveMessage("Atenção", "O campo Marca é obrigatório.");
            msg += "Erro";
        }

        if (bike.getModelo().equals("") || bike.getModelo().length() < 1) {
            Util.saveMessage("Atenção", "O campo Modelo é obrigatório.");
            msg += "Erro";
        }

        if (bike.getCores().equals("") || bike.getCores().length() < 1) {
            Util.saveMessage("Atenção", "O campo Cores é obrigatório.");
            msg += "Erro";
        }

        if (bike.getAro().equals("") || bike.getAro().length() < 1) {
            Util.saveMessage("Atenção", "O campo Aro é obrigatório.");
            msg += "Erro";
        }

        if (bike.getVelocidades().equals("") || bike.getVelocidades().length() < 1) {
            Util.saveMessage("Atenção", "O campo Velocidades é obrigatório.");
            msg += "Erro";
        }
        
        if(!bikeByChassi().equals("")){
            msg += "Erro";
        }

        return msg;
    }
    
    /**
     * Metodo que chama dialog com o termo de responsabilidade
     */
    public void aceitarTermo(){
        if(bike.getId() > 0 ){
            salvarBike();
        }else{
            RequestContext.getCurrentInstance().execute("PF('dlgTermo').show()");
        }
    }

    /**
     * Metodo que salva uma nova bicicleta
     */
    public void salvarBike() {
        
        
        if (validarCampos().equals("")) {
            bike = bikeDAO.inserirBike(bike);
        }

        if (bike.getId() > 0) {
            Util.saveMessage("Sucesso!", "Bicicleta cadastrada com sucesso.");
            bikes = bikeDAO.getBikesByIdUsuario(Util.getUsuarioLogado().getId());
            habilitarBO = false;
            boletins = boletimDAO.getListaBoletim(bike.getId());

        }else{
            Util.saveMessage("Atenção!", "Falha ao cadastrar a bicicleta.");
        }
    }

    /**
     * Novo cadastro
     */
    public void novoCadastro() {
        habilitarBO = true;
        bike = new Bike();
        boletins.clear();
    }

    /**
     * Metodo que retorna a bike pelo chassi
     */
    public String bikeByChassi() {
        String chassi = bike.getChassi();
        String erro = "";
        Bike bikeAux = bike;
        bike = bikeDAO.getBikeByChassi(bike.getChassi());

        if (bike.getId() > 0 && bike.getUsuario().getId() == Util.getUsuarioLogado().getId()) {
            Util.saveMessage("Atenção", "Você já cadastrou essa bicicleta.");
            bike = new Bike();
            bike = bikeAux;
        } else if (bike.getId() > 0 && bike.getUsuario().getId() != Util.getUsuarioLogado().getId()) {
            Util.saveMessage("Atenção", "Essa bicicleta já está cadastrada para outro usuário.");
            bike = new Bike();
            erro =  "Você já cadastrou essa bicicleta.";
        } else {
            bike = new Bike();
            bike = bikeAux;
        }
        return erro;
    }

    /**
     * Metodo que salva boletim
     */
    public void salvarBoletim() {
        boletim = boletimDAO.inserirBoletim(boletim, bike.getId());

        if (boletim.getId() > 0) {
            Util.saveMessage("Sucesso!", "Boletim cadastrado com sucesso!");
            boletins = boletimDAO.getListaBoletim(bike.getId());
        } else {
            Util.saveMessage("Atenção!", "Falha ao cadastrar boletim!");
        }

    }
    
    /**
     * Metodo que exclui boletim
     * @param idBoletim 
     */
    public void deletarBoletim(int idBoletim){
        boletimDAO.deletarBoletim(idBoletim);
        boletins = boletimDAO.getListaBoletim(bike.getId());
        
        Util.saveMessage("Sucesso", "Boletim excluído com sucesso!");
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
        habilitarBO = false;
        boletins = boletimDAO.getListaBoletim(bike.getId());
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
        RequestContext.getCurrentInstance().update("formCadastro:dlgBoletim");
        RequestContext.getCurrentInstance().execute("PF('dlgBoletim').show()");
    }

    public int getIdBike() {
        return idBike;
    }

    public void setIdBike(int idBike) {
        this.idBike = idBike;

    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

}
