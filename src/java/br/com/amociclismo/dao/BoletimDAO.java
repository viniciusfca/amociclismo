/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.entity.Boletim;
import br.com.amociclismo.util.Conexao;
import br.com.amociclismo.util.Util;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vinicius
 */
public class BoletimDAO {
    
    /**
     * Metodo que insere um novo boletim de ocorrência
     * @param boletim
     * @param idBike
     * @return 
     */
    public Boletim inserirBoletim(Boletim boletim , int idBike){
        Conexao conexao = new Conexao();
        CallableStatement cst = null;
        
        try{
            cst = conexao.conectar().prepareCall("{call amociclismo.iBoletim(?,?,?,?,?)}");
            cst.setInt(1, idBike);
            cst.setDate(2, new java.sql.Date(boletim.getData().getTime()));
            cst.setString(3, boletim.getStatus());
            cst.setString(4, boletim.getObservacao());
            cst.setString(5, boletim.getNumero());
            
            cst.execute();
            
            PreparedStatement ps = conexao.conectar().prepareStatement("SELECT id FROM boletim WHERE idBike = ? ORDER BY id desc");
            ps.setInt(1, idBike);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                boletim.setId(rs.getInt("id"));
            }
            
        }catch(Exception e ){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return boletim;
    }
    
    /**
     * Metodo que retorna uma lista de boletins
     * @param idBike
     * @return 
     */
    public List<Boletim> getListaBoletim(int idBike){
        Conexao conexao = new Conexao();
        PreparedStatement ps  = null;
        String sql = "SELECT * FROM boletim WHERE idBike = ? ORDER BY id desc";
        List<Boletim> boletins = new ArrayList<Boletim>();
        
        try{
            ps =  conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idBike);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Boletim bo =  new Boletim();
                bo.setId(rs.getInt("id"));
                bo.setData(rs.getDate("data"));
                bo.setIdBike(rs.getInt("idBike"));
                bo.setStatus(rs.getString("status"));
                bo.setObservacao(rs.getString("observacao"));
                bo.setNumero(rs.getString("numero"));
                
                boletins.add(bo);
            }
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return boletins;
    }
    
}
