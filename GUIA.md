# GUIA PARA COMPLETAR EL TP 6 COMPARABLE / COMPARATOR

## Objetivo general

Completar un proyecto Spring Boot que demuestre el uso correcto de `Comparable`, `Comparator`, lambdas, method references, criterios compuestos, manejo de errores y seleccion dinamica de estrategias de ordenamiento mediante una API REST.

El proyecto debe compilar, ejecutar sin errores y contener respuestas conceptuales en `RESPUESTAS.md`.

## 1. Preparar proyecto Spring Boot

Crear o revisar un proyecto Maven con:

- Java 21.
- Spring Boot 3.4.x o superior.
- Dependencias: Spring Web, Lombok opcional, DevTools opcional.
- Estructura de paquetes ordenada.

Paquete usado en este proyecto:

```text
com.unlar.ordenamiento
```

Estructura esperada:

```text
src/main/java/com/unlar/ordenamiento
├── OrdenamientoApplication.java
├── controller
├── dto
├── exception
├── model
├── repository
└── service
```

## 2. Crear modelo `Estudiante`

Crear clase `Estudiante` con estos atributos privados:

- `String legajo`
- `String nombre`
- `double promedio`
- `int edad`
- `int cantidadMateriasAprobadas`

Agregar:

- Constructor vacio.
- Constructor completo.
- Getters y setters.
- `toString()`.
- `implements Comparable<Estudiante>`.

Implementar `compareTo()` para definir orden natural por `promedio` descendente:

```java
@Override
public int compareTo(Estudiante otro) {
    return Double.compare(otro.promedio, this.promedio);
}
```

No usar resta para comparar numeros.

## 3. Demostrar problema inicial con `Collections.sort`

Crear una clase de prueba o demo con una lista de al menos 5 estudiantes.

Antes de implementar `Comparable`, `Collections.sort(lista)` no compila porque `Estudiante` no cumple el contrato de comparacion natural.

El error de compilacion debe quedar copiado como comentario en el codigo.

Luego de implementar `Comparable`, mostrar:

- Lista antes de ordenar.
- Lista despues de ordenar.
- Promedios en orden descendente.

## 4. Crear repository en memoria

Crear `EstudianteRepository` como componente Spring.

Debe contener minimo 10 estudiantes cargados en memoria.

Los datos deben tener variedad y empates intencionales:

- Empates en `promedio`.
- Empates en `edad`.
- Empates en `cantidadMateriasAprobadas`.

Ejemplo:

```text
LU-2024-001, Martin Quiroga, 8.5, 22, 18
LU-2024-002, Valeria Diaz, 8.5, 20, 15
LU-2024-004, Camila Torres, 9.1, 21, 24
LU-2024-005, Lucas Gonzalez, 9.1, 23, 24
```

El repository debe devolver una copia de la lista para evitar modificar la lista interna.

## 5. Crear comparators externos

Crear demos o tests para probar distintos `Comparator<Estudiante>`.

Comparators obligatorios:

1. Lambda explicita para ordenar por `cantidadMateriasAprobadas` ascendente:

```java
(e1, e2) -> Integer.compare(
    e1.getCantidadMateriasAprobadas(),
    e2.getCantidadMateriasAprobadas()
)
```

2. Method reference para ordenar por nombre:

```java
Comparator.comparing(Estudiante::getNombre)
```

3. Method reference para ordenar por edad:

```java
Comparator.comparing(Estudiante::getEdad)
```

Mostrar listas ordenadas con cada criterio.

## 6. Crear criterios compuestos

Implementar comparators con:

- `thenComparing()`
- `reversed()`

Comparators requeridos:

1. Promedio descendente y, si empata, nombre ascendente.

```java
Comparator.comparingDouble(Estudiante::getPromedio)
    .reversed()
    .thenComparing(Estudiante::getNombre)
```

2. Promedio ascendente usando `.reversed()` sobre comparator existente.

3. Materias aprobadas descendente y, si empata, nombre ascendente.

```java
Comparator.comparingInt(Estudiante::getCantidadMateriasAprobadas)
    .reversed()
    .thenComparing(Estudiante::getNombre)
```

Verificar empates con datos cargados.

## 7. Demostrar anti-patron de resta

Crear comparator incorrecto:

```java
Comparator<Estudiante> restaTramposa =
    (e1, e2) -> e1.getEdad() - e2.getEdad();
```

Crear dos estudiantes con edades:

- `Integer.MAX_VALUE`
- `-1`

Demostrar que la resta produce orden incorrecto por overflow.

Luego corregir:

```java
Comparator<Estudiante> correcto =
    (e1, e2) -> Integer.compare(e1.getEdad(), e2.getEdad());
```

Verificar que ahora el orden es correcto.

## 8. Crear service con patron Strategy

Crear `EstudianteService`.

