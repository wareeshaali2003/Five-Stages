package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class ID_EX  extends Module {
val io = IO ( new Bundle {
    // inputs
val pc = Input (UInt(32.W))
val pc4 = Input (UInt(32.W))
val memwr_in = Input(UInt(1.W))
val br_in = Input(UInt(1.W))
val memrd_in = Input(UInt(5.W))
val regwr_in = Input(UInt(1.W))
val memToreg_in = Input(UInt(1.W))
val AluOp_in = Input(UInt(3.W))
val OpA_sel_in = Input(UInt(2.W))
val OpB_sel_in = Input(UInt(1.W))
val nextpc_sel_in = Input(UInt(2.W))
val imm = Input (SInt(32.W))
val funct3_in = Input(UInt(3.W))
val funct7_in = Input(UInt(1.W))
val rs1_sel_in = Input(UInt(5.W))
val rs2_sel_in = Input(UInt(5.W))
val rs1_in = Input(SInt(32.W))
val rs2_in = Input(SInt(32.W))
val rd_sel_in = Input(UInt(5.W))
//Output
val pc_out = Output (UInt(32.W))
val pc4_out = Output (UInt(32.W))
val rs1_sel_out = Output (UInt(5.W))
val rs2_sel_out = Output (UInt(5.W))
val rs1_out = Output ( SInt ( 32. W ) )
val rs2_out = Output ( SInt ( 32. W ) )
val imm_out = Output (SInt(32.W))
val rd_sel_out = Output (UInt(5.W))
val funct3_out = Output (UInt(3.W))
val funct7_out = Output (UInt(1.W))
val memwr_out = Output (UInt(1.W))
val memrd_out = Output (UInt(1.W))
// val br_out = Output (UInt(1.W))
val regwr_out = Output (UInt(1.W))
val memToreg_out = Output (UInt(1.W))
val AluOp_out = Output (UInt(3.W))
val OpA_sel_out = Output (UInt(2.W))
val OpB_sel_out = Output (UInt(1.W))
// val nextpc_sel_out = Output (UInt(2.W))
})

// initializing each variable so we can store value in it  later on wire it with  the actual signal 
val reg_pc = RegInit(0.U(32.W))
val reg_pc4 = RegInit(0.U(32.W))
val reg_rs1_sel_in = RegInit(0.U(32.W))
val reg_rs2_sel_in = RegInit(0.U(32.W))
val reg_rs1_in = RegInit(0.S(5.W))
val reg_rs2_in = RegInit(0.S(5.W))
val reg_imm = RegInit(0.S(32.W))
val reg_rd_sel_in = RegInit(0.U(5.W))
val reg_funct3_in = RegInit(0.U(32.W))
val reg_funct7_in = RegInit(0.U(32.W))
val reg_memwr_in = RegInit(0.U(32.W))
val reg_memrd_in = RegInit(0.U(32.W))
// val reg_br_in = RegInit(0.U(1.W))
val reg_regwr_in = RegInit(0.U(32.W))
val reg_memToreg_in = RegInit(0.U(32.W))
val reg_AluOp_in = RegInit(0.U(32.W))
val reg_OpA_sel_in = RegInit(0.U(2.W))
val reg_OpB_sel_in = RegInit(0.U(1.W))
// val reg_operandA_in = RegInit(0.S(32.W))
// val reg_operandB_in = RegInit(0.S(32.W))
// val reg_nextpc_sel_in = RegInit(0.U(2.W))

// wiring each reg with input so register will store the value of input for later use
reg_pc :=  io.pc
reg_pc4 :=  io.pc4
reg_rs1_sel_in := io.rs1_sel_in
reg_rs2_sel_in := io.rs2_sel_in
reg_rs1_in := io.rs1_in
reg_rs2_in := io.rs2_in
reg_imm := io.imm
reg_rd_sel_in := io.rd_sel_in
reg_funct3_in := io.funct3_in
reg_funct7_in := io.funct7_in
reg_memwr_in := io.memwr_in
reg_memrd_in := io.memrd_in
// reg_br_in := io.br_in
reg_regwr_in := io.regwr_in
reg_memToreg_in := io.memToreg_in
reg_AluOp_in := io.AluOp_in
reg_OpA_sel_in := io.OpA_sel_in
reg_OpB_sel_in := io.OpB_sel_in
// reg_operandA_in := io.operandA_in
// reg_operandB_in := io.operandB_in
// reg_nextpc_sel_in := io.nextpc_sel_in

//  output signal to registers

io.pc_out := reg_pc
io.pc4_out := reg_pc4
io.rs1_sel_out := reg_rs1_sel_in
io.rs2_sel_out := reg_rs2_sel_in
io.rs1_out := reg_rs1_in
io.rs2_out := reg_rs2_in
io.imm_out := reg_imm
io.rd_sel_out := reg_rd_sel_in
io.funct3_out := reg_funct3_in
io.funct7_out := reg_funct7_in 
io.memwr_out := reg_memwr_in
io.memrd_out := reg_memrd_in
// io.br_out := reg_br_in
io.regwr_out := reg_regwr_in
io.memToreg_out := reg_memToreg_in
io.AluOp_out := reg_AluOp_in
io.OpA_sel_out := reg_OpA_sel_in
io.OpB_sel_out := reg_OpB_sel_in
// io.operandA_out := reg_operandA_in
// io.operandB_out := reg_operandB_in
// io.nextpc_sel_out := reg_nextpc_sel_in

}