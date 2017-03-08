/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.BikeDAO;
import br.com.amociclismo.dao.BoletimDAO;
import br.com.amociclismo.dao.ImageBikeDAO;
import br.com.amociclismo.entity.Bike;
import br.com.amociclismo.entity.Boletim;
import br.com.amociclismo.entity.ImageBike;
import br.com.amociclismo.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Vinicius
 */
@ManagedBean(name = "bikeMB")
@ViewScoped
public class BikeBean {

    private Bike bike;
    private Boletim boletim;
    private ImageBike image;

    private BikeDAO bikeDAO;
    private BoletimDAO boletimDAO;
    private ImageBikeDAO imageBikeDAO;

    private int idBike = 0;

    private String cpfTransferencia;

    private boolean habilitarBO = true;
    private boolean habViewFotos = true;
    private boolean habAddFotos = true;

    private List<Boletim> boletins;
    private List<Bike> bikes;
    private List<ImageBike> images;

    /**
     * Construtor
     */
    public BikeBean() {
        bikeDAO = new BikeDAO();
        boletimDAO = new BoletimDAO();
        imageBikeDAO = new ImageBikeDAO();

        bike = new Bike();
        boletim = new Boletim();
        image = new ImageBike();

        boletins = new ArrayList<Boletim>();
        images = new ArrayList<ImageBike>();
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

        if (!bikeByChassi().equals("")) {
            msg += "Erro";
        }

        return msg;
    }

    /**
     * Metodo que transfere bike
     */
    public void transferirBike() {
        String retorno = bikeDAO.transferirBike(cpfTransferencia, bike.getUsuario().getId(), bike.getId());

        if (!retorno.equals("Não existe usuário cadastrado para o CPF informado.")) {
            Util.saveMessage("Sucesso", "Sua Bicicleta foi transferida para: " + retorno);
            bikes = bikeDAO.getBikesByIdUsuario(Util.getUsuarioLogado().getId());
        } else {
            Util.saveMessage("Atenção!", retorno);
            retorno = "";
        }
    }

    /**
     * Metodo que chama dialog com o termo de responsabilidade
     */
    public void aceitarTermo() {
        if (bike.getId() > 0) {
            salvarBike();
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgTermo').show()");
        }
    }

    /**
     * Metodo que faz o upload do arquivo
     *
     * @param fileUploadEvent
     */
    public void upload(FileUploadEvent fileUploadEvent) {
        try {
            if (fileUploadEvent.getFile() != null) {
                if (fileUploadEvent.getFile().getFileName().trim().length() > 0) {
                    try {
                        UploadedFile file = fileUploadEvent.getFile();
                        enviarFtp(file);
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                } else {
                    throw new Exception("Nenhum arquivo selecionado");
                }
            } else {
                throw new Exception("Nenhum arquivo selecionado");
            }
        } catch (Exception e) {
            Util.saveMessage("Erro", e.getMessage());
        }
    }

    /**
     * Meotodo que envia arquivo via ftp
     *
     * @param file
     */
    public void enviarFtp(UploadedFile file) {
        System.out.println("Chegou aqui: " + file.getFileName());
        int returnCode = 0;
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect("ftp.amociclismo.com.br");

            //Verifico se o host é valido e faço login
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.login("amocicli", "Am326@CL80");

                //Altero o diretório corrente
                ftp.changeWorkingDirectory("/public_html/Imagens/" + bike.getId());
                returnCode = ftp.getReplyCode();

                //Verifico se o diretório não existe e crio
                if (returnCode == 550) {
                    ftp.makeDirectory("/public_html/Imagens/bikes/" + bike.getId());
                    ftp.changeWorkingDirectory("/public_html/Imagens/bikes/" + bike.getId());
                } else {
                    ftp.changeWorkingDirectory("/public_html/Imagens/bikes/" + bike.getId());
                }

                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.storeFile(file.getFileName(), file.getInputstream());
                ftp.disconnect();
               
                ImageBike img = new ImageBike();
                
                img.setIdBike(bike.getId());
                img.setUrl("http://www.amociclismo.com.br/Imagens/bikes/" + bike.getId() + "/" + file.getFileName());
                img = imageBikeDAO.inserir(img, bike.getId());

                images = imageBikeDAO.listar(bike.getId());

                if (images.size() > 0) {
                    habViewFotos = false;
                } else {
                    habViewFotos = true;
                }

                if (images.size() == 3) {
                    habAddFotos = true;
                } else {
                    habAddFotos = false;
                }

                RequestContext.getCurrentInstance().execute("PF('dlgUploadImagem').hide()");
                Util.saveMessage("Sucesso", "Imagens enviadas com sucesso.");

                RequestContext.getCurrentInstance().update("formCadastro");

            }

        } catch (Exception e) {
            Util.saveMessage("Falha ao enviar imagem.", "Motivo: " + e.getMessage());
        }
    }

    /**
     * Meotodo que exclui image da bike
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

            if (images.size() == 3) {
                habAddFotos = true;
            } else {
                habAddFotos = false;
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
            habilitarBO = false;
            habAddFotos = false;
            boletins = boletimDAO.getListaBoletim(bike.getId());

        } else {
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
            erro = "Você já cadastrou essa bicicleta.";
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
     *
     * @param idBoletim
     */
    public void deletarBoletim(int idBoletim) {
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
        images = imageBikeDAO.listar(bike.getId());

        if (images.size() > 0) {
            habViewFotos = false;
        } else {
            habViewFotos = true;
        }

        if (images.size() == 3) {
            habAddFotos = true;
        } else {
            habAddFotos = false;
        }
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

    public String getCpfTransferencia() {
        return cpfTransferencia;
    }

    public void setCpfTransferencia(String cpfTransferencia) {
        this.cpfTransferencia = cpfTransferencia;
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

    public boolean isHabAddFotos() {
        return habAddFotos;
    }

    public void setHabAddFotos(boolean habAddFotos) {
        this.habAddFotos = habAddFotos;
    }

}
