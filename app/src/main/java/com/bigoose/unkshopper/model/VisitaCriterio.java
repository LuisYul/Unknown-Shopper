package com.bigoose.unkshopper.model;

import java.util.List;

public class VisitaCriterio {
    private String idVisita;
    private String idCriterio;
    private String score;
    private String valor;

    public VisitaCriterio(String idVisita, String idCriterio, String score, String valor) {
        this.idVisita = idVisita;
        this.idCriterio = idCriterio;
        this.score = score;
        this.valor = valor;
    }

    public VisitaCriterio() {
    }

    public String getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(String idVisita) {
        this.idVisita = idVisita;
    }

    public String getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(String idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
