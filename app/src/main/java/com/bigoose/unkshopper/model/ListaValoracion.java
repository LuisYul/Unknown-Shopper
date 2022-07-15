package com.bigoose.unkshopper.model;

import java.util.List;

public class ListaValoracion {
    private String titulo;
    private List<CriterioAtributo> listaVal;
    double promedio;
    double promedioPunt;
    private String idCriterio;

    public ListaValoracion(String titulo, List<CriterioAtributo> listaVal,double promedio,
                           String idCriterio,double promedioPunt) {
        this.titulo = titulo;
        this.listaVal = listaVal;
        this.promedio=promedio;
        this.idCriterio=idCriterio;
        this.promedioPunt=promedioPunt;
    }

    public ListaValoracion() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<CriterioAtributo> getListaVal() {
        return listaVal;
    }

    public void setListaVal(List<CriterioAtributo> listaVal) {
        this.listaVal = listaVal;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public double getPromedioPunt() {
        return promedioPunt;
    }

    public void setPromedioPunt(double promedioPunt) {
        this.promedioPunt = promedioPunt;
    }

    public String getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(String idCriterio) {
        this.idCriterio = idCriterio;
    }
}