Debe tener un `Map<String, Comparator<Estudiante>>`.

Claves obligatorias:

- `promedio`
- `edad`
- `nombre`
- `materiasAprobadas`
- `legajo`

No usar `switch`.

No usar cadena larga de `if-else` para elegir comparator.

Cada comparator debe tener desempate predecible por `legajo`:

```java
Comparator.comparingDouble(Estudiante::getPromedio)
    .thenComparing(Estudiante::getLegajo)
```

Metodo requerido:

```java
public List<Estudiante> ordenar(List<Estudiante> lista, String sortBy, String order)
```

Debe:

- Buscar comparator en el mapa.
- Lanzar excepcion si `sortBy` no existe.
- Aceptar solo `asc` y `desc`.
- Aplicar `.reversed()` cuando `order` sea `desc`.
- Devolver una nueva lista ordenada.

## 9. Crear endpoint REST

Crear `EstudianteController`.

Endpoint requerido:

```text
GET /api/estudiantes?sortBy=promedio&order=asc
```

Parametros:

| Parametro | Default | Valores aceptados |
|---|---|---|
| `sortBy` | `promedio` | `promedio`, `edad`, `nombre`, `materiasAprobadas`, `legajo` |
| `order` | `asc` | `asc`, `desc` |

El controller debe:

- Recibir parametros.
- Pedir lista al repository.
- Delegar ordenamiento al service.
- Devolver JSON.

El controller no debe contener logica de comparators.

## 10. Manejar errores

Si cliente envia:

```text
GET /api/estudiantes?sortBy=colorFavorito
```

Debe responder HTTP 400.

JSON esperado:

```json
{
  "error": "Criterio de ordenamiento no valido",
  "criterioRecibido": "colorFavorito",
  "criteriosAceptados": ["promedio", "edad", "nombre", "materiasAprobadas", "legajo"]
}
```

Clases necesarias:

- `InvalidSortCriteriaException`
- `ErrorResponse`
- `GlobalExceptionHandler`

`GlobalExceptionHandler` debe usar `@RestControllerAdvice` y `@ExceptionHandler`.

## 11. Completar `RESPUESTAS.md`

Responder estas 5 preguntas:

1. Por que `Collections.sort()` no compila con `List<Estudiante>` antes de implementar `Comparable`.
2. Por que `promedio` fue elegido como orden natural y que pasaria si se pide ordenar por materias aprobadas.
3. Que problemas de diseno trae `Comparable` si se necesitan muchos criterios. Relacionar con SRP y OCP.
4. Que es overflow, por que la resta lo provoca, que contrato rompe y por que `Integer.compare()` es correcto.
5. Que patron se aplica con `Map<String, Comparator<T>>` y como se relaciona con polimorfismo.

Las respuestas deben explicar el por que, no solo repetir codigo.

## 12. Probar proyecto

Comandos:

```bash
mvn clean compile
mvn test
```

Si `mvn test` falla por dependencias externas, al menos verificar:

```bash
mvn test-compile
```

Probar endpoints:

```text
GET /api/estudiantes
GET /api/estudiantes?sortBy=nombre&order=asc
GET /api/estudiantes?sortBy=promedio&order=desc
GET /api/estudiantes?sortBy=materiasAprobadas&order=asc
GET /api/estudiantes?sortBy=inventado
```

Revisar:

- Orden correcto.
- Empates resueltos de forma predecible.
- Error HTTP 400 ante criterio invalido.

## 13. Commits recomendados

Hacer commits incrementales:

```text
feat: crear modelo estudiante comparable
feat: agregar comparators y demos
feat: implementar service de ordenamiento
feat: agregar endpoint rest de estudiantes
feat: manejar errores de criterios invalidos
docs: responder preguntas conceptuales
```

Minimo recomendado:

- Un commit por Parte 1.
- Un commit por Parte 2.
- Un commit por Parte 3.
- Un commit por Parte 4.

## Division de tareas

Orden pensado para trabajar en una sola computadora. Cada integrante toma su bloque completo, lo termina, prueba lo que corresponde y recien ahi pasa la computadora al siguiente.

### Integrante 1

Primer bloque de trabajo:

1. Revisar estructura Maven, paquetes y clase principal `OrdenamientoApplication`.
2. Crear o corregir paquete `model`.
3. Implementar clase `Estudiante` completa:
   - atributos privados.
   - constructor vacio.
   - constructor completo.
   - getters y setters.
   - `toString()`.
4. Implementar `Comparable<Estudiante>`.
5. Definir orden natural por `promedio` descendente usando `Double.compare()`.
6. Crear `ComparableDemo`.
7. Agregar comentario con error original de `Collections.sort(lista)` antes de implementar `Comparable`.
8. En `ComparableDemo`, mostrar lista antes y despues de ordenar.
9. Crear `ComparatorDemo`.
10. Implementar comparators simples:
    - materias aprobadas ascendente con lambda e `Integer.compare()`.
    - nombre ascendente con `Comparator.comparing(Estudiante::getNombre)`.
    - edad ascendente con `Comparator.comparing(Estudiante::getEdad)`.
