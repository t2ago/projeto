/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.controle;

import com.mycompany.clinica.dao.MedicoDAO;
import com.mycompany.clinica.modelo.Medico;
import java.util.ArrayList;

public class MedicoControle {
    private MedicoDAO medicoDAO;

    public MedicoControle() {
        this.medicoDAO = new MedicoDAO();
    }

    public void salvarMedico(String nome, String especialidade, String crm) {
        Medico p = new Medico(nome, especialidade, crm);
        medicoDAO.inserir(p);
    }

    public boolean atualizarMedico(Medico medico) {
        return medicoDAO.atualizar(medico);
    }

    public boolean deletarMedico(int id) {
        return medicoDAO.remover(id);
    }

    public ArrayList<Medico> listarMedicos() {
        return medicoDAO.listarTodos();
    }

    public ArrayList<Medico> buscarPorNome(String nome) {
        return medicoDAO.buscarPorNome(nome);
    }
    
    public boolean checarCrm(String crm) {
        return medicoDAO.checarCrm(crm);
    }
}