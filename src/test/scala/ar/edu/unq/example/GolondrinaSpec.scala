package ar.edu.unq.example

class GolondrinaSpec extends BaseSpec {

  "Una golondrina" should "perder energia al volar" in {
    val pepita = new Golondrina(100)
    pepita.energia shouldBe  100

    pepita.volar(75)
    pepita.energia shouldBe 25
  }

  it should "saber sus viajes largos" in {
    val pepita = new Golondrina(100)
    pepita.volar(75)
    pepita.volar(50)

    pepita.viajesLargos should be (List(75))
  }
}