# Trabajo Práctico N° 6 — Ordenamiento de objetos: Comparable, Comparator y polimorfismo en acción

---

**Universidad Nacional de La Rioja**  
**Programación III — Paradigma Orientado a Objetos**  
**Año 2026**

### Grupo: IDK
Integrantes:

* Perez Joaquin EISI852
* Perez Valentina ELSI1240
* Brizuela Marco Ezequiel EISI676

---

## 1. Datos del TP

| | |
|---|---|
| **Modalidad** | Individual o en parejas |
| **Forma de entrega** | Repositorio Git (GitHub o GitLab) con **commits incrementales**. Cada ejercicio completado debe reflejarse en al menos un commit con mensaje descriptivo. La rama `main` debe contener el proyecto Spring Boot funcional al final del TP, más el archivo `RESPUESTAS.md`. |
| **Entregables** | 1. Proyecto Spring Boot Maven comprimido (`.zip`) o link al repositorio público. 2. Archivo `RESPUESTAS.md` con las respuestas a las preguntas conceptuales. |

---

## 2. Objetivos de aprendizaje

Al finalizar este TP el estudiante será capaz de:

1. **Distinguir** el orden natural de los órdenes alternativos y explicar cuándo corresponde usar `Comparable` y cuándo `Comparator`.
2. **Implementar** las interfaces `Comparable<T>` y `Comparator<T>` como estrategias concretas de comparación, incluyendo sus versiones funcionales con lambdas y method references en Java 21.
3. **Justificar** decisiones de diseño en términos de acoplamiento, cohesión y principios de orientación a objetos.
4. **Exponer** comportamiento polimórfico a través de una API REST en Spring Boot que selecciona dinámicamente la estrategia de ordenamiento según parámetros del request.
5. **Detectar y corregir** el anti-patrón de la resta en comparaciones de enteros, comprendiendo el contrato de `compareTo`/`compare` y el riesgo de overflow.
6. **Documentar** el razonamiento detrás de cada decisión de diseño en un archivo técnico (`RESPUESTAS.md`).

---

## 3. Marco conceptual

### ¿Por qué necesitamos comparar objetos?

Cuando trabajamos con colecciones de objetos propios (`List<Estudiante>`, `Set<Producto>`, etc.) y queremos ordenarlas —por ejemplo con `Collections.sort()` o `list.sort()`— Java nos frena con un error de compilación. El motivo es simple: **Java no tiene forma de saber cuál de nuestros objetos va primero**. A diferencia de `Integer`, `String` o `LocalDate`, nuestras clases no traen incorporada una noción de orden.

La solución está en dos interfaces del corazón de Java que modelan la comparación como un **comportamiento** (y en POO, los comportamientos se modelan con interfaces):

|  | `Comparable<T>` | `Comparator<T>` |
|---|---|---|
| **Dónde vive la lógica** | Dentro de la clase de dominio | En una lambda, method reference o clase externa |
| **Método clave** | `int compareTo(T o)` | `int compare(T o1, T o2)` |
| **Cantidad de criterios posibles** | **Uno solo** (el orden natural) | **Infinitos** (uno por cada comparator) |
| **Acoplamiento con el dominio** | Alto: modificás la clase | Bajo: la clase no se entera |
| **¿Cuándo usarlo?** | Cuando hay un orden que es "el obvio" para ese tipo de objeto | Cuando necesitás múltiples criterios, o no podés modificar la clase, o no querés "ensuciar" el dominio |

### El contrato

Ambas interfaces se basan en el mismo contrato: el método devuelve un `int` que es:
- **negativo** si `this` (o `o1`) va **antes** que el otro,
- **cero** si son **iguales** según ese criterio,
- **positivo** si `this` (o `o1`) va **después**.

Este contrato debe ser **consistente con equals()** (aunque Java no lo fuerza), **transitivo** (si A < B y B < C, entonces A < C) y **simétrico** (`signo(compare(a,b)) == -signo(compare(b,a))`).

### ⚠️ Advertencia: el anti-patrón de la resta

Un "truco" que circula por Internet propone implementar la comparación de enteros así:

```java
// ❌ INCORRECTO: no hagas esto
(p1, p2) -> p1.getPromedio() - p2.getPromedio();
```

Parece inofensivo, pero **falla estrepitosamente por overflow de enteros**. Si un estudiante tiene promedio `Integer.MAX_VALUE` y otro `-1`, la resta produce un valor incorrecto y el orden resultante es erróneo, violando el contrato de la interfaz. La forma correcta es usar los métodos `compare` de las clases wrapper:

