package com.bigoose.unkshopper.model;

public class VisitaEvaluacion {

    private String idVisita;
    private String hora;
    private String score;

    public VisitaEvaluacion(String idVisita, String hora, String score) {
        this.idVisita = idVisita;
        this.hora = hora;
        this.score = score;
    }

    public VisitaEvaluacion() {
    }

    public String getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(String idVisita) {
        this.idVisita = idVisita;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
