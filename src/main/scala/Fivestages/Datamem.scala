package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class data_Mem extends Module {
val io = IO (new Bundle {
val addr = Input(UInt(8.W))
val data = Input(SInt(32.W))
val wen = Input ( Bool() )
val ren = Input ( Bool() )
val output = Output(SInt(32.W))

})
io.output:=0.S
val memory = Mem (1024,SInt(32.W))

 when(io.wen === 1.B) {
   memory.write ( io.addr.asUInt , io.data )
  }
  .elsewhen(io.ren === 1.B) {
           io.output := memory.read( io.addr.asUInt)
        }
.otherwise {
    // Provide a default assignment for io.output
    io.output := 0.S
  }

}