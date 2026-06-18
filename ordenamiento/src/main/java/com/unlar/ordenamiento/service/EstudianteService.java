package com.unlar.ordenamiento.service;

import com.unlar.ordenamiento.exception.InvalidSortCriteriaException;
import com.unlar.ordenamiento.model.Estudiante;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstudianteService {
    private final Map<String, Comparator<Estudiante>> comparators;

    public EstudianteService() {
        comparators = new LinkedHashMap<>();
        comparators.put("promedio", Comparator.comparingDouble(Estudiante::getPromedio)
                .thenComparing(Estudiante::getLegajo));
        comparators.put("edad", Comparator.comparingInt(Estudiante::getEdad)
                .thenComparing(Estudiante::getLegajo));
        comparators.put("nombre", Comparator.comparing(Estudiante::getNombre)
                .thenComparing(Estudiante::getLegajo));
        comparators.put("materiasAprobadas", Comparator.comparingInt(Estudiante::getCantidadMateriasAprobadas)
                .thenComparing(Estudiante::getLegajo));
        comparators.put("legajo", Comparator.comparing(Estudiante::getLegajo));
    }

    public List<Estudiante> ordenar(List<Estudiante> lista, String sortBy, String order) {
        Comparator<Estudiante> comparator = comparators.get(sortBy);
        if (comparator == null) {
            throw new InvalidSortCriteriaException(
                    "Criterio de ordenamiento no valido",
                    sortBy,
                    criteriosAceptados()
            );
        }

        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            throw new InvalidSortCriteriaException(
                    "Orden no valido",
                    order,
                    List.of("asc", "desc")
            );
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        List<Estudiante> ordenados = new ArrayList<>(lista);
        ordenados.sort(comparator);
        return ordenados;
    }

    public List<String> criteriosAceptados() {
        return new ArrayList<>(comparators.keySet());
    }
}
