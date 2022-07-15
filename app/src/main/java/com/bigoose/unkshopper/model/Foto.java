package com.bigoose.unkshopper.model;

public class Foto {
    private String tipo;
    private byte[] foto;
    private String comentario;

    public Foto(String tipo, byte[] foto, String comentario) {
        this.tipo = tipo;
        this.foto = foto;
        this.comentario = comentario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
