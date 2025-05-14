/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.controle;

import com.mycompany.clinica.dao.PacienteDAO;
import com.mycompany.clinica.modelo.Paciente;
import java.util.ArrayList;

public class PacienteControle {

    private PacienteDAO pacienteDAO;

    public PacienteControle() {
        this.pacienteDAO = new PacienteDAO();
    }

    public void salvarPaciente(Paciente paciente) {
        pacienteDAO.inserir(paciente);
    }

    public void atualizarPaciente(Paciente paciente) {
        pacienteDAO.atualizar(paciente);
    }

    public void deletarPaciente(int id) {
        pacienteDAO.remover(id);
    }

    public ArrayList<Paciente> listarPacientes() {
        return pacienteDAO.listarTodos();
    }

    public Paciente buscarPorId(int id) {
        return pacienteDAO.buscarPorId(id);
    }
}

