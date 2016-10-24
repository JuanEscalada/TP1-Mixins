package tpMixins

/**
  * Creado por Juan Escalada, Programacion con Objetos III
  */

// Los colaboradores externos Arma y Escudo no son necesarios aun por lo que los dejamos comentados.

  class Personaje(var planeta: Planeta = new Planeta(10)) extends Luchador{

      //La variable energia representa a la cantidad de vida del personaje.
      private var _energia : Int = 200

      var puntosDePoder = 5

      var inventario : List[ObjetoRecogible] = List()

      def guardar(objeto : ObjetoRecogible) = {}

      def poseeObjeto(objeto : ObjetoRecogible) : Boolean = inventario.contains(objeto)

      def pesoDisponible : Int = 0

      def volumenLibre : Int = 0

      def capacidadDeCarga : Int = 0

      def pesoUtilizado : Int = 0

      def oxigenoDisponible : Double = 0

      def energiaActual : Int = _energia

      def gravedadActual : Int = planeta.gravedad

      def danioDirecto(danio : Int) : Unit = _energia -= danio

      override def estaMuerto : Boolean = energiaActual <= 0

      def caminar(unaCantidadDeKilometros : Int){
        recorrerCon(unaCantidadDeKilometros, pesoUtilizado)
      }

      def recorrerCon(unaCantidadDeKilometros : Int, pesoUtilizado : Int){}

      def recibirUnAtaqueDePor(unPersonajeEnemigo: Luchador, unDanio: Int) ={

        val energiaResultante = _energia - danioNoAbsorbido(unDanio)
        if (energiaResultante <= 0 ) _energia = 0
        else _energia = energiaResultante
      }


      private def danioNoAbsorbido(unDanio : Int) : Int ={
        val danio = absorberDanio(unDanio)
        danio
      }


      override def atacar(unPersonajeEnemigo : Luchador){
        infrinjirDanioA(unPersonajeEnemigo, puntosDePoder)
      }

      override def infrinjirDanioA(unPersonajeEnemigo : Luchador, unDanio : Int) : Unit ={
          unPersonajeEnemigo.recibirUnAtaqueDePor(this, unDanio)
      }

}


  trait Defensor {def absorberDanio(unDanio :Int) :Int = unDanio}

/*
    La clase mochila representa a un dispositivo para cargar objetos entre otras cosas.
  Por defecto siempre tiene el mixin compactacionNula para poder agregar objetos, pero
  pueden darsele tanto compactacion por vacio como compactacion por deshidratacion (pero no ambas,
  ya que no funcionan por acumulación).

    Lo mismo sucede con el tipo de rigidez Regular, se le puede dar otro tipo de rigidez pero no
  son acumulables.
 */

  trait Mochila extends Personaje {

      var volumenDisponible : Int = 0 // La capacidad esta dada en cm3.

      var _pesoDisponible : Int = 0 // El peso esta dado en gramos.

      var pesoMaximoSoportado : Int = _

      var capacidad : Int = _

      override def pesoDisponible : Int ={
        _pesoDisponible = pesoMaximoSoportado - pesoUtilizado
        _pesoDisponible
      }

      override def pesoUtilizado : Int ={
        var pesoUtilizado : Int = 0
        inventario.foreach { obj : ObjetoRecogible => pesoUtilizado += obj.pesoCon(planeta.gravedad) }
        pesoUtilizado
      }

      // Se agrega, de ser posible, un objeto con un volumen dado al inventario, caso contrario
      //se levanta una excepcion
      private def agregarAlInventario(objeto : ObjetoRecogible, volumen : Int): Unit ={
          if(volumenDisponible > volumen && pesoDisponible > objeto.pesoCon(planeta.gravedad)){
            inventario = objeto :: inventario
            volumenDisponible -= volumen
            _pesoDisponible -= objeto.pesoCon(planeta.gravedad)
          }
      }

       override def guardar(objeto : ObjetoRecogible): Unit ={
          agregarAlInventario(objeto, objeto.volumen(this))
      }

      override def volumenLibre: Int = volumenDisponible

      override def capacidadDeCarga: Int = _pesoDisponible

  }
