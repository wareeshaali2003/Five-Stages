package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class MEM_WB  extends Module {
val io = IO ( new Bundle {
    // inputs
val rd_sel_in = Input (UInt(5.W))
val memrd_in = Input (Bool())
val regwr_in = Input (Bool())
val memToreg_in = Input (Bool())
val memwrite_in = Input(Bool())
val in_ALU_out = Input (SInt(32.W))
val in_dataMem = Input (SInt(32.W))
//Output
val rd_sel_out = Output (UInt(5.W))
val memrd_out = Output (Bool())
val regwr_out = Output (Bool())
val memwrite_out = Output(Bool())
val memToreg_out = Output (Bool())
val Alu_out = Output (SInt(32.W))
val dataMem_out = Output (SInt(32.W))
})
// initializing each variable so we can store value in it  later on wire it with  the actual signal 
val reg_rd_sel_in = RegInit(0.U(5.W))
val reg_memrd_in = RegInit(0.B)
val reg_regwr_in = RegInit(0.B)
val reg_memwrite_in = RegInit(0.B)
val reg_memToreg_in = RegInit(0.B)
val reg_in_ALU_out = RegInit(0.S(32.W))
val reg_in_dataMem = RegInit(0.S(32.W))
// wiring each reg with input so register will store the value of input for later use
reg_rd_sel_in := io.rd_sel_in
reg_memrd_in := io.memrd_in
reg_regwr_in := io.regwr_in
reg_memwrite_in := io.memwrite_in
reg_memToreg_in := io.memToreg_in
reg_in_ALU_out := io.in_ALU_out
reg_in_dataMem := io.in_dataMem

//  output signal to registers

io.rd_sel_out := reg_rd_sel_in
io.memrd_out := reg_memrd_in
io.regwr_out := reg_regwr_in
io.memwrite_out := reg_memwrite_in
io.memToreg_out := reg_memToreg_in
io.Alu_out := reg_in_ALU_out
io.dataMem_out := reg_in_dataMem 
}