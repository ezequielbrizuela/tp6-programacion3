package com.unlar.ordenamiento.controller;

import com.unlar.ordenamiento.model.Estudiante;
import com.unlar.ordenamiento.repository.EstudianteRepository;
import com.unlar.ordenamiento.service.EstudianteService;
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
