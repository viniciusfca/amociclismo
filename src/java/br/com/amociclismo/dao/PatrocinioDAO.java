/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.entity.Patrocinio;
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
public class PatrocinioDAO {

    /**
     * Metodo que insere um novo patrocinador
     *
     * @param patrocinio
     * @return 
     */
    public Patrocinio inserir(Patrocinio patrocinio) {
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        String sql = "INSERT INTO patrocinador (url,urlimage,nome,datacadastro) values(?,?,?,NOW())";
        String update = "UPDATE patrocinador SET url = ?, urlimage = ?, nome = ? where id = ?";

        try {
            if (patrocinio.getId() < 1) {
                ps = conexao.conectar().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, patrocinio.getUrl());
                ps.setString(2, patrocinio.getUrlImage());
                ps.setString(3, patrocinio.getNome());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    patrocinio.setId(rs.getInt(1));
                }
            }else{
                ps = conexao.conectar().prepareStatement(update);
                ps.setString(1, patrocinio.getUrl());
                ps.setString(2, patrocinio.getUrlImage());
                ps.setString(3, patrocinio.getNome());
                ps.setInt(4, patrocinio.getId());

                ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return patrocinio;
    }
    
    /**
     * Metodo que deleta patrocinador
     * @param idPatrocinio
     * @return 
     */
    public boolean deletar(int idPatrocinio){
        boolean retorno = false;
        PreparedStatement ps = null;
        Conexao conexao = new Conexao();
        String sql =  "DELETE FROM patrocinador where id = ?";
        
        try{
            ps = conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idPatrocinio);
            ps.executeUpdate();
            
            retorno = true;
            
        }catch(Exception e){
            retorno = false;
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return retorno;
    }
    
    /**
     * Metodo que retorna a lista de patrocinadores
     * @return 
     */
    public List<Patrocinio> listar(){
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        String sql = "SELECT * FROM patrocinador ORDER BY id";
        List<Patrocinio> patrocinios = new ArrayList<Patrocinio>();
        
        try{
            ps = conexao.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Patrocinio p = new Patrocinio();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setUrl(rs.getString("url"));
                p.setUrlImage(rs.getString("urlimage"));
                
                patrocinios.add(p);
            }
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
            
        return patrocinios;
    }

}
