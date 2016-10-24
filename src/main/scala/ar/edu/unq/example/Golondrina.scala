package ar.edu.unq.example


import scala.collection.mutable.ListBuffer

class Golondrina(var energia: Int) {
  val viajes = ListBuffer[Int]()

  def volar(distancia: Int) = {
    energia -= distancia
    viajes += distancia
  }

  def viajesLargos = viajes.filter(_ > 50)

  override def toString =
    s"Yo soy una golondrina con $energia energia " +
    s"y viaje ${viajes.mkString(", ")}"
}