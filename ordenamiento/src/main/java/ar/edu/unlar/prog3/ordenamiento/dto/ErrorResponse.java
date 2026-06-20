package ar.edu.unlar.prog3.ordenamiento.dto;

import java.util.List;

public class ErrorResponse {
    private String error;
    private String criterioRecibido;
    private List<String> criteriosAceptados;

    public ErrorResponse(String error, String criterioRecibido, List<String> criteriosAceptados) {
        this.error = error;
        this.criterioRecibido = criterioRecibido;
        this.criteriosAceptados = criteriosAceptados;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCriterioRecibido() {
        return criterioRecibido;
    }

    public void setCriterioRecibido(String criterioRecibido) {
        this.criterioRecibido = criterioRecibido;
    }

    public List<String> getCriteriosAceptados() {
        return criteriosAceptados;
    }

    public void setCriteriosAceptados(List<String> criteriosAceptados) {
        this.criteriosAceptados = criteriosAceptados;
    }
}
