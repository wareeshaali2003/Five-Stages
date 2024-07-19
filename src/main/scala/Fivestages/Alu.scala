package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

object ALUOP1 {
	val ALU_ADD = "b00000".U
    val ALU_ADDI = "b00000".U
    val ALU_SUB = "b01000".U
    val ALU_AND = "b00111".U
    val ALU_ANDI = "b00111".U
    val ALU_OR  = "b00110".U
    val ALU_ORI  = "b00110".U
    val ALU_XOR = "b00100".U
    val ALU_XORI = "b00100".U
    val ALU_SLT = "b00010".U
    val ALU_SLTI = "b00010".U
    val ALU_SLL = "b00001".U
    val ALU_SLLI = "b00001".U
    val ALU_SLTU= "b00011".U
    val ALU_SLTIU = "b00011".U
    val ALU_SRL = "b00101".U
    val ALU_SRLI = "b00101".U
    val ALU_SRA = "b01101".U
    val ALU_SRAI = "b00101".U
    val ALU_COPY_A = "b11111" .U  //JAL
    val ALU_XXX = DontCare
}

trait Config{
    val WLEN = 32
    val ALUOP_SIG_LEN = 5
}

import ALUOP1._

class ALUIO extends Bundle with Config {
    val in_A = Input(SInt(WLEN.W))
    val in_B = Input(SInt(WLEN.W))
    val alu_Op = Input(UInt(ALUOP_SIG_LEN.W))
    val out = Output(SInt(WLEN.W))
}

class ALU extends Module with Config {
    val io = IO(new ALUIO)

   io.out := 0.S

// AluOp := io.alu_Op
	when(io.alu_Op  === ALU_ADD ||io.alu_Op  === ALU_ADDI ){ 								
		io.out := io.in_A + io.in_B
	}.elsewhen(io.alu_Op  === ALU_SLL ||io.alu_Op  === ALU_SLLI ){ 							
		io.out:= (io.in_A << io.in_B(4,0)).asSInt
	}.elsewhen(io.alu_Op  === ALU_SLT || io.alu_Op  === ALU_SLTI ){							
		when(io.in_A < io.in_B){
			io.out := 1.S
		}.otherwise{
			io.out := 0.S
		}
	}.elsewhen(io.alu_Op  === ALU_SLTU | io.alu_Op  === ALU_SLTIU){			
		val Ua = io.in_A.asUInt
		val Ub = io.in_B.asUInt
		when(Ua < Ub){
			io.out := 1.S
		}.otherwise{
			io.out := 0.S
		}
	}.elsewhen(io.alu_Op  === ALU_XOR ||io.alu_Op  === ALU_XORI ){							
		io.out := io.in_A ^ io.in_B
	}.elsewhen(io.alu_Op  === ALU_SRL || io.alu_Op  === ALU_SRLI ){							
			
		val shift = io.in_A.asUInt >> (io.in_B(4,0)).asUInt
		io.out := shift.asSInt
	}.elsewhen(io.alu_Op  === ALU_OR || io.alu_Op  === ALU_ORI ){							
		io.out := io.in_A | io.in_B
	}.elsewhen(io.alu_Op  === ALU_AND || io.alu_Op  === ALU_ANDI ){							
		io.out := io.in_A & io.in_B
	}.elsewhen(io.alu_Op  === ALU_SUB){							
		io.out := io.in_A - io.in_B
	}.elsewhen(io.alu_Op  === ALU_SRA || io.alu_Op  === ALU_SRAI){							
		io.out := (io.in_A >> io.in_B(4,0)).asSInt
	}.elsewhen(io.alu_Op  === ALU_COPY_A){							
		io.out := io.in_A
	}

}