package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class control extends Module {
    val io = IO(new Bundle{
        val instr =  Input ( UInt (7. W ) )
        val memwrite = Output(Bool())
        val Branch = Output(Bool())
        val memread = Output(Bool())
        val regwrite =  Output(Bool())
        val memtoreg = Output(Bool())
        val aluop =  Output(UInt(3.W))
        val opA = Output(UInt(2.W))
        val opB = Output(Bool())
        val extendsel =  Output(UInt(2.W))
        val next_pc_sel = Output(UInt(2.W))
    })
  io.memwrite := false.B
  io.Branch := false.B
  io.memread := false.B
  io.regwrite := false.B
  io.memtoreg := false.B
  io.aluop := 0.U
  io.opA := 0.U
  io.opB := 0.B
  io.extendsel := 0.U
  io.next_pc_sel := 0.U
  
    when(io.instr === "b0110011".U) {  //R-type
    io.memwrite:= 0.B
    io.Branch:= 0.B
    io.memread := 0.B
    io.regwrite := 1.B
    io.memtoreg := 0.B
    io.aluop := 0.U
    io.opA := 0.U
    io.opB := 0.B
    io.extendsel := 0.U
    io.next_pc_sel := 0.U
  }
    .elsewhen(io.instr === "b0010011".U) { //I-type
    io.memwrite:= 0.B
    io.Branch:= 0.B
    io.memread := 0.B
    io.regwrite := 1.B
    io.memtoreg := 0.B
    io.aluop := 1.U
    io.opA := 0.U
    io.opB := 1.B
    io.extendsel := 0.U
    io.next_pc_sel := 0.U
  }
   .elsewhen(io.instr === "b1100011".U) { //SB-type
    io.memwrite:= 0.B
    io.Branch:= 1.B
    io.memread := 0.B
    io.regwrite := 0.B
    io.memtoreg := 0.B
    io.aluop := "b010".U
    io.opA := 0.U
    io.opB := 0.B
    io.extendsel := 0.U
    io.next_pc_sel :="b01".U
  }
   .elsewhen (io.instr === "b1101111".U){        //jal-type
            io. memwrite := 0.B
            io. Branch := 0.B
            io. memread := 0.B 
            io. regwrite := 1.B
            io. memtoreg := 0.B
            io. opA := "b01".U
            io. opB := 0.B
            io. extendsel := "b00".U
            io.next_pc_sel := "b10".U
            io.aluop := "b011".U
        }
    .elsewhen (io.instr === "b1100111".U){        //jalr-type
            io. memwrite := 0.B
            io. Branch := 0.B
            io. memread := 0.B 
            io. regwrite := 1.B
            io. memtoreg := 0.B
            io. opA := "b01".U
            io. opB := 1.B
            io. extendsel := "b00".U
            io.next_pc_sel := "b11".U
            io.aluop := "b011".U
        }
    .elsewhen (io.instr === "b0100011".U){        //s-type
            io. memwrite := 1.B
            io. Branch := 0.B
            io. memread := 0.B 
            io. regwrite := 1.B
            io. memtoreg := 0.B
            io. opA := "b00".U
            io. opB := 1.B
            io. extendsel := "b01".U
            io.next_pc_sel := "b00".U
            io.aluop := "b101".U
        }
    .elsewhen (io.instr === "b0000011".U){     //load-type
            io. memwrite := 0.B
            io. Branch := 0.B
            io. memread := 1.B 
            io. regwrite := 1.B
            io. memtoreg := 1.B
            io. opA := "b00".U
            io. opB := 1.B
            io. extendsel := "b00".U
            io.next_pc_sel := "b00".U
            io.aluop := "b100".U
        }
    .elsewhen (io.instr === "b0110111".U){     //lui-type
            io. memwrite := 0.B
            io. Branch := 0.B
            io. memread := 0.B 
            io. regwrite := 1.B
            io. memtoreg := 0.B
            io. opA := "b10".U
            io. opB := 1.B
            io. extendsel := "b10".U
            io.next_pc_sel := "b00".U
            io.aluop := "b110".U
        }
    }
