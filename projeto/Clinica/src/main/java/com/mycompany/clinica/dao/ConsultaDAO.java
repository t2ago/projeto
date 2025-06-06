package com.mycompany.clinica.dao;

import com.mycompany.clinica.modelo.Consulta;
import com.mycompany.clinica.conexao.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ConsultaDAO {

    public void inserir(Consulta consulta) {
        String sql = "INSERT INTO Consulta (id_paciente, id_medico, data, hora, observacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getIdPaciente());
            stmt.setInt(2, consulta.getIdMedico());
            stmt.setDate(3, Date.valueOf(consulta.getData()));
            stmt.setTime(4, Time.valueOf(consulta.getHora()));
            stmt.setString(5, consulta.getObservacao());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean atualizar(Consulta consulta) {
        String sql = "UPDATE Consulta SET id_paciente = ?, id_medico = ?, data = ?, hora = ?, observacao = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getIdPaciente());
            stmt.setInt(2, consulta.getIdMedico());
            stmt.setDate(3, Date.valueOf(consulta.getData()));
            stmt.setTime(4, Time.valueOf(consulta.getHora()));
            stmt.setString(5, consulta.getObservacao());
            stmt.setInt(6, consulta.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM Consulta WHERE id = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Consulta> listarTodos() {
        ArrayList<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT * FROM Consulta";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setId(rs.getInt("id"));
                consulta.setIdPaciente(rs.getInt("id_paciente"));
                consulta.setIdMedico(rs.getInt("id_medico"));
                consulta.setData(rs.getDate("data").toLocalDate());
                consulta.setHora(rs.getTime("hora").toLocalTime());
                consulta.setObservacao(rs.getString("observacao"));
                consultas.add(consulta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultas;
    }

    public Consulta buscarPorId(int id) {
        Consulta consulta = null;
        String sql = "SELECT * FROM Consulta WHERE id = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    consulta = new Consulta();
                    consulta.setId(rs.getInt("id"));
                    consulta.setIdPaciente(rs.getInt("id_paciente"));
                    consulta.setIdMedico(rs.getInt("id_medico"));
                    consulta.setData(rs.getDate("data").toLocalDate());
                    consulta.setHora(rs.getTime("hora").toLocalTime());
                    consulta.setObservacao(rs.getString("observacao"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consulta;
    }

    public ArrayList<Consulta> buscarPorNome(String nome) {
        ArrayList<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.* FROM Consulta c "
                + "JOIN Paciente p ON c.fk_Paciente_id = p.id "
                + "WHERE p.nome LIKE ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        rs.getInt("id"),
                        rs.getInt("fk_Paciente_id"),
                        rs.getInt("fk_Medico_id"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("observacao")
                );
                consultas.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public ArrayList<Consulta> buscarPorNomeOuData(String nomePaciente, LocalDate dataConsulta) {
        ArrayList<Consulta> consultas = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT c.* FROM Consulta c JOIN Paciente p ON c.id_paciente = p.id WHERE 1=1"
        );

        if (nomePaciente != null && !nomePaciente.isEmpty()) {
            sql.append(" AND LOWER(p.nome) LIKE ?");
        }

        if (dataConsulta != null) {
            sql.append(" AND c.data = ?");
        }

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (nomePaciente != null && !nomePaciente.isEmpty()) {
                stmt.setString(paramIndex++, "%" + nomePaciente.toLowerCase() + "%");
            }

            if (dataConsulta != null) {
                stmt.setDate(paramIndex++, Date.valueOf(dataConsulta));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        rs.getInt("id"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_medico"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("observacao")
                );
                consultas.add(consulta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultas;
    }

    public boolean consultaIgual(Consulta consulta) {
        String sql = "SELECT * FROM Consulta WHERE id_paciente = ? AND id_medico = ? AND data = ? AND hora = ? AND observacao = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getIdPaciente());
            stmt.setInt(2, consulta.getIdMedico());
            stmt.setDate(3, Date.valueOf(consulta.getData()));
            stmt.setTime(4, Time.valueOf(consulta.getHora()));
            stmt.setString(5, consulta.getObservacao());

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean medicoOcupado(int idMedico, LocalDate data, LocalTime hora) {
        String sql = "SELECT * FROM Consulta WHERE id_medico = ? AND data = ? AND hora = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMedico);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setTime(3, Time.valueOf(hora));

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean medicoOcupado(int idMedico, LocalDate data, LocalTime hora, int idConsultaAtual) {
        String sql = "SELECT * FROM Consulta WHERE id_medico = ? AND data = ? AND hora = ? AND id <> ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMedico);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setTime(3, Time.valueOf(hora));
            stmt.setInt(4, idConsultaAtual);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
