package Fivestages
import chisel3._
import org.scalatest._
import chiseltest._
class IF_IDtest extends FreeSpec with ChiselScalatestTester{
    "IF_ID test" in {
        test(new IF_ID()){ a =>
        a.io.pc_in.poke(1.U)
		a.io.pc4_in.poke(2.U)
		a.io.inst_in.poke(3.U)
		a.io.pc_in_out.expect(1.U)
		a.io.pc4_in_out.expect(2.U)
		a.io.inst_in_out.expect(3.U)
        }
    }
}