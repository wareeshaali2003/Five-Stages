package Fivestages
import chisel3._
import org.scalatest._
import chiseltest._

class Toptest extends FreeSpec with ChiselScalatestTester{
    "Top Test" in { 
        test(new Top()){ c=>{
        c.io.in.poke(1.U)
        c.clock.step(500)
        }
        }
    }
}