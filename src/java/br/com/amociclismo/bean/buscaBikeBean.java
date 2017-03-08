/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.BikeDAO;
import br.com.amociclismo.dao.BoletimDAO;
import br.com.amociclismo.dao.ImageBikeDAO;
import br.com.amociclismo.dao.UsuarioDAO;
import br.com.amociclismo.entity.Bike;
import br.com.amociclismo.entity.Boletim;
import br.com.amociclismo.entity.ImageBike;
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
@ManagedBean(name="buscaBikeMB")
@ViewScoped
public class buscaBikeBean {
    
    private Bike bike;
    private Usuario usuario;
    private ImageBike image;
    
    private List<Bike> bikes;
    private List<Boletim> boletins;
    private List<ImageBike> images;
    
    private BikeDAO bikeDAO;
    private UsuarioDAO usuarioDAO;
    private BoletimDAO boletimDAO;
    private ImageBikeDAO imageBikeDAO;
    
    private String valorPesquisa;
    private String tipoPesquisa;
    
    private boolean habiliarExcluir = true;
    private boolean habViewFotos = true;

    public buscaBikeBean() {
        this.bike = new Bike();
        bike.setUsuario(new Usuario());
        this.usuario = new Usuario();
        image = new ImageBike();
        
        this.bikes = new ArrayList<>();
        this.boletins = new ArrayList<>();
        images = new ArrayList<ImageBike>();
        
        
        usuarioDAO =  new UsuarioDAO();
        bikeDAO = new BikeDAO();
        boletimDAO = new BoletimDAO();
        imageBikeDAO = new ImageBikeDAO();

        
        tipoPesquisa = "3";
    }
    
    
    /**
     * Meotodo que exclui image da bike
     * @param idImage
     */
    public void excluirImage(int idImage) {
        if (imageBikeDAO.excluir(idImage)) {
            Util.saveMessage("Sucesso!", "Imagem excluída com sucesso.");
            images = imageBikeDAO.listar(bike.getId());

            if (images.size() > 0) {
                habViewFotos = false;
            } else {
                habViewFotos = true;
            }
            
        } else {
            Util.saveMessage("Atenção", "Falha ao excluir a imagem.");
        }
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
    
    
    public void excluirBike(){
        if(bikeDAO.excluirBike(bike.getId())){
            bike = new Bike();
            Util.saveMessage("Sucesso", "Bicicleta excluída com sucesso!");
            RequestContext.getCurrentInstance().execute("PF('dlgExcluir').hide()");
            habiliarExcluir = true;
        }else{
            Util.saveMessage("Atenção", "Falha ao excluir bicicleta!");
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
        tipoPesquisa = "3";
        bikes.clear();
        habiliarExcluir = false;
        
        images = imageBikeDAO.listar(bike.getId());
        if (images.size() > 0) {
                habViewFotos = false;
            } else {
                habViewFotos = true;
            }
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

    public boolean isHabiliarExcluir() {
        return habiliarExcluir;
    }

    public void setHabiliarExcluir(boolean habiliarExcluir) {
        this.habiliarExcluir = habiliarExcluir;
    }

    public ImageBike getImage() {
        return image;
    }

    public void setImage(ImageBike image) {
        this.image = image;
    }

    public List<ImageBike> getImages() {
        return images;
    }

    public void setImages(List<ImageBike> images) {
        this.images = images;
    }

    public boolean isHabViewFotos() {
        return habViewFotos;
    }

    public void setHabViewFotos(boolean habViewFotos) {
        this.habViewFotos = habViewFotos;
    }
    
    
   
}