11. Implementar comparators compuestos:
    - promedio descendente + nombre ascendente.
    - promedio ascendente usando `.reversed()`.
    - materias aprobadas descendente + nombre ascendente.
12. Ejecutar una prueba rapida de demos desde el IDE.
13. Revisar que no haya restas en comparaciones reales.
14. Guardar cambios.
15. Commit recomendado:

```text
feat: crear modelo estudiante y comparators
```

### Integrante 2

Segundo bloque de trabajo:

1. Tomar proyecto despues del commit del Integrante 1.
2. Crear o corregir paquete `repository`.
3. Implementar `EstudianteRepository` como `@Component`.
4. Cargar minimo 10 estudiantes en memoria.
5. Asegurar empates intencionales:
   - al menos dos estudiantes con mismo promedio.
   - al menos dos estudiantes con misma edad.
   - al menos dos estudiantes con misma cantidad de materias aprobadas.
6. Crear metodo `findAll()` que devuelva copia de la lista.
7. Crear o corregir paquete `service`.
8. Implementar `EstudianteService` como `@Service`.
9. Crear `Map<String, Comparator<Estudiante>>`.
10. Registrar claves:
    - `promedio`
    - `edad`
    - `nombre`
    - `materiasAprobadas`
    - `legajo`
11. Agregar desempate por `legajo` en comparators donde corresponda.
12. Implementar metodo `ordenar(List<Estudiante> lista, String sortBy, String order)`.
13. Validar criterio `sortBy`.
14. Validar orden `asc` o `desc`.
15. Aplicar `.reversed()` cuando `order` sea `desc`.
16. Devolver nueva lista ordenada, sin modificar lista original.
17. Crear tests del service en `OrdenamientoApplicationTests`.
18. Crear prueba de overflow:
    - comparator incorrecto con resta.
    - comparator correcto con `Integer.compare()`.
19. Crear o completar `OverflowDemo`.
20. Ejecutar:

```bash
mvn clean compile
mvn test-compile
```

21. Guardar cambios.
22. Commit recomendado:

```text
feat: implementar repository service y pruebas
```

### Integrante 3

Tercer bloque de trabajo:

1. Tomar proyecto despues del commit del Integrante 2.
2. Crear o corregir paquete `controller`.
3. Implementar `EstudianteController` como `@RestController`.
4. Crear endpoint:

```text
GET /api/estudiantes
```

5. Agregar parametros:
   - `sortBy` con default `promedio`.
   - `order` con default `asc`.
6. Conectar controller con `EstudianteRepository`.
7. Conectar controller con `EstudianteService`.
8. Verificar que controller no tenga logica de comparators.
9. Crear o corregir paquete `dto`.
10. Implementar `ErrorResponse`.
11. Crear o corregir paquete `exception`.
12. Implementar `InvalidSortCriteriaException`.
13. Implementar `GlobalExceptionHandler` con `@RestControllerAdvice`.
14. Hacer que criterio invalido devuelva HTTP 400.
15. Probar endpoints:

```text
GET /api/estudiantes
GET /api/estudiantes?sortBy=nombre&order=asc
GET /api/estudiantes?sortBy=promedio&order=desc
GET /api/estudiantes?sortBy=materiasAprobadas&order=asc
GET /api/estudiantes?sortBy=inventado
```

16. Completar `RESPUESTAS.md` con las 5 preguntas.
17. Revisar ortografia basica y formato final.
18. Ejecutar:

```bash
mvn clean compile
mvn test-compile
```

19. Si `mvn test` funciona en la computadora, ejecutarlo tambien.
20. Guardar cambios.
21. Commit recomendado:

```text
feat: agregar api rest errores y respuestas
```

### Orden de trabajo resumido

1. Integrante 1: modelo, `Comparable`, demos y comparators.
2. Integrante 2: repository, service, strategy map, tests y overflow.
3. Integrante 3: controller, errores REST, endpoints y respuestas finales.

## Checklist final

- `Estudiante` implementa `Comparable<Estudiante>`.
- `compareTo()` usa `Double.compare()`.
- Comparators usan `Integer.compare()`, `Comparator.comparing()` o `comparingInt/comparingDouble`.
- No hay resta en comparaciones reales.
- Repository tiene minimo 10 estudiantes.
- Datos tienen empates.
- Service usa `Map`, no `switch`.
- Controller no contiene logica de seleccion de comparators.
- Endpoint devuelve JSON ordenado.
- Error invalido devuelve HTTP 400.
- `RESPUESTAS.md` tiene 5 respuestas.
- Proyecto compila.
- Commits incrementales hechos.
