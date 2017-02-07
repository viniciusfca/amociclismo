/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.bean;

import br.com.amociclismo.entity.Patrocinio;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author vinicius
 */
@ManagedBean(name="patrocinioMB")
@ViewScoped
public class PatrocinioBean {
    
    private List<Patrocinio> patrocinios;
    
    /**
     * Construtor
     */
    public PatrocinioBean() {
        patrocinios = new ArrayList<Patrocinio>();
        carregarList();
        
    }
    
    
    public void carregarList(){
        Patrocinio p1 = new Patrocinio();
        Patrocinio p2 = new Patrocinio();
        Patrocinio p3 = new Patrocinio();
        Patrocinio p4 = new Patrocinio();
        
        p1.setNome("Google");
        p1.setUrl("http://www.google.com.br");
        p1.setUrlImage("https://yt3.ggpht.com/-v0soe-ievYE/AAAAAAAAAAI/AAAAAAAAAAA/OixOH_h84Po/s900-c-k-no-mo-rj-c0xffffff/photo.jpg");
        
        p2.setNome("Yahoo");
        p2.setUrl("http://www.yahoo.com.br");
        p2.setUrlImage("https://s.yimg.com/dh/ap/default/130909/y_200_a.png");
        
        p3.setNome("Java");
        p3.setUrl("http://www.oracle.com");
        p3.setUrlImage("https://cdn.codementor.io/assets/topic/category_header/java-3cf2d56f64ec228a8dfa919fffdd23c6.png");
        
        p4.setNome("Oracle");
        p4.setUrl("http://www.oracle.com");
        p4.setUrlImage("https://www.oracle.com/us/oracle-social-share-fb-480-2516041.jpg");
        
        patrocinios.add(p1);
        patrocinios.add(p2);
        patrocinios.add(p3);
        patrocinios.add(p4);
    
    }

    public List<Patrocinio> getPatrocinios() {
        return patrocinios;
    }

    public void setPatrocinios(List<Patrocinio> patrocinios) {
        this.patrocinios = patrocinios;
    }
    
    
    
}
