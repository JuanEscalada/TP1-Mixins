
TP Mixins: Norman’s Sky

El dominio del TP trata de un personaje que explora planetas en busca. Está vagamente
inspirado en el juego No Man’s Sky.
Como resultaría extremadamente complejo modelar todos los aspectos del juego, vamos a
basarnos en algunos pocos como ser la recolección de elementos.
El TP es un poco una excusa para jugar con mixins. Como un mixin es una unidad de
comportamiento fácil de compartir, es interesante que traten de pensar la solución a los puntos
utilizando mixins. Ya que, probablemente de todas formas se pueda resolver con herencia, o
composición tradicionales, o encontrándole la vuelta para reutilizar código de otras maneras más
tradicionales.

Lo que sucederá probablemente es que esas otras formas requieran trabajo manual o bien no
cierren del todo.
En cuanto a la forma, deberán entregar el modelo de objetos con sus respectivos tests para
cada punto.


Parte I


1) Inventario
Nuestro personaje puede recoger objetos y guardarlos en un inventario.
Claro que no es tan simple como eso.
La capacidad de carga del personaje depende del volúmen (cc3) que ocupan los
elementos.

1.a) Tipos de Mochilas
El volumen depende de la capacidad máxima del contenedor donde guarde los objetos.
Por ejemplo inicialmente nuestro personaje puede cargar elementos porque tiene una
Mochila chica.
Hay tres tipos de mochilas de acuerdo a su capacidad en volumen
● Mochila chica : hasta 40 litros (1 litro = 1000 cc3)
● Mochila mediana: hasta 60 litros.
● Mochila grande: hasta 90 litros.

1.b) Características de las mochilas: compactación
Además de su capacidad en volumen las mochilas vienen con distintas características.
Por ejemplo compactación.
● Una mochila compactadora por vacío, aplica vacío para reducir el volúmen de los
objetos. Hay objetos compactables por vacío, y otros que no. Por ejemplo una roca
no se puede compactar por vacío. Sigue ocupando el mismo volumen. Existen otros
dos tipos de objetos compactables por vacío:
○ los compactables: se reducen a la mitad de su volumen
○ semicompactables:
se reducen en N puntos fijos. Por ejemplo: Si un trozo
de tierra de 300cc3 semicompactable de 100cc3, al compactarse por vacío
va a ocupar sólo 200cc3.
● Una mochila deshidratadora: también reduce el volumen de sus elementos, pero
sólo para aquellos que sean orgánicos. Un elemento orgánico tiene cierta cantidad
de agua como parte de su cuerpo. La deshidratadora elimina el volumen de agua del
cuerpo.

1.c) Características de las mochilas: resistencia al daño
Otro característica de mochila tiene que ver con su capacidad de absorber golpes y por lo
tanto daño .
● Mochila rígida: absorbe un porcentaje “x” del daño. Ej si es de 50%, y recibe un
daño de 50 puntos, entonces los elementos sólo reciben 25 puntos de daño.
● Mochila semirígida:
resiste hasta X puntos de daño, transmiten todo el excedente.
Ejemplo si resiste hasta 10 puntos y recibe un daño de 60 puntos, transmite 50.
Recibe 3 puntos de daño, transmite 0, 9 >
0, 10 >
0, 11 >
1 punto.
● Mochila regular: transmite el daño directo.


2) Trajes
Vamos a agregar un elemento nuevo al juego. Los “ trajes ”

2.a) Tipos de traje: en base al oxígeno
Los trajes proveen oxígeno a nuestro personaje.
Estos trajes tienen una cantidad de oxígeno que se va consumiendo al caminar ciertos kms.
La forma en que consumen depende del traje.
Tenemos dos tipos de trajes:
● Liviano: capacidad máxima 1500 unidades. Consume el 15% de lo recorrido (kms),
o bien si este valor es superior a 10 unidades, sólo 10 unidades. O sea el 15% con
un máximo de 10 unidades.
● Pesado: capacidad máxima 4000. Consume un 20% de los kms que recorre el
personaje. Pero posee un dispositivo especial por el cual, cada vez que se camina,
genera 10 unidades de oxígeno.


Parte II

2.b) Resistencia al daño
A su vez, los trajes cumplen la función de resistir contra el daño a recibir del personaje.
Cuando un traje recibe un daño, lo transmite al personaje. Por defecto transmite el daño
completo.
Sin embargo tenemos diferentes tipos de trajes que alteran la forma de transmitir el daño.
Resulta que estos tipos son los mismos que para las mochilas 
Tenemos: Rígido , SemiRígido , y claro, el traje Regular.


3) Combates
Dentro de estos mundos nuestro personaje podrá encontrar elementos con vida. Estos
tienen energía que se reduce al recibir daño.
Algunos de estos seres son agresivos y al encontrarlos podrán atacarnos. Y nuestro
personaje también podrá atacarlos.
Cuando nuestro personaje se encuentra con uno de estos seres, para simplificarlo vamos a
hacer que el combate conste sólo de 1 etapa donde primero ataca uno, y luego el otro. Y se
termina ahí el combate :)
Tenemos varias características a modelar en este caso


