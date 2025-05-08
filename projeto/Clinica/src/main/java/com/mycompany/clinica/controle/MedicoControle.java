/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.controle;

import com.mycompany.clinica.dao.MedicoDAO;
import com.mycompany.clinica.modelo.Medico;
import java.util.List;

public class MedicoControle {

    private MedicoDAO medicoDAO;

    public MedicoControle() {
        this.medicoDAO = new MedicoDAO();
    }

    public void salvarMedico(Medico medico) {
        medicoDAO.inserir(medico);
    }

    public void atualizarMedico(Medico medico) {
        medicoDAO.atualizar(medico);
    }

    public void deletarMedico(int id) {
        medicoDAO.remover(id);
    }

    public List<Medico> listarMedicos() {
        return medicoDAO.listarTodos();
    }

    public Medico buscarPorId(int id) {
        return medicoDAO.buscarPorId(id);
    }
}