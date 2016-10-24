package tpMixins

/**
  * Creado por Juan Escalada, Programacion con Objetos III
  */

import org.scalatest.FlatSpec
import org.scalatest.Matchers

trait BaseSpec extends FlatSpec with Matchers

class testsPersonaje extends BaseSpec{

  "Una Personaje nuevo con una Mochila Chica y un Traje Liviano" should "tener 40000cm3 de espacio libre para guardar objetos" +
    " y 1500 unidades de oxigeno para utilizar" in {
    val personaje = new Personaje with TrajeLiviano with MochilaChica

    personaje.volumenLibre shouldBe 40000
    personaje.capacidadDeCarga shouldBe 30000
    personaje.oxigenoDisponible shouldBe 1500
  }

  "Una Personaje nuevo con una Mochila Mediana y un Traje Pesado" should "tener 60000cm3 de espacio libre para guardar objetos" +
    " y 4000 unidades de oxigeno para utilizar" + " y si se le da una Mochila Grande deberia tener 90000cm3 de espacio libre" in {
    val personaje = new Personaje with TrajePesado with MochilaMediana

    personaje.volumenLibre shouldBe 60000
    personaje.capacidadDeCarga shouldBe 45000
    personaje.oxigenoDisponible shouldBe 4000

    val personaje2 = new Personaje with TrajePesado with MochilaGrande

    personaje2.volumenLibre shouldBe 90000
    personaje2.capacidadDeCarga shouldBe 67500
  }

  "Una Personaje nuevo con una Mochila Mediana y un Traje Pesado" should "Guardar un objeto en su mochila y agregarlo al inventario" in {
    val personaje = new Personaje with TrajePesado with MochilaMediana
    val objeto = new ObjetoInorganico(10, 1)

    personaje.volumenLibre shouldBe 60000
    personaje.capacidadDeCarga shouldBe 45000

    personaje.guardar(objeto)
    personaje.volumenLibre shouldBe 59990
    personaje.capacidadDeCarga shouldBe 44990
    personaje.poseeObjeto(objeto) shouldBe true
  }

  "Una Personaje nuevo con una Mochila Mediana y un Traje Pesado" should "No puede guardar el objeto en la mochila porque no tiene mas volumen disponible" in {
    val personaje = new Personaje with TrajePesado with MochilaMediana
    val objeto = new ObjetoInorganico(80000, 3500)

    personaje.volumenLibre shouldBe 60000
    personaje.capacidadDeCarga shouldBe 45000

    personaje.guardar(objeto)
    personaje.inventario.isEmpty shouldBe true
    personaje.poseeObjeto(objeto) shouldBe false
  }

  "Una Personaje nuevo con una Mochila Mediana con Compactacion por vacio" should "Guardar Tres objetos: uno no compactable" +
    "uno semicompactable y uno compactable y el volumen cambia acorde al objeto que se agrega"in {
    val personaje = new Personaje with TrajePesado with MochilaMediana with CompactacionPorVacio
    val objetoNoCompactable = new ObjetoInorganico(10, 1)
    val objetoCompactable = new ObjetoInorganico(20, 2) with Compactable
    val objetoSemiCompactable = new ObjetoInorganico(20, 2, 2) with SemiCompactable

    personaje.volumenLibre shouldBe 60000

    personaje.guardar(objetoNoCompactable)
    personaje.volumenLibre shouldBe 59990
    personaje.capacidadDeCarga shouldBe 44990
    personaje.poseeObjeto(objetoNoCompactable) shouldBe true

    personaje.guardar(objetoCompactable)
    personaje.volumenLibre shouldBe 59980
    personaje.capacidadDeCarga shouldBe 44970
    personaje.poseeObjeto(objetoCompactable) shouldBe true


    personaje.guardar(objetoSemiCompactable)
    personaje.volumenLibre shouldBe 59962
    personaje.capacidadDeCarga shouldBe 44950
    personaje.poseeObjeto(objetoSemiCompactable) shouldBe true

  }


  "Una Personaje nuevo con una Mochila Mediana con Compactacion por deshidratacion" should "Guardar Dos objetos: uno inorganico" +
    " y uno organico y el volumen cambia acorde al objeto que se agrega"in {
    val personaje = new Personaje with TrajePesado with  MochilaMediana with CompactacionPorDeshidratacion
    val objetoInorganico = new ObjetoInorganico(10, 1)
    val objetoDeshidratable = new ObjetoOrganico(20, 2, 6)

    personaje.volumenLibre shouldBe 60000

    personaje.guardar(objetoInorganico)
    personaje.volumenLibre shouldBe 59990
    personaje.capacidadDeCarga shouldBe 44990
    personaje.poseeObjeto(objetoInorganico) shouldBe true

    personaje.guardar(objetoDeshidratable)
    personaje.volumenLibre shouldBe 59976
    personaje.capacidadDeCarga shouldBe 44970
    personaje.poseeObjeto(objetoDeshidratable) shouldBe true

  }

