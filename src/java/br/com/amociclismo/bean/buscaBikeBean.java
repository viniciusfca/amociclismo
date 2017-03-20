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
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
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
     * @param url
     */
    public void excluirImage(int idImage, String url) {
        if (imageBikeDAO.excluir(idImage)) {
            Util.saveMessage("Sucesso!", "Imagem excluída com sucesso.");
            images = imageBikeDAO.listar(bike.getId());
            
            try {
             
            int returnCode = 0;    
            FTPClient ftp = new FTPClient();
            ftp.connect("ftp.amociclismo.com.br");

            //Verifico se o host é valido e faço login
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.login("amocicli", "Am326@CL80");

                //Altero o diretório corrente
                ftp.changeWorkingDirectory("/public_html/Imagens/" + bike.getId());

                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.deleteFile("/public_html"+url);
                ftp.disconnect();
                
                images = imageBikeDAO.listar(bike.getId());

                if (images.size() > 0) {
                    habViewFotos = false;
                } else {
                    habViewFotos = true;
                }

                RequestContext.getCurrentInstance().update("formCadastro");

            }

        } catch (Exception e) {
            Util.saveMessage("Falha ao excluir imagem.", "Motivo: " + e.getMessage());
        }

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
     * Metodo que salva uma nova bicicleta
     */
    public void salvarBike() {

        if (validarCampos().equals("")) {
            bike = bikeDAO.inserirBike(bike);
        }

        if (bike.getId() > 0) {
            Util.saveMessage("Sucesso!", "Bicicleta cadastrada com sucesso.");
            bikes = bikeDAO.getBikesByIdUsuario(Util.getUsuarioLogado().getId());
            boletins = boletimDAO.getListaBoletim(bike.getId());

        } else {
            Util.saveMessage("Atenção!", "Falha ao cadastrar a bicicleta.");
        }
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

        return msg;
    }
    
    
    /**
     * Metodo que retorna uma lista de bikes
     */
    public void buscarBike(){
        
        if(tipoPesquisa.equals("1")){
            valorPesquisa = " chassi=" + valorPesquisa  ;
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("2")){
            valorPesquisa = " cores like '%" + valorPesquisa + "%' ORDER BY cores";
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("3")){
            valorPesquisa = " marca like '%" + valorPesquisa + "%' ORDER BY marca";
            bikes = bikeDAO.listarBike(valorPesquisa);
        }
        
        if(tipoPesquisa.equals("4")){
            valorPesquisa = " modelo like '%" + valorPesquisa + "%' ORDER BY modelo";
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
