# Práctica Preguntas
Por Samuel :)

## Paso 0.3 — Jugar una partida manualmente

#### 1. ¿Qué tipos de mensajes aparecen en consola?

- Mensajes de **configuración inicial** (bienvenida, pedir número de jugadores y nombres).
- Mensajes de **estado del turno** (quién es el jugador actual, qué número sacó en el dado).
- Mensajes de **movimiento y tablero** (nueva posición del jugador, qué categoría de pregunta le toca y la pregunta en sí).
- Mensajes de **resolución** (si la respuesta fue correcta/incorrecta, la cantidad actual de monedas, o si entra/sale de la "penalty box" o cárcel).


#### 2. ¿Qué secuencia tiene un turno de juego?

1. Se tira el dado (aleatorio o manual).
2. Si el jugador está en la cárcel (penalty box), se evalúa si sale (sacando un número impar). Si saca par, se queda y pierde el turno.
3. Si no está en la cárcel (o acaba de salir), su ficha avanza en el tablero.
4. Se calcula la categoría según la nueva casilla y se le hace una pregunta.
5. El jugador responde.
6. Si acierta, gana una moneda (Gold Coin). Si falla, es enviado a la cárcel


#### 3. ¿Cuándo termina la partida?

El juego termina cuando un jugador alcanza exactamente 6 Gold Coins.

---

## Bloque 1
## Paso 1.1 — Lectura activa de Game.java

#### Olores de Código
- [x] **Método demasiado largo:** El método `roll(int roll)` es enorme y difícil de leer. Tiene demasiadas condiciones anidadas.

- [x] **Nombres engañosos:**
  - `places:` No queda claro que se refiere a las "posiciones" en el tablero. Sería mejor positions.
  - `purses:` Se refiere a las monedas o puntuación, sería mejor `coins` o `score`.
  - `winner:` Guarda el resultado de `didPlayerWin()`, pero debido a cómo está programado, en realidad guarda si el jugador no ha ganado (`notAWinner`).

- [x] **Duplicación de código:**
  - La lógica para mover al jugador y hacer la pregunta está duplicada exactamente igual dentro del `if (isGettingOutOfPenaltyBox)` y en el `else` principal de la función `roll()`.
  - La lógica para avanzar al siguiente jugador `(currentPlayer++; if (currentPlayer == players.size()) currentPlayer = 0;)` se repite 3 veces en distintos métodos.

- [x] **Uso de índices paralelos:** Existen 4 estructuras distintas (`players`, `places`, `purses`, `inPenaltyBox`) que comparten el mismo índice `currentPlayer`.

- [x] **Números mágicos sin nombre**:
  - 6 (límite de monedas para ganar).
  - 12 (tamaño del tablero).
  - 50 (cantidad de preguntas generadas por categoría).

- [x] **Mezcla de responsabilidades:** La clase `Game` hace de todo: maneja el estado de cada jugador, administra los mazos de preguntas, controla el tablero y dicta las reglas de los turnos.

#### Responsabilidades que deberían estar separadas
- Estado y acciones de un jugador individual (su nombre, posición, monedas y si está castigado).
- Gestión de las categorías y las colas de preguntas (Question Deck).
- Lógica y reglas del tablero/juego.

#### Abstracciones ausentes:
- Clase `Player`
- Clase `QuestionDeck`

#### El typo y el bug:
- El Typo: En el método `handleCorrectAnswer()`, en el bloque de código para cuando el jugador responde bien y no estaba castigado, dice: `System.out.println("Answer was corrent!!!!");`.
- El Bug Crítico: "Las condiciones de entrada y salida de la caja de penalti". Si miras la función `handleCorrectAnswer()`, cuando un jugador acierta y `isGettingOutOfPenaltyBox` es `true`, el jugador gana su moneda, pero su estado `inPenaltyBox[currentPlayer]` jamás se vuelve a cambiar a `false`. Esto significa que, aunque el jugador "salió" para ese turno, el juego lo sigue considerando prisionero para siempre, obligándolo a tener que sacar números impares en todos sus turnos futuros para poder participar.

## Paso 1.2 — Discusión de hallazgos
- El olor más urgente de corregir: Los nombres engañosos (como places o purses) y el uso de los arrays paralelos. Los nombres incorrectos mienten sobre lo que hace el sistema y los arrays paralelos dispersan el estado de lo que lógicamente debería ser un solo objeto a lo largo de toda la clase.

- El olor más peligroso de tocar: La lógica anidada en el método roll() y handleCorrectAnswer(). Al estar atada a la variable de estado global isGettingOutOfPenaltyBox y tener el bug silencioso que descubrimos sobre la salida de la cárcel, cualquier cambio estructural brusco aquí corre un alto riesgo de romper el Golden Master.

## Paso 1.3 — Planificar el itinerario de refactorizaciones
#### ITINERARIO TENTATIVO

1. Renombrar variables y métodos engañosos (places → positions, purses → coins, etc.).
2. Extraer números mágicos como constantes (como el 6 para ganar, 12 para el tablero, 50 para el número de preguntas).
3. Extraer métodos pequeños por responsabilidad (bloques de 3-5 líneas para encapsular condiciones).
4. Agrupar los arrays paralelos creando la clase Player e ir moviendo el comportamiento allí.
5. Extraer la lógica de los mazos de preguntas hacia una nueva abstracción QuestionDeck.
6. Simplificar la lógica de turno (roll y respuestas) para que se lea de forma natural, reduciendo la anidación.
7. Corregir el typo y el bug en ambas clases (Game y GameOld) de manera controlada.
