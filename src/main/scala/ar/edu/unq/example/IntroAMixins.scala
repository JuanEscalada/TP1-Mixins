package ar.edu.unq.example


  class Persona(var edad: Int = 0, var nombre: String = "")

  class Docente extends Persona(){
    def darClases() : Unit = {
      println("oh lala, estoy dando clases")
    }
  }

  class Alumno extends Persona(){
    def estudiarMateria() : Unit = {
      println("estudio estudio")
    }
  }

  class Pokemon(var nombre: String, var nivel : Int)

  trait CapturarPokemones {
    def capturarPokemon(pokemon : Pokemon) : Unit = {
      if(nivel >= pokemon.nivel) {
        println(s"Yo quiero ser el mejor! Mejor que nadie mas! Capture un ${pokemon.nombre}")
      }
      else{
        println(s"El pokemon ${pokemon.nombre} se ha escapado")
      }
    }

    def nivel : Int
  }

  trait conNivel { var nivel = 8}

  class Pokebola extends CapturarPokemones with conNivel


  object IntroAMixins extends App {
    val alumno = new Alumno with CapturarPokemones with conNivel
    val docente = new Docente with CapturarPokemones with conNivel
    val marowak = new Pokemon("Marowak", 25)
    alumno.nivel = 17
    docente.nivel = 40

    alumno capturarPokemon(marowak)
    docente capturarPokemon(marowak)
}
