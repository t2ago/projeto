/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.controle;

import com.mycompany.clinica.dao.ConsultaDAO;
import com.mycompany.clinica.modelo.Consulta;
import java.time.LocalDate;
import java.util.List;

public class ConsultaControle {

    private ConsultaDAO consultaDAO;

    public ConsultaControle() {
        this.consultaDAO = new ConsultaDAO();
    }

    public void agendarConsulta(Consulta consulta) {
        consultaDAO.inserir(consulta);
    }

    public void atualizarConsulta(Consulta consulta) {
        consultaDAO.atualizar(consulta);
    }

    public void deletarConsulta(int id) {
        consultaDAO.remover(id);
    }

    public List<Consulta> listarConsultas() {
        return consultaDAO.listarTodos();
    }

    public List<Consulta> buscarPorNomePaciente(String nome) {
        return consultaDAO.buscarPorNomePaciente(nome);
    }

    public List<Consulta> buscarPorData(LocalDate data) {
        return consultaDAO.buscarPorData(data);
    }

    public Consulta buscarPorId(int id) {
        return consultaDAO.buscarPorId(id);
    }
}
