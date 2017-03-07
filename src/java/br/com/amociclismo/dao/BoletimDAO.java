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
     *
     * @param boletim
     * @param idBike
     * @return
     */
    public Boletim inserirBoletim(Boletim boletim, int idBike) {
        Conexao conexao = new Conexao();
        CallableStatement cst = null;
        PreparedStatement psUpdate = null;

        String update = "UPDATE boletim SET status = ?, observacao = ?, numero = ? WHERE id =  ?";
        try {

            if (boletim.getId() < 1) {

                cst = conexao.conectar().prepareCall("{call amocicli_bd.iBoletim(?,?,?,?,?)}");
                cst.setInt(1, idBike);
                cst.setDate(2, new java.sql.Date(boletim.getData().getTime()));
                cst.setString(3, boletim.getStatus());
                cst.setString(4, boletim.getObservacao());
                cst.setString(5, boletim.getNumero());

                cst.execute();

                PreparedStatement ps = conexao.conectar().prepareStatement("SELECT id FROM boletim WHERE idBike = ? ORDER BY id desc");
                ps.setInt(1, idBike);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    boletim.setId(rs.getInt("id"));
                }

            }else{
                psUpdate = conexao.conectar().prepareStatement(update);
                psUpdate.setString(1, boletim.getStatus());
                psUpdate.setString(2, boletim.getObservacao());
                psUpdate.setString(3, boletim.getNumero());
                psUpdate.setInt(4, boletim.getId());
                
                psUpdate.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return boletim;
    }

    /**
     * Metodo que retorna uma lista de boletins
     *
     * @param idBike
     * @return
     */
    public List<Boletim> getListaBoletim(int idBike) {
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        String sql = "SELECT * FROM boletim WHERE idBike = ? ORDER BY id desc";
        List<Boletim> boletins = new ArrayList<Boletim>();

        try {
            ps = conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idBike);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Boletim bo = new Boletim();
                bo.setId(rs.getInt("id"));
                bo.setData(rs.getDate("data"));
                bo.setIdBike(rs.getInt("idBike"));

                if (rs.getString("status").equals("F")) {
                    bo.setStatus("Furtada");
                } else {
                    bo.setStatus("Recuperada");
                }

                bo.setObservacao(rs.getString("observacao"));
                bo.setNumero(rs.getString("numero"));

                boletins.add(bo);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return boletins;
    }
    
    /**
     * Metodo que exclui boletim de ocorrencia
     * @param idBoletim 
     */
    public void deletarBoletim(int idBoletim){
        Conexao conexao = new Conexao();
        PreparedStatement ps =  null;
        String sql = "DELETE FROM boletim WHERE id = ?";
        
        try{
            ps = conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idBoletim);
            ps.executeUpdate();
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
    }

}
