package com.bigoose.unkshopper.model;

import java.util.List;

public class Visita {
    private String idVisita;
    private String idLocal;
    private String nombre_local;
    private String horaIni;
    private String horaFin;
    private String latitudIni;
    private String longitudIni;
    private String latitudFin;
    private String longitudFin;
    private String idRegion;
    private String nombre_region;
    private String idCliente;
    private String nombre_cliente;
    private String idGiroNegocio;
    private String nombre_giroNegocio;
    private String comentario;
    private String direccion;
    private String condicion;
    private int estado;

    public Visita(String idVisita, String horaIni, String horaFin, String latitudIni, String longitudIni,
                  String latitudFin, String longitudFin, String comentario, String condicion) {
        this.idVisita = idVisita;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.latitudIni = latitudIni;
        this.longitudIni = longitudIni;
        this.latitudFin = latitudFin;
        this.longitudFin = longitudFin;
        this.comentario = comentario;
        this.condicion = condicion;
    }


    public Visita() {
    }

    public String getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(String idVisita) {
        this.idVisita = idVisita;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getNombre_local() {
        return nombre_local;
    }

    public void setNombre_local(String nombre_local) {
        this.nombre_local = nombre_local;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getLatitudIni() {
        return latitudIni;
    }

    public void setLatitudIni(String latitudIni) {
        this.latitudIni = latitudIni;
    }

    public String getLongitudIni() {
        return longitudIni;
    }

    public void setLongitudIni(String longitudIni) {
        this.longitudIni = longitudIni;
    }

    public String getLatitudFin() {
        return latitudFin;
    }

    public void setLatitudFin(String latitudFin) {
        this.latitudFin = latitudFin;
    }

    public String getLongitudFin() {
        return longitudFin;
    }

    public void setLongitudFin(String longitudFin) {
        this.longitudFin = longitudFin;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

    public String getNombre_region() {
        return nombre_region;
    }

    public void setNombre_region(String nombre_region) {
        this.nombre_region = nombre_region;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getIdGiroNegocio() {
        return idGiroNegocio;
    }

    public void setIdGiroNegocio(String idGiroNegocio) {
        this.idGiroNegocio = idGiroNegocio;
    }

    public String getNombre_giroNegocio() {
        return nombre_giroNegocio;
    }

    public void setNombre_giroNegocio(String nombre_giroNegocio) {
        this.nombre_giroNegocio = nombre_giroNegocio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}

