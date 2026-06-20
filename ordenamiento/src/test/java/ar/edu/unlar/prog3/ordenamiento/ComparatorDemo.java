package ar.edu.unlar.prog3.ordenamiento;

import ar.edu.unlar.prog3.ordenamiento.model.Estudiante;

import java.util.Comparator;
import java.util.List;

public class ComparatorDemo {

    public static void main(String[] args) {
        Comparator<Estudiante> porMateriasAsc =
                (e1, e2) -> Integer.compare(e1.getCantidadMateriasAprobadas(), e2.getCantidadMateriasAprobadas());
        Comparator<Estudiante> porNombreAsc = Comparator.comparing(Estudiante::getNombre);
        Comparator<Estudiante> porEdadAsc = Comparator.comparing(Estudiante::getEdad);
        Comparator<Estudiante> porPromedioDesc = Comparator.comparingDouble(Estudiante::getPromedio).reversed();
        Comparator<Estudiante> porPromedioDescNombreAsc = porPromedioDesc
                .thenComparing(Estudiante::getNombre);
        Comparator<Estudiante> porPromedioAsc = porPromedioDesc.reversed();
        Comparator<Estudiante> porMateriasDescNombreAsc = Comparator.comparingInt(Estudiante::getCantidadMateriasAprobadas)
                .reversed()
                .thenComparing(Estudiante::getNombre);

        mostrar("Materias aprobadas ascendente", porMateriasAsc);
        mostrar("Nombre alfabetico ascendente", porNombreAsc);
        mostrar("Edad ascendente", porEdadAsc);
        mostrar("Promedio descendente y nombre ascendente", porPromedioDescNombreAsc);
        mostrar("Promedio ascendente usando reversed", porPromedioAsc);
        mostrar("Materias aprobadas descendente y nombre ascendente", porMateriasDescNombreAsc);
    }

    private static void mostrar(String titulo, Comparator<Estudiante> comparator) {
        List<Estudiante> estudiantes = estudiantes();
        estudiantes.sort(comparator);
        System.out.println("\n" + titulo);
        estudiantes.forEach(System.out::println);
    }

    private static List<Estudiante> estudiantes() {
        return List.of(
                new Estudiante("LU-2024-001", "Martin Quiroga", 8.5, 22, 18),
                new Estudiante("LU-2024-002", "Valeria Diaz", 8.5, 20, 15),
                new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22),
                new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24),
                new Estudiante("LU-2024-005", "Lucas Gonzalez", 9.1, 23, 24),
                new Estudiante("LU-2024-006", "Agustina Lopez", 6.8, 19, 10),
                new Estudiante("LU-2024-007", "Nahuel Herrera", 7.5, 22, 14),
                new Estudiante("LU-2024-008", "Florencia Rios", 8.9, 25, 20),
                new Estudiante("LU-2024-009", "Tomas Sosa", 6.5, 20, 12),
                new Estudiante("LU-2024-010", "Lucia Fernandez", 7.8, 21, 16)
        );
    }
}
