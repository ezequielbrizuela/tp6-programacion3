package ar.edu.unlar.prog3.ordenamiento.repository;

import ar.edu.unlar.prog3.ordenamiento.model.Estudiante;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EstudianteRepository {
    private final List<Estudiante> estudiantes = new ArrayList<>();

    @PostConstruct
    public void cargarEstudiantes() {
        estudiantes.add(new Estudiante("LU-2024-001", "Martin Quiroga", 8.5, 22, 18));
        estudiantes.add(new Estudiante("LU-2024-002", "Valeria Diaz", 8.5, 20, 15));
        estudiantes.add(new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22));
        estudiantes.add(new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24));
        estudiantes.add(new Estudiante("LU-2024-005", "Lucas Gonzalez", 9.1, 23, 24));
        estudiantes.add(new Estudiante("LU-2024-006", "Agustina Lopez", 6.8, 19, 10));
        estudiantes.add(new Estudiante("LU-2024-007", "Nahuel Herrera", 7.5, 22, 14));
        estudiantes.add(new Estudiante("LU-2024-008", "Florencia Rios", 8.9, 25, 20));
        estudiantes.add(new Estudiante("LU-2024-009", "Tomas Sosa", 6.5, 20, 12));
        estudiantes.add(new Estudiante("LU-2024-010", "Lucia Fernandez", 7.8, 21, 16));
    }

    public List<Estudiante> findAll() {
        return new ArrayList<>(estudiantes);
    }
}
