/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Vinicius
 */
public class Conexao {
    
    public Connection connection;
	
	/**
     * Método que devolve conexão
     * @return 
     */
    public Connection conectar(){
        try {
                  
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://micro2048:3306/amociclismo","root", "1234");
            //connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/amociclismo","root", "1234");
            
            return connection;
        } catch (Exception e) {
            System.out.println("Erro ao conectar. Motivo: " + e.getMessage());
            return null;
        }        
    }
    
    
    /**
     * Método que desconecta
     */
    public void desconectar(){
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("Erro ao desconectar. Motivo: " + e.getMessage());
        }
    }
    
}
