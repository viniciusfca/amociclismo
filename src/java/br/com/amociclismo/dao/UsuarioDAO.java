/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.entity.Usuario;
import br.com.amociclismo.util.Conexao;
import br.com.amociclismo.util.Util;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

/**
 *
 * @author Vinicius
 */
public class UsuarioDAO {

    /**
     * Metodo que insere um novo usuário na base
     *
     * @param usuario
     * @return
     */
    public Usuario inserirUsuario(Usuario usuario) {
        Conexao conexao = new Conexao();
        CallableStatement cst = null;

        try {

            cst = conexao.conectar().prepareCall("{call amociclismo.iUsuario(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, usuario.getNome());
            cst.setString(2, usuario.getSexo());
            cst.setString(3, usuario.getCpf());
            cst.setString(4, usuario.getRg());
            cst.setDate(5, Util.tratarData(usuario.getDataNascimento()));
            cst.setString(6, usuario.getEmail());
            cst.setString(7, usuario.getTelefone());
            cst.setString(8, usuario.getCelular());
            cst.setString(9, usuario.getEndereco());
            cst.setString(10, usuario.getNumero());
            cst.setString(11, usuario.getBairro());
            cst.setString(12, usuario.getComplemento());
            cst.setString(13, usuario.getCep());
            cst.setString(14, usuario.getCidade());
            cst.setString(15, usuario.getUf());
            cst.setString(16, Util.encrypt(usuario.getSenha()));
            cst.setString(17, "C");
            cst.setString(18, Util.getIp());

            cst.execute();

            PreparedStatement ps = conexao.conectar().prepareStatement("SELECT id FROM usuario WHERE cpf = ?");
            ps.setString(1, usuario.getCpf());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return usuario;
    }
    
    
    
    /**
     * Metodo que insere um novo usuário na base
     *
     * @param usuario
     * @return
     */
    public Usuario updateUsuario(Usuario usuario) {
        Conexao conexao = new Conexao();
        CallableStatement cst = null;

        try {

            cst = conexao.conectar().prepareCall("{call amociclismo.uUsuario(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, usuario.getNome());
            cst.setString(2, usuario.getSexo());
            cst.setString(3, usuario.getCpf());
            cst.setString(4, usuario.getRg());
            cst.setDate(5, Util.tratarData(usuario.getDataNascimento()));
            cst.setString(6, usuario.getEmail());
            cst.setString(7, usuario.getTelefone());
            cst.setString(8, usuario.getCelular());
            cst.setString(9, usuario.getEndereco());
            cst.setString(10, usuario.getNumero());
            cst.setString(11, usuario.getBairro());
            cst.setString(12, usuario.getComplemento());
            cst.setString(13, usuario.getCep());
            cst.setString(14, usuario.getCidade());
            cst.setString(15, usuario.getUf());
            cst.setString(16, Util.encrypt(usuario.getSenha()));
            cst.setString(17, usuario.getTipo());
            cst.setString(18, Util.getIp());
            cst.setInt(19, usuario.getId());

            cst.execute();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return usuario;
    }

    /**
     * Meotodo que retonar um usuario pelo cpf
     * @param cpf
     * @return 
     */
    public Usuario getUsuarioByCpf(String cpf) {
        Conexao conexao = new Conexao();
        CallableStatement cst = null;
        Usuario usuario = new Usuario();
        try {
            cst = conexao.conectar().prepareCall("{call amociclismo.getUsuarioByCpf(?)}");
            cst.setString(1, cpf);

            ResultSet rs = cst.executeQuery();
            if (rs.next()) {
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
                usuario.setCep(rs.getString("cep"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setUf(rs.getString("uf"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCelular(rs.getString("celular"));
                usuario.setIp(rs.getString("ip"));
                usuario.setTipo(rs.getString("tipo"));
                usuario.setSexo(rs.getString("sexo"));
                usuario.setSenha(rs.getString("senha"));

            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return usuario;
    }

    /**
     * Meotodo que retorna um usuario pelo id
     * @param idUsuario
     * @return 
     */
     public Usuario getUsuarioById(int idUsuario) {
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        Usuario usuario = new Usuario();
        try {
            ps = conexao.conectar().prepareStatement("SELECT * FROM Usuario WHERE id = ?");
            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
                usuario.setCep(rs.getString("cep"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setUf(rs.getString("uf"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCelular(rs.getString("celular"));
                usuario.setIp(rs.getString("ip"));
                usuario.setTipo(rs.getString("tipo"));
                usuario.setSexo(rs.getString("sexo"));
                usuario.setSenha(rs.getString("senha"));

            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return usuario;
    }

    
    
    /**
     * Meotodo que retorna uma lista de usuarios pelo nome
     * @param nome
     * @return 
     */
    public List<Usuario> getUsuarioByNome(String nome) {
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;

        List<Usuario> usuarios = new ArrayList<Usuario>();
        try {
            ps = conexao.conectar().prepareStatement("SELECT * FROM Usuario WHERE nome LIKE '%"+nome+"%'");
           

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
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
                usuario.setCep(rs.getString("cep"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setComplemento(rs.getString("complemento"));
                usuario.setUf(rs.getString("uf"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCelular(rs.getString("celular"));
                usuario.setIp(rs.getString("ip"));
                usuario.setTipo(rs.getString("tipo"));
                usuario.setSexo(rs.getString("sexo"));
                usuario.setSenha(rs.getString("senha"));
                
                usuarios.add(usuario);

            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return usuarios;
    }

    /**
     * Meotodo que retorna um usuario pelo login e senha
     *
     * @param username
     * @param senha
     * @return
     */
    public Usuario getUsuarioByLogin(String username, String senha) {
        PreparedStatement ps = null;
        Conexao conexao = new Conexao();
        Usuario usuario = new Usuario();

        try {
            ps = conexao.conectar().prepareStatement("SELECT * FROM usuario where cpf = ? and senha = ?");
            ps.setString(1, username);
            ps.setString(2, Util.encrypt(senha));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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
                usuario.setCep(rs.getString("cep"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setUf(rs.getString("uf"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setCelular(rs.getString("celular"));
                usuario.setIp(rs.getString("ip"));
                usuario.setTipo(rs.getString("tipo"));
                usuario.setSexo(rs.getString("sexo"));
                usuario.setSenha(Util.decrypt(rs.getString("senha")));

            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return usuario;
    }

}
