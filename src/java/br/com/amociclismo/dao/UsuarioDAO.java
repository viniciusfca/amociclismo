/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.entity.Usuario;
import br.com.amociclismo.util.Conexao;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Vinicius
 */
public class UsuarioDAO {
    
    /**
     * Metodo que insere um novo usuário na base
     * @param usuario
     * @return 
     */
    public Usuario inserirUsuario(Usuario usuario){
        Conexao conexao = new Conexao();
        CallableStatement cst = null;
        
        try{
            
            cst = conexao.conectar().prepareCall("{call amociclismo.iUsuario(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, usuario.getNome());
            cst.setString(2, usuario.getSexo());
            cst.setString(3, usuario.getCpf());
            cst.setString(4, usuario.getRg());
            cst.setDate(5, new java.sql.Date(usuario.getDataNascimento().getTime()));
            cst.setString(6, usuario.getEmail());
            cst.setString(7, usuario.getTelefone());
            cst.setString(8, usuario.getCelular());
            cst.setString(9, usuario.getEndereco());
            cst.setString(10, usuario.getNumero());
            cst.setString(11, usuario.getBairro());
            cst.setString(12, usuario.getComplemento());
            cst.setString(13, usuario.getCpf());
            cst.setString(14, usuario.getCidade());
            cst.setString(15, usuario.getUf());
            cst.setString(16, usuario.getTipo());
            cst.setString(17, usuario.getIp());
            
            cst.execute();
            
            
            PreparedStatement ps =  conexao.conectar().prepareStatement("SELECT id FROM usuario WHERE cpf = ?");
            ps.setString(1, usuario.getCpf());
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                usuario.setId(rs.getInt("id"));
            }
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return usuario;
    }
    
    
    public Usuario getUsuarioByCpf(String cpf){
        Conexao conexao =  new Conexao();
        CallableStatement cst = null;
        Usuario usuario = new Usuario();
        try{
            cst = conexao.conectar().prepareCall("{call amociclismo.getUsuarioByCpf(?)}");
            cst.setString(1, cpf);
            
            ResultSet rs = cst.executeQuery();
            usuario.setId(rs.getInt("id"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setRg(rs.getString("rg"));
            usuario.setDataNascimento(rs.getDate("dataNascimento"));
            usuario.setDataCadastro(rs.getDate("dataCadastro"));
            usuario.setEmail(rs.getString("email"));
            usuario.setNumero(rs.getString("numero"));
            usuario.setBairro(rs.getString("bairro"));
            usuario.setCidade(rs.getString("cidade"));
            usuario.setComplemento(rs.getString("complemento"));
            usuario.setUf(rs.getString("uf"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setCelular(rs.getString("celular"));
            usuario.setIp(rs.getString("ip"));
            usuario.setTipo(rs.getString("tipo"));
            usuario.setSexo(rs.getString("sexo"));
            usuario.setSenha(rs.getString("senha"));
            
            
            
            
        }catch(Exception e ){
            System.out.println("Erro: " + e.getMessage());
        }finally{
            conexao.desconectar();
        }
        
        return usuario;
    }
    
}
