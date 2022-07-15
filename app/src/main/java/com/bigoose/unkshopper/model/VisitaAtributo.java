package com.bigoose.unkshopper.model;

public class VisitaAtributo {
    private String idVisita;
    private String idCriterio;
    private String idAtributo;
    private String score;

    public VisitaAtributo(String idVisita, String idCriterio, String idAtributo, String score) {
        this.idVisita = idVisita;
        this.idCriterio = idCriterio;
        this.idAtributo = idAtributo;
        this.score = score;
    }

    public VisitaAtributo() {
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

    public String getIdAtributo() {
        return idAtributo;
    }

    public void setIdAtributo(String idAtributo) {
        this.idAtributo = idAtributo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
