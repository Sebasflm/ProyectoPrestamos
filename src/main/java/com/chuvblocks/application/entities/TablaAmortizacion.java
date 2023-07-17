package com.chuvblocks.application.entities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TablaAmortizacion {
    private int numeroCuota;
    private double capitalAmortizado;
    private double interes;
    private double cuota;
    private double saldoPendiente;

    public TablaAmortizacion(int numeroCuota, double capitalAmortizado, double interes, double cuota, double saldoPendiente) {
        this.numeroCuota = numeroCuota;
        this.capitalAmortizado = capitalAmortizado;
        this.interes = interes;
        this.cuota = cuota;
        this.saldoPendiente = saldoPendiente;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public double getCapitalAmortizado() {
        return capitalAmortizado;
    }

    public String getCapitalAmortizadoAsString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "$" + df.format(capitalAmortizado);
    }

    public double getInteres() {
        return interes;
    }

    public String getInteresAsString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "$" + df.format(interes);
    }

    public double getCuota() {
        return cuota;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public String getSaldoPendienteAsString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "$" + df.format(saldoPendiente);
    }

    public static List<TablaAmortizacion> generarTablaAmortizacion(Credito credito) {
        double saldoPendiente = credito.getPrestamo();
        double tasaInteresMensual = credito.getTasaInteres() / 12.0;
        int plazo = credito.getPlazo();
        double cuota = credito.getValorCuotas();

        List<TablaAmortizacion> tablaAmortizacion = new ArrayList<>();

        for (int i = 1; i <= plazo; i++) {
            double interes = saldoPendiente * tasaInteresMensual;
            double capitalAmortizado = cuota - interes;

            if (i == plazo) {
                saldoPendiente = 0;
            } else {
                saldoPendiente -= capitalAmortizado;
            }
            tablaAmortizacion.add(new TablaAmortizacion(i, capitalAmortizado, interes, cuota, saldoPendiente));

            if (saldoPendiente < 0) {
                saldoPendiente = 0;
            }
        }

        return tablaAmortizacion;
    }
}