  "Un Personaje nuevo con una Mochila Mediana y un Traje Pesado camina una distancia" should "Caminar una cantidad de 100 kilometros restando al oxigeno la cantidad correspondiente" in {
    val personaje = new Personaje with TrajePesado with MochilaMediana

    personaje.oxigenoDisponible shouldBe 4000.0

    personaje.caminar(100)
    personaje.oxigenoDisponible shouldBe 3980.0
    // En este caso el resultado es 3980 ya que decidimos que primero se recuperan las 10 unidades y luego recien se
    //hace el descuento. Pero podria ser alrevez sin problema.
    personaje.caminar(100)
    personaje.oxigenoDisponible shouldBe 3970.0

    // Aqui vemos que si se recupera el oxigeno, pero al caminar nuevamente.

  }

  "Un Personaje nuevo con una Mochila Mediana y un Traje Liviano camina una distancia" should "Caminar una cantidad de 100 kilometros restando al oxigeno la cantidad correspondiente" in {
   val personaje = new Personaje with TrajeLiviano with MochilaMediana

    personaje.oxigenoDisponible shouldBe 1500

    personaje.caminar(100)
    personaje.oxigenoDisponible shouldBe 1490
  }

  "Un personaje nuevo con una Mochila chica Regular" should "recibir daño y que no se absorba nada" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica

    personaje.energiaActual shouldBe 200

    personaje.recibirUnAtaqueDePor(new Personaje with TrajeLiviano, 20)

