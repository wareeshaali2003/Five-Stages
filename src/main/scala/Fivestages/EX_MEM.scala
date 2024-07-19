package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class EX_MEM  extends Module {
val io = IO ( new Bundle {   
    // inputs
val memwr_in = Input(UInt(1.W))
val memrd_in = Input(UInt(1.W))
val regwr_in = Input(UInt(1.W))
val memToreg_in = Input(UInt(1.W))
val rs2_in = Input(SInt(32.W))
val Alu_out_in = Input (SInt(32.W))
val rd_sel_in = Input(UInt(5.W))
//Output
val rs2_out = Output(SInt(32.W))
val Alu_out_out = Output(SInt(32.W))
val rd_sel_out = Output(UInt(5.W))
val memwr_out = Output(UInt(1.W))
val memrd_out = Output(UInt(1.W))
val regwr_out = Output(UInt(1.W))
val memToreg_out = Output(UInt(1.W))
})
// initializing each variable so we can store value in it  later on wire it with  the actual signal 
val reg_memwr_in = RegInit(0.U(1.W))
val reg_memrd_in =  RegInit(0.U(1.W))
val reg_regwr_in = RegInit(0.U(1.W))
val reg_memToreg_in = RegInit(0.U(1.W))
val reg_rs2_in = RegInit(0.S(32.W))
val reg_Alu_out_in = RegInit(0.S(32.W))
val reg_rd_sel_in =  RegInit(0.U(5.W))
// wiring each reg with input so register will store the value of input for later use
reg_rs2_in := io.rs2_in
reg_rd_sel_in := io.rd_sel_in
reg_memwr_in := io.memwr_in
reg_memrd_in := io.memrd_in
reg_regwr_in := io.regwr_in
reg_memToreg_in := io.memToreg_in
reg_Alu_out_in := io.Alu_out_in
//  output signal to registers
io.rs2_out := reg_rs2_in
io.rd_sel_out := reg_rd_sel_in
io.memwr_out := reg_memwr_in
io.memrd_out := reg_memrd_in
io.regwr_out := reg_regwr_in
io.memToreg_out := reg_memToreg_in
io.Alu_out_out := reg_Alu_out_in

}