3.a) Respuesta al daño
Habrá distintos personajes que responderán de distinta forma al daño, y todas estas serán
combinables, es decir que un personaje podrá varias de estas al mismo tiempo.
● Escudo : un personaje con escudo tiene cierta capacidad de absorción de daño, por
ejemplo un escudo de 100 disminuye el daño recibido en 100. Luego el escudo se
descarga, una vez que absorbió esos 100 puntos
● Escudo Recargable: es similar al Escudo, sólo que, se puede recargar a medida
que el personaje camina. Carga 10 puntos por cada km recorrido.
● Reflector de daño: esta capacidad no afecta el daño recibido, pero inflige un
porcentaje del daño recibido al atacante. Por ejemplo un Reflector de 5%, cuando
recibe un daño de 100 puntos, inflige automáticamente 5 puntos al atacante (ese
daño también deberá pasar como un ataque normal, con lo cual si el atacante
también tiene escudo o alguna otra característica se tendrá en cuenta).
● Absorción de daño: esta característica permite convertir el daño en poder de
ataque. En base a un factor. Es decir, si nos infligen 20 puntos de daño y tenemos
una capacidad de absorción de 0.15, entonces vamos a generar un incremento de
20 * 0.15 puntos de poder de ataque (ver siguiente punto sobre ataque)


3.b) Ataque
El ataque se refiere al poder de daño que genera en el otro. Este poder depende del arma
que tiene el personaje.
Tipos de armas:

● Rifles Láser : utilizan celdas de energía. Tienen 10 puntos de poder inicialmente, y
en cada uso disminuye en 1. Nunca se reduce menos que 1 punto
● Arma de fuego: 3 puntos de poder. Tiene una cantidad X de balas que se pueden
cargar y se van consumiendo. Si se queda sin balas, entonces no aporta daño.
● Arma rara: incomprensible para nosotros :P Utiliza la energía del personaje que la
porta para generar puntos de ataque. Pero la contra es que reduce la energía el
personaje (no a través del mecanismo de daño). Ejemplo: si el personaje tiene 100
de energía, y es una “arma rara de 3”, produce 3 de daño pero instantáneamente le
consume 3 de energía al personaje, quedando en 97.
Al atacar un personaje causa tantos puntos de daño como su poder de ataque. O sea, la
traducción de “poder de ataque” >
“daño” es directa.
Existen modificadores del poder de ataque , que aplican tanto al personaje como a las
armas.

● Duplicadores: eso, duplican el poder de ataque que generaba ese personaje / arma
inicialmente.
● Potenciadores: incrementan el poder de ataque en X unidades. Ej: Potenciador(5)
produce: poderDeAtaque + 5
● Canceladores : son como maldiciones, un elemento de ataque con un Cancelador,
no produce ningún punto de poder / daño. Por ejemplo, imaginen que el personaje
encuentra una daga maldita (nos fuimos a juegos de rol :P)


4) Bonus


1) Sobre mochilas

a) Masa y pesos: además del volumen modelar el hecho de que los objetos
tiene masa, y que esa masa se convierte en peso dependiendo de la
gravedad actual (dada por el cuerpo en el que está, por ejemplo un planeta).
Las mochilas entonces tendrán un limite ahora no sólo por volumen sino por
peso.

b) Las mochilas pueden tener la capacidad de ser propulsoras. Permiten elevar
al personaje (y todas sus cosas) por cierto tiempo (segundos). Para generar
esta fuerza consumen combustible en la forma de: consumo = tiempo (seg) *
peso (mochila). La propulsión además, modifica la posición del personaje,
aumentando su coordenada z (altura) en 1 metro cada 2 segundos.

2) Sobre Trajes

a) Modelar la idea de que en algunas mochilas el consumo de oxígeno de los
trajes al caminar también depende del peso de los objetos (peso = masa *
gravedad) que carga el personaje. Es decir que además del consumo que ya
tenían, se consumirá oxígeno adicional en base a: consumo (oxigeno) =
distancia * peso / √distancia

3) Sobre Combates

a) Modelar una caracterísitca del personaje que potencie el poder de ataque del
arma, pero que dependa de una caracterísitca del personaje. Por ejemplo,
PoderDependienteDelEstadoFisico, que disminuya la potencia de ataque en
base al cansancio del personaje (por caminar).

b) Que el poder ahora se afecte por la experiencia en Combate (se ganan
puntos de experiencia por cada combate). (obvio, modelar como un mixin)

c) Implementar combates más completos donde los personajes se sigan
atacando hasta que o bien uno muera, o bien no se inflijan daño significativo
(algún tipo de corte)

