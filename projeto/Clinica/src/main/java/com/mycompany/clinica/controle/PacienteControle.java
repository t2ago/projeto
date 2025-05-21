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

    public void salvarPaciente(String nome, String telefone, String cpf) {
        Paciente p = new Paciente(nome, telefone, cpf);
        pacienteDAO.inserir(p);
    }

    public boolean atualizarPaciente(Paciente paciente) {
        return pacienteDAO.atualizar(paciente);
    }

    public boolean deletarPaciente(int id) {
        return pacienteDAO.remover(id);
    }

    public ArrayList<Paciente> listarPacientes() {
        return pacienteDAO.listarTodos();
    }

    public ArrayList<Paciente> buscarPorNome(String nome) {
        return pacienteDAO.buscarPorNome(nome);
    }

    public String obterNomePacientePorId(int id) {
        return pacienteDAO.obterNomePacientePorId(id);
    }

    public boolean checarCpf(String cpf) {
        return pacienteDAO.checarCpf(cpf);
    }

}

