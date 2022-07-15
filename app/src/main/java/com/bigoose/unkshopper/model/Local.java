package com.bigoose.unkshopper.model;

public class Local {

    private String codigo;
    private String nombre;
    private String distrito;
    private String direccion;

    public Local(String codigo, String nombre, String distrito, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.distrito = distrito;
        this.direccion = direccion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
