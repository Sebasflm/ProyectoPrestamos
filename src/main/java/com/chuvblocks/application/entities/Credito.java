package com.chuvblocks.application.entities;

import java.text.DecimalFormat;
import java.util.Objects;

public class Credito {
    private Cliente cliente;
    private double prestamo;
    private double tasaInteres;
    private int plazo;
    private double valorCuotas;

    public Credito(Cliente cliente, double prestamo, double tasaInteres, int plazo) {
        this.cliente = cliente;
        this.prestamo = prestamo;
        this.tasaInteres = tasaInteres;
        this.plazo = plazo;
        calcularValorCuotas();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credito credito = (Credito) o;
        return Objects.equals(cliente, credito.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(double prestamo) {
        this.prestamo = prestamo;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public double getValorCuotas() {
        return valorCuotas;
    }

    public void setValorCuotas(double valorCuotas) {
        this.valorCuotas = valorCuotas;
    }

    public void calcularValorCuotas() {
        double tasaInteresMensual = tasaInteres / 12.0;
        double factor = Math.pow(1 + tasaInteresMensual, plazo);
        valorCuotas = prestamo * (tasaInteresMensual * factor) / (factor - 1);
    }
}
