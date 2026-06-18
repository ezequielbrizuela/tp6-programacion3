# RESPUESTAS

## Pregunta 1

`Collections.sort()` no compilaba con `List<Estudiante>` porque Java solo puede ordenar naturalmente una lista cuando sus elementos implementan `Comparable<T>`.

Antes de implementar esa interfaz, `Estudiante` no cumplia el contrato que exige `Collections.sort(lista)`: cada objeto debe saber compararse con otro objeto del mismo tipo mediante `compareTo()`. Sin ese metodo, Java no sabe si un estudiante debe ir antes, igual o despues que otro.

## Pregunta 2

Elijo `promedio` como orden natural porque en este dominio representa el merito academico y es el criterio mas obvio para comparar estudiantes de forma general.

Si manana aparece un requisito para ordenar por `cantidadMateriasAprobadas`, no modificaria `compareTo()`. Cambiar el orden natural romperia codigo que ya espera estudiantes ordenados por promedio. Para ese nuevo caso usaria un `Comparator<Estudiante>` externo. Asi mantengo estable el dominio y agrego nuevos criterios sin modificar la clase principal.

## Pregunta 3

`Comparable` ata la clase a un unico criterio. Si el sistema necesita ordenar la misma lista por promedio, edad, nombre y materias aprobadas, poner toda esa logica dentro de `Estudiante` mezcla responsabilidades.

Eso afecta SRP porque la clase deja de representar solo datos y comportamiento propio del estudiante para tambien decidir multiples politicas de ordenamiento. Tambien afecta OCP porque cada nuevo criterio obligaria a modificar codigo existente. Con `Comparator`, cada criterio queda como estrategia externa y el sistema se puede extender sin tocar el orden natural.

## Pregunta 4

Overflow de enteros ocurre cuando una operacion supera el rango permitido por `int`. En Java, `int` va desde `-2147483648` hasta `2147483647`. Si una resta se pasa de ese limite, el resultado "da la vuelta" y queda con signo incorrecto.

El truco `e1.getEdad() - e2.getEdad()` puede provocar overflow. Por ejemplo, `Integer.MAX_VALUE - (-1)` deberia ser positivo, pero se desborda y produce un resultado negativo. Eso rompe el contrato de `Comparator`, porque indica que un objeto va antes cuando en realidad deberia ir despues. `Integer.compare()` no resta los valores directamente, compara con logica segura y evita ese overflow.

## Pregunta 5

Usar `Map<String, Comparator<Estudiante>>` aplica el patron Strategy. Cada `Comparator` es una estrategia de ordenamiento distinta y todas cumplen la misma interfaz.

Esto se relaciona con polimorfismo porque el service puede tratar todos los criterios como `Comparator<Estudiante>` sin conocer el detalle interno de cada comparacion. Es preferible a un `switch` porque evita logica procedural larga, reduce acoplamiento y permite agregar nuevos criterios registrando una nueva estrategia en el mapa.
