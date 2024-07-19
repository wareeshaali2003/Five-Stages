package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class IF_ID  extends Module {
val io = IO ( new Bundle {
val pc_in = Input (UInt(32.W))
val pc4_in = Input (UInt(32.W))
val inst_in = Input (UInt(32.W))
val pc_in_out = Output (UInt(32.W))
val pc4_in_out = Output (UInt(32.W))
val inst_in_out = Output (UInt(32.W))
})
val reg_pc_in = RegInit(0.U(32.W))
val reg_pc4_in = RegInit(0.U(32.W))
val reg_inst_in = RegInit(0.U(32.W))
reg_pc_in :=  io.pc_in
reg_pc4_in :=  io.pc4_in
reg_inst_in :=   io.inst_in
io.pc_in_out := reg_pc_in
io.pc4_in_out := reg_pc4_in
io.inst_in_out := reg_inst_in
}