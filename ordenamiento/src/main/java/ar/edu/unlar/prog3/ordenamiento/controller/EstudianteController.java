package ar.edu.unlar.prog3.ordenamiento.controller;

import ar.edu.unlar.prog3.ordenamiento.model.Estudiante;
import ar.edu.unlar.prog3.ordenamiento.repository.EstudianteRepository;
import ar.edu.unlar.prog3.ordenamiento.service.EstudianteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EstudianteController {
    private final EstudianteRepository estudianteRepository;
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteRepository estudianteRepository, EstudianteService estudianteService) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteService = estudianteService;
    }

    @GetMapping("/api/estudiantes")
    public List<Estudiante> listar(
            @RequestParam(defaultValue = "promedio") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return estudianteService.ordenar(estudianteRepository.findAll(), sortBy, order);
    }
}