    personaje.energiaActual shouldBe 180

  }

  "Un personaje nuevo con una Mochila chica SemiRigida" should "recibir daño y que se absorban 10 puntos" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with SemiRigido

    personaje.energiaActual shouldBe 200

    personaje.recibirUnAtaqueDePor(new Personaje with TrajeLiviano, 20)

    personaje.energiaActual shouldBe 190

  }

  "Un personaje nuevo con una Mochila chica Rigida" should "recibir daño y que se absorba el 20%" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido

    personaje.energiaActual shouldBe 200

    personaje.recibirUnAtaqueDePor(new Personaje with TrajeLiviano, 20)

    personaje.energiaActual shouldBe 184

  }

  "Un personaje nuevo con una Mochila chica Rigida, arma de fuego y un potenciador" should "ataca con sus puntos de poder potenciados en 5 y el enemigo queda con 193 de salud" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaFuego with Potenciador {potencia = 5; cargarArmaConBalas(200)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personajeEnemigo.energiaActual shouldBe 189
  }

  "Un personaje nuevo con una Mochila chica Rigida, arma de fuego y un duplicador" should "ataca y el enemigo queda con 195 de salud" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaFuego with Duplicador { cargarArmaConBalas(200)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personajeEnemigo.energiaActual shouldBe 189
  }


  "Un personaje nuevo con una Mochila chica Rigida, arma de fuego y un cancelador" should "ataca y el enemigo no recibe daños" in{

    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido  with ArmaFuego with Cancelador {cargarArmaConBalas(200)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personajeEnemigo.energiaActual shouldBe 197
  }

  "Un personaje nuevo con una Mochila chica Rigida, arma rara y un duplicador" should "ataca, el atacante queda en 192 y el enemigo queda con 184 de salud" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Duplicador
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 190
    personajeEnemigo.energiaActual shouldBe 184
  }

  "Un personaje nuevo con una Mochila chica Rigida, arma rara y un potenciador" should "ataca, el atacante queda en 192 y el enemigo queda con 184 de salud" in{

    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Potenciador {potencia = 10}

    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 190
    personajeEnemigo.energiaActual shouldBe 175
  }

  "Un personaje nuevo con una Mochila chica Rigida, arma rara y un cancelador" should "ataca, el atacante y el enemigo no reciben daños" in{

    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Cancelador

    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 190
    personajeEnemigo.energiaActual shouldBe 192
  }

  "Un personaje nuevo con una Mochila chica Rigida, arma rara y un cancelador y otro personaje igual pero sin cancelador" should "ataca, el atacante y el enemigo no reciben daños, luego el enemigo ataca,el atacante y el enemigo de ese atacante anterior recibe daños" in{

    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Cancelador
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 190
    personajeEnemigo.energiaActual shouldBe 192

    personajeEnemigo.atacar(personaje)

    personaje.energiaActual shouldBe 178
    personajeEnemigo.energiaActual shouldBe 182
  }

  "Un personaje nuevo con una Mochila chica Rigida, escudo regular, rifle laser y un duplicador y otro personaje igual pero con potenciador" should "ataca, el enemigo recibe daños, luego el enemigo ataca,el atacante anterior recibe daños que son menores gracias al escudo" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with RifleLaser with Duplicador with Escudo {cargarArmaConCeldas(90)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Potenciador{potencia = 5}

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 184

    personajeEnemigo.atacar(personaje)

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 174
  }

  "Un personaje nuevo con una Mochila chica Rigida, escudo recargable, rifle laser y un potenciador y otro personaje igual pero con potenciador" should "ataca, el enemigo recibe daños, luego el enemigo ataca,el atacante anterior recibe daños que son menores gracias al escudo" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with Recargable with RifleLaser with Potenciador {potencia = 78; cargarArmaConCeldas(90)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Potenciador{potencia = 5}

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 125

    personajeEnemigo.atacar(personaje)

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 115
  }

  "Un personaje nuevo con una Mochila chica Rigida, escudo reflector danio, rifle laser y un potenciador y otro personaje igual pero con arma rara con potenciador" should "ataca, el enemigo recibe daños, luego el enemigo ataca,el atacante anterior recibe daños que son menores gracias al escudo" in{

    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ReflectorDeDanio with RifleLaser with Potenciador {potencia = 78; cargarArmaConCeldas(90)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Potenciador {potencia = 5}

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 200

    personaje.atacar(personajeEnemigo)

    personaje.energiaActual shouldBe 200
    personajeEnemigo.energiaActual shouldBe 125

    personajeEnemigo.atacar(personaje)

    personaje.energiaActual shouldBe 184
    personajeEnemigo.energiaActual shouldBe 114
  }

  "En un combate entre un personaje nuevo con una Mochila chica Rigida, escudo reflector danio, rifle laser y un potenciador y otro personaje igual pero con arma rara con potenciador" should "ataca, el enemigo recibe daños, luego el enemigo ataca,el atacante anterior recibe daños que son menores gracias al escudo" in{

    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ReflectorDeDanio with RifleLaser with Potenciador {potencia = 78; cargarArmaConCeldas(90)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Potenciador {potencia = 5}

    Combate(personaje, personajeEnemigo)

    Combate.energiaPrimerPersonaje shouldBe 184
    Combate.energiaSegundoPersonaje shouldBe 114
  }


  "En un combate entre un personaje nuevo con una Mochila chica Rigida, escudo reflector danio, rifle laser y un duplicador y otro personaje igual pero con arma rara con cancelador" should "se realiza un combate de 5 turnos y el primer personaje gana debido a que el segundo tiene un cancelador" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ReflectorDeDanio with RifleLaser with Duplicador {cargarArmaConCeldas(90)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaRara with Cancelador

    Combate(personaje, personajeEnemigo, 5)

    Combate.energiaPrimerPersonaje shouldBe 160
    Combate.energiaSegundoPersonaje shouldBe 70
  }

  "En un combate entre un personaje nuevo con una Mochila chica Rigida, escudo reflector danio, arma de fuego que no tiene balas y un duplicador y otro personaje igual pero con un rifle laser con potenciador" should "se realiza un combate de 10 turnos y el segundo personaje gana y el primero muere" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ReflectorDeDanio with ArmaFuego
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with RifleLaser with Potenciador {potencia = 200; cargarArmaConCeldas(90)}

    Combate(personaje, personajeEnemigo, 10)

    Combate.energiaPrimerPersonaje shouldBe 0
    Combate.energiaSegundoPersonaje shouldBe 176
  }

  "En un combate entre un personaje nuevo con una Mochila chica Rigida, escudo reflector danio, arma de fuego y un duplicador y otro personaje igual pero con un rifle laser con potenciador" should "se realiza un combate de 10 turnos, el segundo personaje gana y el primero muere" in{
    val personaje = new Personaje with TrajeLiviano with MochilaChica with Rigido with ArmaFuego with Duplicador with ReflectorDeDanio {cargarArmaConBalas(90)}
    val personajeEnemigo = new Personaje with TrajeLiviano with MochilaChica with Rigido with RifleLaser with Duplicador {cargarArmaConCeldas(90)}

    Combate(personaje, personajeEnemigo, 10)

    Combate.energiaPrimerPersonaje shouldBe 40
    Combate.energiaSegundoPersonaje shouldBe 80
  }
}
