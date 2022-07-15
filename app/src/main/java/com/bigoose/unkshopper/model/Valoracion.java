package com.bigoose.unkshopper.model;

public class Valoracion {

    private String item;
    private double puntaje;

    public Valoracion(String item, double puntaje) {
        this.item = item;
        this.puntaje = puntaje;
    }

    public Valoracion() {
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }
}
