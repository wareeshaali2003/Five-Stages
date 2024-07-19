package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class Interface_BLU extends Bundle {

val funct3 = Input (UInt (3. W ) )
val rs1 = Input (SInt(32.W ))
val rs2 = Input (SInt(32.W ))
val output = Output (UInt(1.W))

}

class BLU extends Module {

val io = IO ( new Interface_BLU )

// Start Coding here
io . output  := 0.U

when( io.rs1 === io.rs2 && io.funct3 === 0. U ) {
    // beq 
    io . output  :=  1.U 
}
 .elsewhen( io.rs1 =/= io.rs2 && io.funct3 === 1. U ) {
    // bne
  io . output  :=  1.U 
}
.elsewhen( io.rs1 > io.rs2 && io.funct3 === 2. U ) {
    // blt
  io . output  :=  1.U 
}
.elsewhen( io.rs1 < io.rs2 && io.funct3 === 3. U) {
    // bge
  io . output  :=  1.U 
}
.elsewhen( io.rs1 <= io.rs2 && io.funct3 === 4. U ) {
    // bltu
  io . output  :=  1.U 
}
.elsewhen( io.rs1 >= io.rs2 && io.funct3 === 5. U ) {
    // bgeu
  io . output  :=  1.U 
}}

// End your code here
// Well , you can actually write classes too . So , technically you have no

