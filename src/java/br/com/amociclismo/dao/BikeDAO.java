/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.dao;

import br.com.amociclismo.entity.Bike;
import br.com.amociclismo.entity.Usuario;
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
public class BikeDAO {

    /**
     * Metodo que insere uma nova bicicleta
     *
     * @param bike
     */
    public Bike inserirBike(Bike bike) {
        Conexao conexao = new Conexao();
        CallableStatement cst;
        PreparedStatement psUpdate = null;
        String update = "UPDATE Bike  SET chassi = ?, marca = ?, modelo = ?, cores = ?, aro = ?, velocidade = ?, observacao = ?, localCompra = ?, notaFiscal = ? WHERE id = ?";

        try {

            if (bike.getId() < 1) {
                cst = conexao.conectar().prepareCall("{call amociclismo.iBike(?,?,?,?,?,?,?,?,?,?)}");
                cst.setInt(1, Util.getUsuarioLogado().getId());
                cst.setString(2, bike.getChassi());
                cst.setString(3, bike.getMarca());
                cst.setString(4, bike.getModelo());
                cst.setString(5, bike.getCores());
                cst.setString(6, bike.getAro());
                cst.setString(7, bike.getVelocidades());
                cst.setString(8, bike.getObservacao());
                cst.setString(9, bike.getLocalCompra());
                cst.setString(10, bike.getNotaFiscal());

                cst.execute();

                PreparedStatement ps = conexao.conectar().prepareStatement("SELECT id FROM bike WHERE idUsuario = ? ORDER BY dataCadastro desc");
                ps.setInt(1, Util.getUsuarioLogado().getId());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    bike.setId(rs.getInt("id"));

                }
            } else {
                psUpdate = conexao.conectar().prepareStatement(update);

                psUpdate.setString(1, bike.getChassi());
                psUpdate.setString(2, bike.getMarca());
                psUpdate.setString(3, bike.getModelo());
                psUpdate.setString(4, bike.getCores());
                psUpdate.setString(5, bike.getAro());
                psUpdate.setString(6, bike.getVelocidades());
                psUpdate.setString(7, bike.getObservacao());
                psUpdate.setString(8, bike.getLocalCompra());
                psUpdate.setString(9, bike.getNotaFiscal());
                psUpdate.setInt(10, bike.getId());

                psUpdate.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return bike;
    }

    /**
     * Metodo que retorna uma lista de bikes pelo id do Usuario logado.
     *
     * @param idUsuario
     * @return
     */
    public List<Bike> getBikesByIdUsuario(int idUsuario) {
        Conexao conexao = new Conexao();
        List<Bike> bikes = new ArrayList<Bike>();
        PreparedStatement ps = null;

        try {
            ps = conexao.conectar().prepareStatement("SELECT * FROM Bike WHERE idUsuario = ? ORDER BY id desc");
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            Usuario user = new Usuario();
            UsuarioDAO usDAO = new UsuarioDAO();
            user = usDAO.getUsuarioById(idUsuario);
            while (rs.next()) {
                Bike b = new Bike();
                b.setId(rs.getInt("id"));
                b.setAro(rs.getString("aro"));
                b.setChassi(rs.getString("chassi"));
                b.setCores(rs.getString("cores"));
                b.setMarca(rs.getString("marca"));
                b.setModelo(rs.getString("modelo"));
                b.setNotaFiscal(rs.getString("notafiscal"));
                b.setLocalCompra(rs.getString("localcompra"));
                b.setObservacao(rs.getString("observacao"));
                b.setVelocidades(rs.getString("velocidade"));
                b.setUsuario(user);
                bikes.add(b);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return bikes;

    }

    /**
     *
     * @param chassi
     * @return
     */
    public Bike getBikeByChassi(String chassi) {
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        Bike b = new Bike();
        try {
            ps = conexao.conectar().prepareStatement("SELECT * FROM Bike WHERE chassi = ?");
            ps.setString(1, chassi);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                b.setId(rs.getInt("id"));
                b.setAro(rs.getString("aro"));
                b.setChassi(rs.getString("chassi"));
                b.setCores(rs.getString("cores"));
                b.setMarca(rs.getString("marca"));
                b.setModelo(rs.getString("modelo"));
                b.setNotaFiscal(rs.getString("notafiscal"));
                b.setLocalCompra(rs.getString("localcompra"));
                b.setObservacao(rs.getString("observacao"));
                b.setVelocidades(rs.getString("velocidade"));

                Usuario user = new Usuario();
                UsuarioDAO usDAO = new UsuarioDAO();
                user = usDAO.getUsuarioById(rs.getInt("idUsuario"));
                b.setUsuario(user);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return b;
    }
    
    /**
     * Metodo que efetua a transferência da bike
     * @return 
     */
    public String transferirBike(String cpf , int idUsuario, int idBike){
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        String sql = "UPDATE BIKE set idUsuario = ? WHERE id = ?";
        String erro = "";
        
        try{
            
            Usuario usuario =  new Usuario();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuario = usuarioDAO.getUsuarioByCpf(Util.retirarPontos(cpf));
            
            if(usuario.getId() > 0 ){
                ps = conexao.conectar().prepareStatement(sql);
                ps.setInt(1,usuario.getId());
                ps.setInt(2, idBike);
                
                ps.executeUpdate();
                
                
                TransferenciaDAO transfDAO  = new TransferenciaDAO();
                transfDAO.inserirLogTransferencia(idUsuario, idBike);
                
                erro = usuario.getNome();
            }else{
                erro = "Não existe usuário cadastrado para o CPF informado.";
            }
           
            
            
        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
            erro = e.getMessage();
        }finally{
            conexao.desconectar();
        }
        
        return erro;
    }

    /**
     * Retorna uma lista de bikes pela pesquisa
     *
     * @param valorPesquisa
     * @return
     */
    public List<Bike> listarBike(String valorPesquisa) {
        String sql = "SELECT * FROM Bike WHERE ";
        Conexao conexao = new Conexao();
        PreparedStatement ps = null;
        List<Bike> bikes = new ArrayList<Bike>();
        try {
            ps = conexao.conectar().prepareStatement(sql + valorPesquisa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Usuario user = new Usuario();
                UsuarioDAO usDAO = new UsuarioDAO();
                user = usDAO.getUsuarioById(rs.getInt("idUsuario"));

                Bike b = new Bike();
                b.setId(rs.getInt("id"));
                b.setAro(rs.getString("aro"));
                b.setChassi(rs.getString("chassi"));
                b.setCores(rs.getString("cores"));
                b.setMarca(rs.getString("marca"));
                b.setModelo(rs.getString("modelo"));
                b.setNotaFiscal(rs.getString("notafiscal"));
                b.setLocalCompra(rs.getString("localcompra"));
                b.setObservacao(rs.getString("observacao"));
                b.setVelocidades(rs.getString("velocidade"));
                b.setUsuario(user);
                bikes.add(b);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            conexao.desconectar();
        }

        return bikes;
    }

}
