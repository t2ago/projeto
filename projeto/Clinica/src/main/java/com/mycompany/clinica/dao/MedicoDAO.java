package com.mycompany.clinica.dao;

import com.mycompany.clinica.modelo.Medico;
import com.mycompany.clinica.conexao.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    public void inserir(Medico medico) {
        String sql = "INSERT INTO Medico (nome, especialidade, crm) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getEspecialidade());
            stmt.setString(3, medico.getCrm());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Medico medico) {
        String sql = "UPDATE Medico SET nome = ?, especialidade = ?, crm = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getEspecialidade());
            stmt.setString(3, medico.getCrm());
            stmt.setInt(4, medico.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM Medico WHERE id = ?";

        try (Connection conn = Conexao.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Medico> listarTodos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM Medico";

        try (Connection conn = Conexao.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getInt("id"));
                medico.setNome(rs.getString("nome"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setCrm(rs.getString("crm"));
                medicos.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicos;
    }

    public Medico buscarPorId(int id) {
        Medico medico = null;
        String sql = "SELECT * FROM Medico WHERE id = ?";

        try (Connection conn = Conexao.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    medico = new Medico();
                    medico.setId(rs.getInt("id"));
                    medico.setNome(rs.getString("nome"));
                    medico.setEspecialidade(rs.getString("especialidade"));
                    medico.setCrm(rs.getString("crm"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medico;
    }
}