```java
// ✅ CORRECTO
(p1, p2) -> Integer.compare(p1.getPromedio(), p2.getPromedio());
```

Este será uno de los ejercicios del TP. No lo olvides.

---

## 4. Dominio del TP: `Estudiante`

Trabajaremos con la siguiente clase de dominio durante todo el práctico:

| Atributo | Tipo | Descripción |
|---|---|---|
| `legajo` | `String` | Identificador único (ej. `"LU-2024-001"`) |
| `nombre` | `String` | Nombre completo |
| `promedio` | `double` | Promedio académico (con decimales) |
| `edad` | `int` | Edad en años |
| `cantidadMateriasAprobadas` | `int` | Materias rendidas y aprobadas |

Elegimos este dominio porque es cercano a la realidad universitaria y permite visualizar claramente distintos criterios de orden: por mérito académico (`promedio`), por trayectoria (`cantidadMateriasAprobadas`), por edad o alfabéticamente.

---

## 5. Setup del proyecto Spring Boot

### 5.1. Crear el proyecto

Usá Spring Initializr ([start.spring.io](https://start.spring.io)) con las siguientes opciones:

| Campo | Valor |
|---|---|
| **Project** | Maven |
| **Language** | Java |
| **Spring Boot** | 3.4.x o superior |
| **Java** | 21 |
| **Group** | `ar.edu.unlar.prog3` |
| **Artifact** | `tp-comparable-comparator` |
| **Dependencies** | Spring Web, Lombok (opcional), Spring Boot DevTools |

### 5.2. Repositorio en memoria

**No usaremos base de datos.** Creá un componente con al menos **10 estudiantes** precargados usando `@PostConstruct` o `@Bean`. Los datos deben ser variados y contener **empates intencionales** en al menos dos atributos, de modo que sea visible el efecto de `thenComparing()` para desempatar.

Ejemplo de datos sugeridos (podés elegir otros, pero mantené variedad y empates):

```
"LU-2024-001", "Martín Quiroga",     8.5, 22, 18
"LU-2024-002", "Valeria Díaz",       8.5, 20, 15   ← empata promedio con Martín
"LU-2024-003", "Facundo Castro",     7.2, 24, 22
"LU-2024-004", "Camila Torres",      9.1, 21, 24
"LU-2024-005", "Lucas González",     9.1, 23, 24   ← empata promedio con Camila
"LU-2024-006", "Agustina López",     6.8, 19, 10
"LU-2024-007", "Nahuel Herrera",     7.5, 22, 14   ← empata edad con Martín
"LU-2024-008", "Florencia Ríos",     8.9, 25, 20
"LU-2024-009", "Tomás Sosa",         6.5, 20, 12   ← empata edad con Valeria
"LU-2024-010", "Lucía Fernández",    7.8, 21, 16   ← empata edad con Camila y Lucas
```

---

## 6. Ejercicios

### Parte 1 — El problema

---

#### Ejercicio 1: El error que dispara todo

1. Creá la clase `Estudiante` como POJO (atributos privados, constructor, getters, setters, `toString()`).
2. En un `main` de prueba —todavía no estamos en Spring— creá una `ArrayList<Estudiante>` con al menos 5 estudiantes e intentá ordenarla con `Collections.sort(lista)`.
3. **Capturá el error de compilación** (no de ejecución) que produce esa línea. Copialo textual en un comentario dentro del código.
4. En `RESPUESTAS.md`, respondé:

> **Pregunta 1:** ¿Por qué `Collections.sort()` no compila cuando le pasamos una `List<Estudiante>`? ¿Qué contrato exige Java que nuestra clase no está cumpliendo?

---

### Parte 2 — Comparable: el orden natural

---

#### Ejercicio 2: Implementar `Comparable<Estudiante>`

1. Hacé que `Estudiante` implemente la interfaz `Comparable<Estudiante>`.
2. Sobrescribí el método `compareTo()` para que el **orden natural** sea por `promedio` de mayor a menor (orden descendente por mérito académico).
3. **Antes de codificar**, respondé en `RESPUESTAS.md`:

> **Pregunta 2:** ¿Por qué elegiste el atributo `promedio` como orden natural? ¿Qué pasaría si mañana un nuevo requisito pide ordenar por `cantidadMateriasAprobadas`? ¿Modificarías `compareTo`? ¿Qué consecuencias tendría?

4. Verificá en un `main` que la lista se ordena correctamente por promedio descendente.
5. Asegurate de usar `Double.compare()` (no resta) dentro de `compareTo()`.

**Criterio de aceptación:** Mostrar la lista antes y después de ordenar, con promedios en orden decreciente.

---

#### Ejercicio 3: Reflexión sobre la limitación de `Comparable`

Sin escribir código, respondé en `RESPUESTAS.md`:

> **Pregunta 3:** `Comparable` nos ata a un único criterio de ordenamiento. ¿Qué problemas de diseño introduce esto si nuestro sistema necesitara ordenar la misma lista de estudiantes de 4 formas distintas según el contexto? Relacioná tu respuesta con los principios de responsabilidad única (SRP) y abierto/cerrado (OCP).

---

### Parte 3 — Comparator: estrategias externas de comparación

---

#### Ejercicio 4: Comparators con lambdas y method references

Creá comparators para al menos **tres atributos distintos** usando las herramientas de Java 8+:

1. **Con lambda explícita:** definí un `Comparator<Estudiante>` mediante una expresión lambda `(e1, e2) -> ...` que ordene por `cantidadMateriasAprobadas` ascendente. Usá `Integer.compare()` (no resta).
2. **Con `Comparator.comparing()` + method reference:** definí comparators para `nombre` (alfabético) y `edad` (ascendente) usando `Comparator.comparing(Estudiante::getNombre)` y `Comparator.comparing(Estudiante::getEdad)`.
3. Verificá los tres en un `main` con `list.sort(comparator)`.

**Criterio de aceptación:** Mostrar la lista ordenada con cada uno de los tres criterios. Los resultados deben ser verificables visualmente.

---

#### Ejercicio 5: Criterios compuestos y orden inverso

1. **Desempate con `thenComparing()`:** creá un comparator que ordene **primero por promedio descendente y, en caso de empate, por nombre alfabéticamente ascendente**. Usá tus datos de prueba con empates intencionales para comprobar que el desempate funciona.
2. **Orden inverso con `reversed()`:** a partir del comparator de promedio descendente, generá uno que ordene por promedio **ascendente** sin reescribir la lógica (simplemente encadenando `.reversed()`).
3. **Combiná todo:** definí un comparator que ordene por `cantidadMateriasAprobadas` descendente y, a igual cantidad, por `nombre` ascendente.

**Criterio de aceptación:** Los tres comparators deben funcionar con `list.sort(comparator)`. Los empates en el primer criterio deben resolverse por el segundo.

---

#### Ejercicio 6: El anti-patrón de la resta

1. Escribí **a propósito** un comparator con el "truco de la resta" para ordenar por edad:

   ```java
   Comparator<Estudiante> restaTramposa =
       (e1, e2) -> e1.getEdad() - e2.getEdad();
   ```

2. Creá un test (podés usar un `main` o, mejor, JUnit 5) donde dos estudiantes tengan edades `Integer.MAX_VALUE` (2147483647) y `-1`. Verificá que el orden producido es **incorrecto**.
3. Corregilo con `Integer.compare()` y verificá que ahora el orden es el esperado.
4. En `RESPUESTAS.md`, respondé:

> **Pregunta 4:** Explicá con tus palabras qué es un overflow de enteros, por qué el "truco de la resta" lo provoca, qué parte del contrato de `Comparator` rompe, y por qué `Integer.compare()` no sufre este problema.

**Criterio de aceptación:** El test con `Integer.MAX_VALUE` y `-1` debe fallar con la resta y funcionar correctamente con `Integer.compare()`.

---

### Parte 4 — Integración con Spring Boot

---

#### Ejercicio 7: Endpoint REST de estudiantes ordenados

Creá un `@RestController` con el siguiente endpoint:

```
GET /api/estudiantes?sortBy=promedio&order=asc
```

| Query param | Tipo | Default | Valores aceptados |
|---|---|---|---|
| `sortBy` | `String` | `"promedio"` | `"promedio"`, `"edad"`, `"nombre"`, `"materiasAprobadas"`, `"legajo"` |
| `order` | `String` | `"asc"` | `"asc"`, `"desc"` |

El endpoint debe devolver la lista de estudiantes en formato JSON, ordenada según los parámetros.

**Criterio de aceptación:** Probá con al menos 3 combinaciones distintas de `sortBy` y `order` usando el navegador o Postman. El JSON debe reflejar el orden correcto. Los empates deben resolverse de forma predecible (usando `thenComparing` por legajo como tie-breaker por defecto).

---

#### Ejercicio 8: Service con patrón Strategy

**NO uses un `switch` ni una cadena de `if-else` para elegir el comparator.** En su lugar:

1. Creá un `@Service` (ej. `EstudianteService`) que contenga un `Map<String, Comparator<Estudiante>>` donde la clave es el valor de `sortBy` y el valor es el comparator correspondiente (construido con `Comparator.comparing()` y method references).
2. El método `ordenar(List<Estudiante> lista, String sortBy, String order)` debe:
   - Buscar el comparator en el mapa.
   - Si no existe, lanzar una excepción (que el controller convertirá en HTTP 400).
   - Aplicar `.reversed()` si `order` es `"desc"`.
   - Devolver la lista ordenada.
3. En `RESPUESTAS.md`, respondé:

> **Pregunta 5:** ¿Qué patrón de diseño estás aplicando al usar un `Map<String, Comparator<T>>` en lugar de un `switch`? Explicá cómo se relaciona este patrón con el polimorfismo y por qué es preferible a la alternativa procedural.

**Criterio de aceptación:** El controller no contiene lógica de selección de comparators. Toda esa responsabilidad está en el service.

---

#### Ejercicio 9: Manejo de errores

Si el cliente envía `?sortBy=colorFavorito`, la API debe responder con HTTP **400 Bad Request** y un cuerpo JSON como este:

```json
{
  "error": "Criterio de ordenamiento no válido",
  "criterioRecibido": "colorFavorito",
  "criteriosAceptados": ["promedio", "edad", "nombre", "materiasAprobadas", "legajo"]
}
```

Implementá este manejo de errores de forma prolija (podés usar `@ExceptionHandler`, `ResponseEntityExceptionHandler` o simplemente devolver `ResponseEntity.badRequest()`).

**Criterio de aceptación:** `GET /api/estudiantes?sortBy=inventado` debe devolver HTTP 400 con el JSON de error descriptivo.

---

### Parte 5 — Opcional (nota extra)

---

#### Ejercicio 10 (opcional): Orden multinivel o tests parametrizados

Elegí **UNA** de las siguientes opciones:

**Opción A — Orden multinivel parametrizable:** Extendé el endpoint para que acepte múltiples criterios separados por coma:

```
GET /api/estudiantes?sortBy=promedio,edad&order=desc,asc
```

El primer criterio es el principal, el segundo desempata, y así sucesivamente. Cada uno puede tener su propia dirección.

**Opción B — Tests con JUnit 5 parametrizado:** Escribí al menos 4 tests parametrizados con `@MethodSource` o `@CsvSource` que verifiquen que el service devuelve el orden correcto para distintas combinaciones de `sortBy` y `order`.

**Opción C — Comparación con `Collator` para nombres con tildes:** Modificá el comparator de nombre para que use `java.text.Collator` con `Collator.PRIMARY` y así manejar correctamente tildes y ñ (ej. "Álvarez" antes de "Benítez", "Nuñez" después de "Molina"). Escribí un test que lo demuestre.

---

## 7. Rúbrica de evaluación

| Criterio | Peso | Descripción |
|---|---|---|
| **Funcionamiento de ejercicios** | 40% | El proyecto compila y ejecuta sin errores. Cada ejercicio produce el resultado esperado. Los endpoints responden correctamente ante entradas válidas e inválidas. |
| **Calidad de diseño y uso correcto de interfaces** | 25% | Uso apropiado de `Comparable` y `Comparator`. Los comparators están implementados con `Integer.compare()`/`Double.compare()` (sin restas). El service usa un `Map` de estrategias (no `switch`). El controller está limpio de lógica de selección. |
| **Respuestas conceptuales** | 20% | `RESPUESTAS.md` contiene respuestas completas, fundamentadas, con vocabulario técnico preciso. Se nota que el alumno entendió el *por qué*, no solo el *cómo*. |
| **Historia de commits y prolijidad** | 15% | Repositorio con commits incrementales y mensajes descriptivos. El código sigue las convenciones de Java (nombres en inglés, camelCase, indentación consistente). El proyecto está bien estructurado en paquetes. |

### Condición mínima de aprobación

- Los ejercicios del 1 al 9 deben compilar y funcionar correctamente.
- `RESPUESTAS.md` debe tener respondidas las 5 preguntas conceptuales.
- El repositorio debe tener al menos **un commit por cada parte** (Parte 1, Parte 2, Parte 3, Parte 4).

---

## 8. Bibliografía

- Baeldung. *"Comparator and Comparable in Java"*. [https://www.baeldung.com/java-comparator-comparable](https://www.baeldung.com/java-comparator-comparable)
- Oracle. *Interface Comparable\<T\>*. Java Platform SE 21 & JDK 21. [https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html)
- Oracle. *Interface Comparator\<T\>*. Java Platform SE 21 & JDK 21. [https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Comparator.html](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Comparator.html)
- Spring Boot Reference Documentation. [https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley. (Patrón Strategy, pp. 315–323).

---

*Cátedra Programación III — UNLaR, 2026.*
