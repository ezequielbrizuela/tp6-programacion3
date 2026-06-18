package com.unlar.ordenamiento.model;

public class Estudiante implements Comparable<Estudiante> {
    private String legajo;
    private String nombre;
    private double promedio;
    private int edad;
    private int cantidadMateriasAprobadas;

    public Estudiante() {
    }

    public Estudiante(String legajo, String nombre, double promedio, int edad, int cantidadMateriasAprobadas) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.promedio = promedio;
        this.edad = edad;
        this.cantidadMateriasAprobadas = cantidadMateriasAprobadas;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getCantidadMateriasAprobadas() {
        return cantidadMateriasAprobadas;
    }

    public void setCantidadMateriasAprobadas(int cantidadMateriasAprobadas) {
        this.cantidadMateriasAprobadas = cantidadMateriasAprobadas;
    }

    @Override
    public int compareTo(Estudiante otro) {
        return Double.compare(otro.promedio, this.promedio);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "legajo='" + legajo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", promedio=" + promedio +
                ", edad=" + edad +
                ", cantidadMateriasAprobadas=" + cantidadMateriasAprobadas +
                '}';
    }
}
