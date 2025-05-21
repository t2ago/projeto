package com.mycompany.clinica.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private int id;
    private int idPaciente;
    private int idMedico;
    private LocalDate data;
    private LocalTime hora;
    private String observacao;

    public Consulta() {
        
    }

    public Consulta(int idPaciente, int idMedico, LocalDate data, LocalTime hora, String observacao) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.data = data;
        this.hora = hora;
        this.observacao = observacao;
    }

    public Consulta(int id, int idPaciente, int idMedico, LocalDate data, LocalTime hora, String observacao) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.data = data;
        this.hora = hora;
        this.observacao = observacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
