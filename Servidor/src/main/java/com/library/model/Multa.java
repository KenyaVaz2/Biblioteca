package com.library.model;


public class Multa {


    private Integer idMulta;
    private Float monto;
    private String estadoPago;
    private Prestamo prestamo;


    public Multa() {
    }

    public Multa(Float monto, String estadoPago, Prestamo prestamo) {
        this.monto = monto;
        this.estadoPago = estadoPago;
        this.prestamo = prestamo;
    }

    public Integer getIdMulta() {
        return idMulta;
    }

    public void setIdMulta(Integer idMulta) {
        this.idMulta = idMulta;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }
}