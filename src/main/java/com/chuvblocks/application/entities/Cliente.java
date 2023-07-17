package com.chuvblocks.application.entities;

import java.util.Objects;

public class Cliente {
    private String nombre;
    private String apellido;
    private String cedula;

    public Cliente(String nombre, String apellido, String cedula) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente usuario = (Cliente) o;
        return Objects.equals(cedula, usuario.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
