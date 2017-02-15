/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.util.Conexao;
import java.sql.PreparedStatement;

/**
 *
 * @author Vinicius
 */
public class TransferenciaDAO {
    
    public void inserirLogTransferencia(int idUsuario, int idBike){
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        String sql = "INSERT INTO Transferencia (idUsuario,idBike, dataTransferencia) values (?,?,NOW())";
        
        try{
            ps = conexao.conectar().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idBike);
            
            ps.execute();
        }catch(Exception e){
            System.out.println("Erro : " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
    }
    
}
