package com.unlar.ordenamiento;

import com.unlar.ordenamiento.model.Estudiante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparableDemo {

    public static void main(String[] args) {
        List<Estudiante> estudiantes = new ArrayList<>(List.of(
                new Estudiante("LU-2024-001", "Martin Quiroga", 8.5, 22, 18),
                new Estudiante("LU-2024-002", "Valeria Diaz", 8.5, 20, 15),
                new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22),
                new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24),
                new Estudiante("LU-2024-005", "Lucas Gonzalez", 9.1, 23, 24)
        ));

        /*
         * Error original antes de implementar Comparable<Estudiante>:
         * no suitable method found for sort(java.util.List<com.unlar.ordenamiento.model.Estudiante>)
         * method java.util.Collections.<T>sort(java.util.List<T>) is not applicable
         * (inference variable T has incompatible bounds)
         */

        System.out.println("Antes de ordenar:");
        estudiantes.forEach(System.out::println);

        Collections.sort(estudiantes);

        System.out.println("\nDespues de ordenar por promedio descendente:");
        estudiantes.forEach(System.out::println);
    }
}
