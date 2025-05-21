package com.mycompany.clinica.controle;

import com.mycompany.clinica.dao.ConsultaDAO;
import com.mycompany.clinica.modelo.Consulta;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ConsultaControle {

    private ConsultaDAO consultaDAO;

    public ConsultaControle() {
        this.consultaDAO = new ConsultaDAO();
    }

    public void agendarConsulta(int idPaciente, int idMedico, LocalDate data, LocalTime hora, String observacao) {
        Consulta p = new Consulta(idPaciente, idMedico, data, hora, observacao);
        consultaDAO.inserir(p);
    }

    public boolean atualizarConsulta(Consulta consulta) {
        return consultaDAO.atualizar(consulta);
    }

    public boolean deletarConsulta(int id) {
        return consultaDAO.remover(id);
    }

    public ArrayList<Consulta> listarConsultas() {
        return consultaDAO.listarTodos();
    }

    public ArrayList<Consulta> buscarPorNome(String nome) {
        return consultaDAO.buscarPorNome(nome);
    }
    
    public ArrayList<Consulta> buscarPorNomeOuData(String nomePaciente, LocalDate dataConsulta) {
        return consultaDAO.buscarPorNomeOuData(nomePaciente, dataConsulta);
    }

    public Consulta buscarPorId(int id) {
        return consultaDAO.buscarPorId(id);
    }
    
    public boolean consultaIgual(Consulta consulta) {
        return consultaDAO.consultaIgual(consulta);
    }
    
    public boolean medicoOcupado(int idMedico, LocalDate data, LocalTime hora) {
        return consultaDAO.medicoOcupado(idMedico, data, hora);
    }
    
    public boolean medicoOcupado(int idMedico, LocalDate data, LocalTime hora, int idConsultaAtual) {
        return consultaDAO.medicoOcupado(idMedico, data, hora, idConsultaAtual);
    }
}
