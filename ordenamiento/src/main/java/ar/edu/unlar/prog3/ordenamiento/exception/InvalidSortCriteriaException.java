package ar.edu.unlar.prog3.ordenamiento.exception;

import java.util.List;

public class InvalidSortCriteriaException extends RuntimeException {
    private final String criterioRecibido;
    private final List<String> criteriosAceptados;

    public InvalidSortCriteriaException(String message, String criterioRecibido, List<String> criteriosAceptados) {
        super(message);
        this.criterioRecibido = criterioRecibido;
        this.criteriosAceptados = criteriosAceptados;
    }

    public String getCriterioRecibido() {
        return criterioRecibido;
    }

    public List<String> getCriteriosAceptados() {
        return criteriosAceptados;
    }
}
