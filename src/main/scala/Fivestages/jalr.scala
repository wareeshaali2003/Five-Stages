package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class jalr extends Module {
  val io = IO (new Bundle {
	val rs1 = Input(SInt(32.W))
	val imm = Input(SInt(32.W))
	val out = Output(SInt(32.W))
  })
//   rs1 batai ga kidhar jump karna hay konsa register 
	val address = io.rs1 + io.imm
	io.out := address & 4294967294L.S
}