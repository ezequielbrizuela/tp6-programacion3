package com.unlar.ordenamiento;

import com.unlar.ordenamiento.exception.InvalidSortCriteriaException;
import com.unlar.ordenamiento.model.Estudiante;
import com.unlar.ordenamiento.service.EstudianteService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrdenamientoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void comparableOrdenaPorPromedioDescendente() {
		List<Estudiante> estudiantes = new ArrayList<>(estudiantes());

		estudiantes.sort(Comparator.naturalOrder());

		assertEquals("LU-2024-004", estudiantes.get(0).getLegajo());
		assertEquals("LU-2024-006", estudiantes.get(estudiantes.size() - 1).getLegajo());
	}

	@Test
	void serviceOrdenaPorNombreAscendente() {
		EstudianteService service = new EstudianteService();

		List<Estudiante> ordenados = service.ordenar(estudiantes(), "nombre", "asc");

		assertEquals("Agustina Lopez", ordenados.get(0).getNombre());
		assertEquals("Valeria Diaz", ordenados.get(ordenados.size() - 1).getNombre());
	}

	@Test
	void serviceRechazaCriterioInvalido() {
		EstudianteService service = new EstudianteService();

		InvalidSortCriteriaException exception = assertThrows(
				InvalidSortCriteriaException.class,
				() -> service.ordenar(estudiantes(), "colorFavorito", "asc")
		);

		assertEquals("colorFavorito", exception.getCriterioRecibido());
	}

	@Test
	void restaTramposaFallaConOverflow() {
		Comparator<Estudiante> restaTramposa =
				(e1, e2) -> e1.getEdad() - e2.getEdad();
		List<Estudiante> estudiantes = edadesExtremas();

		estudiantes.sort(restaTramposa);

		assertEquals("LU-OVER-001", estudiantes.get(0).getLegajo());
	}

	@Test
	void integerCompareOrdenaCorrectamenteEdadesExtremas() {
		Comparator<Estudiante> comparacionCorrecta =
				(e1, e2) -> Integer.compare(e1.getEdad(), e2.getEdad());
		List<Estudiante> estudiantes = edadesExtremas();

		estudiantes.sort(comparacionCorrecta);

		assertEquals("LU-OVER-002", estudiantes.get(0).getLegajo());
	}

	private List<Estudiante> estudiantes() {
		return List.of(
				new Estudiante("LU-2024-001", "Martin Quiroga", 8.5, 22, 18),
				new Estudiante("LU-2024-002", "Valeria Diaz", 8.5, 20, 15),
				new Estudiante("LU-2024-003", "Facundo Castro", 7.2, 24, 22),
				new Estudiante("LU-2024-004", "Camila Torres", 9.1, 21, 24),
				new Estudiante("LU-2024-005", "Lucas Gonzalez", 9.1, 23, 24),
				new Estudiante("LU-2024-006", "Agustina Lopez", 6.8, 19, 10)
		);
	}

	private List<Estudiante> edadesExtremas() {
		return new ArrayList<>(List.of(
				new Estudiante("LU-OVER-001", "Edad Maxima", 8.0, Integer.MAX_VALUE, 1),
				new Estudiante("LU-OVER-002", "Edad Negativa", 8.0, -1, 1)
		));
	}

}