// En este momento lo unico que diferencia a las subclases de mochila es su capacidad.
  trait MochilaChica extends Mochila{
    capacidad = 40000
    pesoMaximoSoportado = 30000
    volumenDisponible = capacidad
    _pesoDisponible = pesoMaximoSoportado
  }

  trait MochilaMediana extends Mochila{
    capacidad = 60000
    pesoMaximoSoportado = 45000
    volumenDisponible = capacidad
    _pesoDisponible = pesoMaximoSoportado
  }

  trait MochilaGrande extends Mochila{
    capacidad = 90000
    pesoMaximoSoportado = 67500
    volumenDisponible = capacidad
    _pesoDisponible = pesoMaximoSoportado
  }

  trait CompactacionPorVacio extends Mochila

  trait CompactacionPorDeshidratacion extends Mochila

/*
  Son los tipos de resistencias que puede tener una mochila y afectan el daño que recibe el personaje
*/

  trait Rigido extends Defensor {
      var porcentajeAbsorcion : Int = 20
      abstract override def absorberDanio(unDanio : Int) : Int ={
        var danio = super.absorberDanio(unDanio)
        danio = danio - ((danio * porcentajeAbsorcion) / 100)
        danio
      }
  }

  trait SemiRigido extends Defensor {
      var puntosResistencia : Int = 10
      abstract override def absorberDanio(unDanio : Int) : Int = {
        val danio = super.absorberDanio(unDanio)
        if (puntosResistencia < danio) danio - puntosResistencia
        else 0
      }

  }


  trait Traje extends Personaje{
      var capacidado2 : Int = _
      var o2disponible : Double = _
      override def oxigenoDisponible : Double = o2disponible
      override def recorrerCon(unaCantidadDeKilometros : Int, peso : Int) : Unit ={
        val raizKilometros = Math.sqrt(unaCantidadDeKilometros)
        var consumo = (unaCantidadDeKilometros * peso) / raizKilometros
        o2disponible -= consumo
      }
  }

  trait TrajeLiviano extends Traje{
     capacidado2 = 1500
     o2disponible = capacidado2
     override def recorrerCon(unaCantidadDeKilometros : Int, peso : Int) : Unit ={
      var consumo = unaCantidadDeKilometros * 0.15
      if(consumo > 10) o2disponible -= 10
      else o2disponible -= consumo
      super.recorrerCon(unaCantidadDeKilometros, peso)
    }
  }

  trait TrajePesado extends Traje{
    capacidado2 = 4000
    o2disponible = capacidado2

    //  Se recorre una distancia caminando en Kilometros y se hace el descuento correspondiente de unidades
    //de oxigeno considerando que el traje recupera 10 unidades cada vez que recorre una distancia.
    override def recorrerCon(unaCantidadDeKilometros : Int, peso : Int) : Unit ={
      if(o2disponible > (capacidado2 - 10)) o2disponible = capacidado2
      else o2disponible += 10
      o2disponible -= unaCantidadDeKilometros * 0.20
      super.recorrerCon(unaCantidadDeKilometros, peso)
    }
  }



