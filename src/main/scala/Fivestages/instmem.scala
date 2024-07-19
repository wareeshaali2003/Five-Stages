package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core
import chisel3.util.experimental.loadMemoryFromFile

class instr_Mem extends Module{
val io = IO (new Bundle {
val addr = Input(UInt(10.W))
val output = Output(UInt(32.W))

})

val imemm = Mem(1024, UInt(32.W))
io.output := 0.U
loadMemoryFromFile (imemm,"D:/Scala-Chisel-Learning-Journey/src/main/scala/Fivestages/testfile.txt")
io.output := imemm.read(io.addr)

}