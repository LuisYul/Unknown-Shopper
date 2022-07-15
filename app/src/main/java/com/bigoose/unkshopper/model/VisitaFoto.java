package com.bigoose.unkshopper.model;

public class VisitaFoto {
    private String idVisita;
    private String hora;
    private String idCriterio;
    private String comentario;
    private byte[] fotoByte;
    private String indice;
    private String idTipo;
    private String latitud;
    private String longitud;
    private String estado;
    private String tipo;
    private String foto;

    public VisitaFoto(String idCriterio, String comentario, byte[] fotoByte) {
        this.idCriterio = idCriterio;
        this.comentario = comentario;
        this.fotoByte = fotoByte;
    }

    public VisitaFoto() {
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

    public String getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(String idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }



    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
