/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.entity.ImageBike;
import br.com.amociclismo.util.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class ImageBikeDAO {
    
    /**
     * Metodo que insere bike na base
     * @param image 
     * @param idBike 
     * @return  
     */
    public ImageBike inserir(ImageBike image, int idBike){
        Conexao conexao  = new Conexao();
        PreparedStatement ps = null;
        String sql = "INSERT INTO imagebike (idBike, url) values (?,?)";
        
        try{
            ps =  conexao.conectar().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idBike);
            ps.setString(2, image.getUrl());
            
            ps.executeUpdate();
            
            ResultSet rs =  ps.getGeneratedKeys();
            if(rs.next()){
                image.setId(rs.getInt(1));
            }
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return image;
    }
    
    /**
     * Metodo que exclui imagem pelo id
     * @param idImage
     * @return 
     */
    public boolean excluir(int idImage){
        Conexao conexao  = new Conexao();
        PreparedStatement ps = null;
        String sql = "DELETE FROM imagebike where id = ?";
        boolean retorno =  false;
        
        try{
            ps =  conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idImage);
            ps.executeUpdate();
            
            retorno  = true;
        }catch(Exception e){
            retorno = false;
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return retorno;
    }
    
    /**
     * Metodo que retorna uma lista de imagens da bike
     * @return 
     */
    public List<ImageBike> listar(int idBike){
        Conexao conexao  = new Conexao();
        PreparedStatement ps = null;
        List<ImageBike> images = new ArrayList<ImageBike>();
        String sql = "SELECT * FROM imagebike where idBike = ?";
        
        
        try{
            ps =  conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idBike);
            ResultSet rs =  ps.executeQuery();
            
            while(rs.next()){
                ImageBike image =  new ImageBike();
                image.setId(rs.getInt("id"));
                image.setIdBike(rs.getInt("idBike"));
                image.setUrl(rs.getString("url"));
                images.add(image);
            }
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return images;
    }
}
