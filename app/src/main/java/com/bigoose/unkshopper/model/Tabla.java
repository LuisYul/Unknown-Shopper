package com.bigoose.unkshopper.model;

public class Tabla {
    private String name;
    private String show;

    public Tabla(String name, String show) {
        this.name = name;
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
