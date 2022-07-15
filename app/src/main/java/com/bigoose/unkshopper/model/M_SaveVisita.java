package com.bigoose.unkshopper.model;

import java.util.List;

public class M_SaveVisita {
    private List<Visita> visita;
    private List<VisitaEvaluacion> visita_evaluacion;
    private List<VisitaCriterio> visita_criterio;
    private List<VisitaAtributo> visita_atributo;
    private List<VisitaFoto> visita_fotos;

    public M_SaveVisita(List<Visita> visita, List<VisitaEvaluacion> visita_evaluacion, List<VisitaCriterio> visita_criterio,
                        List<VisitaAtributo> visita_atributo, List<VisitaFoto> visita_fotos) {
        this.visita = visita;
        this.visita_evaluacion = visita_evaluacion;
        this.visita_criterio = visita_criterio;
        this.visita_atributo = visita_atributo;
        this.visita_fotos = visita_fotos;
    }

    public M_SaveVisita() {
    }

    public List<Visita> getVisita() {
        return visita;
    }

    public void setVisita(List<Visita> visita) {
        this.visita = visita;
    }

    public List<VisitaEvaluacion> getVisita_evaluacion() {
        return visita_evaluacion;
    }

    public void setVisita_evaluacion(List<VisitaEvaluacion> visita_evaluacion) {
        this.visita_evaluacion = visita_evaluacion;
    }

    public List<VisitaCriterio> getVisita_criterio() {
        return visita_criterio;
    }

    public void setVisita_criterio(List<VisitaCriterio> visita_criterio) {
        this.visita_criterio = visita_criterio;
    }

    public List<VisitaAtributo> getVisita_atributo() {
        return visita_atributo;
    }

    public void setVisita_atributo(List<VisitaAtributo> visita_atributo) {
        this.visita_atributo = visita_atributo;
    }

    public List<VisitaFoto> getVisita_fotos() {
        return visita_fotos;
    }

    public void setVisita_fotos(List<VisitaFoto> visita_fotos) {
        this.visita_fotos = visita_fotos;
    }
}
