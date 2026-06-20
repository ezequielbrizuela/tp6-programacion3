package ar.edu.unlar.prog3.ordenamiento;

import ar.edu.unlar.prog3.ordenamiento.model.Estudiante;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OverflowDemo {

    public static void main(String[] args) {
        Comparator<Estudiante> restaTramposa =
                (e1, e2) -> e1.getEdad() - e2.getEdad();
        Comparator<Estudiante> comparacionCorrecta =
                (e1, e2) -> Integer.compare(e1.getEdad(), e2.getEdad());

        System.out.println("Con resta tramposa:");
        ordenar(restaTramposa).forEach(System.out::println);

        System.out.println("\nCon Integer.compare:");
        ordenar(comparacionCorrecta).forEach(System.out::println);
    }

    private static List<Estudiante> ordenar(Comparator<Estudiante> comparator) {
        List<Estudiante> estudiantes = new ArrayList<>(List.of(
                new Estudiante("LU-OVER-001", "Edad Maxima", 8.0, Integer.MAX_VALUE, 1),
                new Estudiante("LU-OVER-002", "Edad Negativa", 8.0, -1, 1)
        ));
        estudiantes.sort(comparator);
        return estudiantes;
    }
}