/* El Objeto recogible representa a un objeto generico */
  abstract class ObjetoRecogible(val _volumen : Int, val masa :Int, val puntosDeCompactacion : Int) {
      def pesoCon(gravedad : Int ) : Int = masa*gravedad
      val volumen = DynDispatch.defMulti[Mochila, Int]
      volumen.defMethod { case m : Mochila => _volumen}
  }


  class ObjetoInorganico(_volumen : Int, masa : Int, puntosDeCompactacion : Int = 0) extends ObjetoRecogible(_volumen, masa, puntosDeCompactacion){}

  class ObjetoOrganico(_volumen : Int, masa : Int, var cantidadDeAgua : Int, puntosDeCompactacion : Int = 0) extends ObjetoRecogible(_volumen, masa, puntosDeCompactacion) {
      volumen.defMethod { case m : CompactacionPorDeshidratacion => _volumen - cantidadDeAgua }
  }


  trait Compactable extends ObjetoRecogible{
      volumen.defMethod { case m : CompactacionPorVacio => _volumen / 2 }
  }

  trait SemiCompactable extends ObjetoRecogible{
      def puntosDeCompactacion : Int
      volumen.defMethod { case m : CompactacionPorVacio => _volumen - puntosDeCompactacion }
  }



   trait Atacante {
     def atacar(unPersonajeEnemigo : Luchador) : Unit
     def infrinjirDanioA(unPersonajeEnemigo : Luchador, unDanio : Int) : Unit
  }

  trait RifleLaser extends Atacante {
    var puntosDePoderRifle = 10
    var cantidadCeldas = 0

    abstract override def infrinjirDanioA(unPersonajeEnemigo : Luchador, unDanio : Int) : Unit = {
      if(cantidadCeldas > 0) {
        super.infrinjirDanioA(unPersonajeEnemigo, puntosDePoderRifle + unDanio)

        cantidadCeldas -= 1

        if (puntosDePoderRifle < 1) {
          puntosDePoderRifle -= 1
        }
      }
      else super.infrinjirDanioA(unPersonajeEnemigo, unDanio)
    }

    def cargarArmaConCeldas(cantCeldas : Int): Unit ={
      cantidadCeldas = cantCeldas
    }
  }

  //Se disminuye un punto de poder en el arma fuego?? o de a 3? o 3 es el valor inicial de puntos de poder que tiene?
  trait ArmaFuego extends Atacante{
    var puntosDePoderArmaDeFuego = 3
    var cantidadBalas = 0
    abstract override def infrinjirDanioA(unPersonajeEnemigo : Luchador, unDanio : Int) : Unit = {
        if (cantidadBalas > 0){
          super.infrinjirDanioA(unPersonajeEnemigo, puntosDePoderArmaDeFuego + unDanio)
          cantidadBalas-=1
        }
        else super.infrinjirDanioA(unPersonajeEnemigo, unDanio)
    }

    def cargarArmaConBalas(cantBalas : Int): Unit ={
      cantidadBalas = cantBalas
    }
  }

  trait ArmaRara extends Atacante{
    var puntosDePoderArmaRara = 10

    def danioDirecto(unDanio : Int) : Unit

    abstract override def infrinjirDanioA (unPersonajeEnemigo : Luchador, unaCantidadDeDanio : Int): Unit ={
      super. infrinjirDanioA(unPersonajeEnemigo, puntosDePoderArmaRara + unaCantidadDeDanio)
      danioDirecto(puntosDePoderArmaRara)
    }
  }

  trait Duplicador extends Atacante{
    abstract override def infrinjirDanioA(unPersonajeEnemigo : Luchador, unaCantidadDeDanio :Int) : Unit ={
      super.infrinjirDanioA(unPersonajeEnemigo, unaCantidadDeDanio * 2)
    }
  }

  trait Potenciador extends Atacante{
    var potencia = 0

    abstract override def infrinjirDanioA(unPersonajeEnemigo : Luchador, unaCantidadDeDanio : Int) : Unit = {
        super.infrinjirDanioA(unPersonajeEnemigo, unaCantidadDeDanio + potencia)
    }
  }

  trait Cancelador extends Atacante{
    abstract override def infrinjirDanioA(unPersonajeEnemigo :Luchador, unaCantidadDeDanio : Int) : Unit ={
        super.infrinjirDanioA(unPersonajeEnemigo, 0)
    }
  }







  trait Escudo extends Defensor{
    var resistenciaEscudo = 100
    abstract override def absorberDanio(unDanio : Int) : Int = {
      var danio : Int =  0
      val danioConAbs = super.absorberDanio(unDanio)
      if (danioConAbs >= resistenciaEscudo) {
        resistenciaEscudo = 0
        danio = danioConAbs - resistenciaEscudo
      }
      else resistenciaEscudo -= unDanio
      danio
    }
  }

  trait Recargable extends Personaje with Escudo{
    abstract override def caminar(unaCantidadDeKilometros : Int) = {
      super.caminar(unaCantidadDeKilometros)
      recargar(unaCantidadDeKilometros)
    }

    private def recargar(unaCantidadDeKilometros: Int) = {
      val resistenciaMaxima: Int = 100
      if (resistenciaEscudo + 10 < resistenciaMaxima) resistenciaEscudo += 10 * unaCantidadDeKilometros
      else resistenciaEscudo = resistenciaMaxima
    }
  }

  trait ReflectorDeDanio extends Personaje{

    abstract override def recibirUnAtaqueDePor(unPersonajeEnemigo : Luchador, unDanio : Int){
      reflejarDanio(unDanio, unPersonajeEnemigo)
      super.recibirUnAtaqueDePor(unPersonajeEnemigo, unDanio)
    }

    private def reflejarDanio(unDanio :Int, unPersonajeEnemigo : Luchador){
      unPersonajeEnemigo.recibirUnAtaqueDePor(this, unDanio = (unDanio * 5) / 100)
    }
  }


  trait AbsorcionDeDanio extends Defensor{
    var porcentajeDeAbsorcion : Double = 0.5
    var potenciacionAlAtaque : Double = 0
    override def absorberDanio(unDanio : Int) : Int ={
        val danio = super.absorberDanio(unDanio)
        potenciacionAlAtaque += porcentajeDeAbsorcion * danio
        danio
    }
  }

  abstract class Luchador extends Atacante with Defensor{
    def energiaActual : Int
    def recibirUnAtaqueDePor(unPersonajeEnemigo: Luchador, unDanio: Int)
    def estaMuerto : Boolean
  }

  class Planeta(val gravedad : Int)


  object Combate{

    var _primerPersonaje : Luchador = _
    var _segundoPersonaje : Luchador = _

    def apply(unPersonaje : Luchador, otroPersonaje :Luchador, cantTurnos : Int = 1) : Unit ={
        _primerPersonaje = unPersonaje; _segundoPersonaje = otroPersonaje
      println(s"El primer Luchador comienza con $energiaPrimerPersonaje puntos de energia. ")
      println(s"y el segundo con $energiaSegundoPersonaje puntos de energia. ")

      combatir(cantTurnos,_primerPersonaje, _segundoPersonaje)

      println(s"Luego de luchar, el primero luchador tiene $energiaPrimerPersonaje puntos de energia. ")
      println(s"y el segundo $energiaSegundoPersonaje puntos de energia. ")

      if (unPersonaje.estaMuerto) println("Luego de luchar, el primero luchador murio")
      if (otroPersonaje.estaMuerto) println("Luego de luchar, el segundo luchador murio")
    }

    def energiaPrimerPersonaje : Int = _primerPersonaje.energiaActual
    def energiaSegundoPersonaje : Int = _segundoPersonaje.energiaActual

    private def combatir(cantTurnos : Int, primerPersonaje : Luchador, segundoPersonaje : Luchador) : Unit = {
      var turnoActual = 0
      while (turnoActual < cantTurnos && !_primerPersonaje.estaMuerto && !_segundoPersonaje.estaMuerto){
        _primerPersonaje.atacar(_segundoPersonaje)
        _segundoPersonaje.atacar(_primerPersonaje)
        turnoActual += 1
      }
    }
  }


  object DynDispatch {
    class DynMethod[A,R] {
      val methods = scala.collection.mutable.ListBuffer[PartialFunction[A,R]]()
      def defMethod(m: PartialFunction[A,R]) = {
        methods += m
      }
      def apply(args: A): R = {
        methods.reverse.find(_.isDefinedAt(args)) match {
          case Some(f) => f(args)
          case None => throw new Exception("type mismatch?")
        }
      }
    }
    def defMulti[A,R] = new DynMethod[A,R]
  }