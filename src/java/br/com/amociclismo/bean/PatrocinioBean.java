/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.dao.PatrocinioDAO;
import br.com.amociclismo.entity.ImageBike;
import br.com.amociclismo.entity.Patrocinio;
import br.com.amociclismo.util.Util;
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
 * @author vinicius
 */
@ManagedBean(name = "patrocinioMB")
@ViewScoped
public class PatrocinioBean {

    private List<Patrocinio> patrocinios;

    private Patrocinio patrocinio;

    private boolean hbAddFotos;

    private PatrocinioDAO patrocinioDAO;

    /**
     * Construtor
     */
    public PatrocinioBean() {
        patrocinios = new ArrayList<Patrocinio>();

        patrocinio = new Patrocinio();

        patrocinioDAO = new PatrocinioDAO();

        hbAddFotos = true;

        carregarList();

    }

    public void carregarList() {
        patrocinios = patrocinioDAO.listar();

    }

    /**
     * Metodo que salva um novo patrocinador
     */
    public void salvarPatrocinador() {
        boolean mostraDialog = false;

        if (patrocinio.getId() > 0) {
            mostraDialog = false;
        } else {
            mostraDialog = true;
        }
        patrocinio = patrocinioDAO.inserir(patrocinio);
        if (patrocinio.getId() > 0) {
            Util.saveMessage("Sucesso!", "Patrocinador cadastrado com sucesso!");

            if (mostraDialog) {
                RequestContext.getCurrentInstance().execute("PF('dlgUploadLogo').show()");
            }

            mostraDialog = false;
        } else {
            Util.saveMessage("Atenção", "Falha ao cadastrar patrocinador!");
        }
    }

    /**
     * Metodo que deleta patrocinador
     *
     * @param idPatrocinador
     */
    public void deletarPatrocinador(int idPatrocinador) {
        if (patrocinioDAO.deletar(idPatrocinador)) {
            deletarFtp(false);
            Util.saveMessage("Sucesso", "Patrocinador excluído com sucesso!");
            patrocinios = patrocinioDAO.listar();
            patrocinio =  new Patrocinio();
        } else {
            Util.saveMessage("Atenção!", "Falha ao excluir patrocinador!");
        }
    }

    /**
     * Metodo que limpa os campos
     */
    public void novoCadastro() {
        patrocinio = new Patrocinio();
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
     *
     * @param p
     */
    public void selecionaPatrocinio(Patrocinio p) {
        patrocinio = p;
        deletarPatrocinador(patrocinio.getId());
    }

    /**
     * Meotodo que envia arquivo via ftp
     *
     * @param file
     */
    public void enviarFtp(UploadedFile file) {

        int returnCode = 0;
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect("ftp.amociclismo.com.br");

            //Verifico se o host é valido e faço login
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.login("amocicli", "Am326@CL80");

                //Altero o diretório corrente
                ftp.changeWorkingDirectory("/public_html/Imagens/patrocinio/" + patrocinio.getId());
                returnCode = ftp.getReplyCode();

                //Verifico se o diretório não existe e crio
                if (returnCode == 550) {
                    ftp.makeDirectory("/public_html/Imagens/patrocinio/" + patrocinio.getId());
                    ftp.changeWorkingDirectory("/public_html/Imagens/patrocinio/" + patrocinio.getId());
                } else {
                    ftp.changeWorkingDirectory("/public_html/Imagens/patrocinio/" + patrocinio.getId());
                }

                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.storeFile(file.getFileName(), file.getInputstream());
                ftp.disconnect();

                patrocinio.setUrlImage("/Imagens/patrocinio/" + patrocinio.getId() + "/" + file.getFileName());

                patrocinioDAO.inserir(patrocinio);
                patrocinios = patrocinioDAO.listar();
                RequestContext.getCurrentInstance().execute("PF('dlgUploadLogo').hide()");
                Util.saveMessage("Sucesso", "Logo adicionada com sucesso!");

                RequestContext.getCurrentInstance().update("formCadastro");

            }

        } catch (Exception e) {
            Util.saveMessage("Falha ao enviar imagem.", "Motivo: " + e.getMessage());
        }
    }

    /**
     * Metodo que deleta no ftp a logo
     */
    public void deletarFtp(boolean mostrarUpload) {
        try {

            int returnCode = 0;
            FTPClient ftp = new FTPClient();
            ftp.connect("ftp.amociclismo.com.br");

            //Verifico se o host é valido e faço login
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.login("amocicli", "Am326@CL80");

                //Altero o diretório corrente
                ftp.changeWorkingDirectory("/public_html/Imagens/patrocinio/" + patrocinio.getId());

                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.deleteFile("/public_html" + patrocinio.getUrlImage());
                ftp.disconnect();

                if (mostrarUpload) {
                    RequestContext.getCurrentInstance().execute("PF('dlgUploadLogo').show()");
                }

                RequestContext.getCurrentInstance().update("formCadastro");

            }

        } catch (Exception e) {
            Util.saveMessage("Falha ao enviar imagem.", "Motivo: " + e.getMessage());
        }
    }

    public List<Patrocinio> getPatrocinios() {
        return patrocinios;
    }

    public void setPatrocinios(List<Patrocinio> patrocinios) {
        this.patrocinios = patrocinios;
    }

    public Patrocinio getPatrocinio() {
        return patrocinio;
    }

    public void setPatrocinio(Patrocinio patrocinio) {
        this.patrocinio = patrocinio;
    }

}
