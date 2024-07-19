package Fivestages
import chisel3._
import chisel3.util._
import chisel3.core

class Top extends Module {
  val io = IO(new Bundle {
  val in = Input(UInt(32.W))
  val out = Output(UInt(32.W))
  val addr = Output ( UInt ( 10 . W ) )
  })

  val Alu_mod = Module(new ALU)
  val Alu_Control_mod = Module(new alucntr)
  val branch_forward_mod =Module(new BFU)
  val Branch_Logic_mod = Module(new BLU)
  val hazard_Detection_mod = Module(new datahazards)
  val Data_Memory_mod = Module(new  data_Mem)
  val EX_MEM_mod = Module (new EX_MEM)
  val forward_Unit_mod = Module(new ForwardUnit)
  val ID_EX_mod = Module(new ID_EX)
  val IF_ID_mod = Module(new IF_ID)
  val Im_Gen_mod = Module(new ImmdValGen1)
  val Memory_mod = Module(new instr_Mem)
  val Jalr_mod = Module(new jalr)
  val MEM_WB_mod = Module(new MEM_WB)
  val Pc_mod = Module(new PC)
	val Control_mod = Module(new control)
  val reg_mod = Module(new  RegFile)
	val structural_Detector_mod =Module(new STRDetector)
  
//  forward unit
forward_Unit_mod.io.EX_MEM_REGRD := EX_MEM_mod.io.rd_sel_out
forward_Unit_mod.io.ID_EX_REGRS1 := ID_EX_mod.io.rs1_sel_out
forward_Unit_mod.io.ID_EX_REGRS2 := ID_EX_mod.io.rs2_sel_out 
forward_Unit_mod.io.EX_MEM_REGWR := EX_MEM_mod.io.regwr_out
forward_Unit_mod.io.MEM_WB_REGRD := MEM_WB_mod.io.rd_sel_out
forward_Unit_mod.io.MEM_WB_REGWR := MEM_WB_mod.io.regwr_out

Pc_mod.io.in := Pc_mod.io.pc4
Memory_mod.io.addr := Pc_mod.io.pc(11,2)
IF_ID_mod.io.pc_in := Pc_mod.io.pc
IF_ID_mod.io.pc4_in := Pc_mod.io.pc4
IF_ID_mod.io.inst_in := Memory_mod.io.output
Control_mod.io.instr := IF_ID_mod.io.inst_in_out(6,0)
io.addr := Memory_mod.io.output(9,0)

// regfile module connection
reg_mod.io.rs1 := IF_ID_mod.io.inst_in_out(19, 15)
reg_mod.io.rs2 := IF_ID_mod.io.inst_in_out(24, 20)

// immidiate inputs 
Im_Gen_mod.io.instruction := IF_ID_mod.io.inst_in_out 
Im_Gen_mod.io.pc := IF_ID_mod.io.pc_in_out 

// connect alu control to the execution stage
Alu_Control_mod.io.aluop := ID_EX_mod.io.AluOp_out
Alu_Control_mod.io.func3 := ID_EX_mod.io.funct3_out
Alu_Control_mod.io.func7 := ID_EX_mod.io.funct7_out

// structural Detector
structural_Detector_mod.io.rs1_sel := IF_ID_mod.io.inst_in_out(19, 15)
structural_Detector_mod.io.rs2_sel := IF_ID_mod.io.inst_in_out(24, 20)
structural_Detector_mod.io.MEM_WB_REGRD := MEM_WB_mod.io.rd_sel_out
structural_Detector_mod.io.MEM_WB_RegWR := MEM_WB_mod.io.regwr_out
// rs1
ID_EX_mod.io.rs1_in := MuxCase(0.S, Array(
  (structural_Detector_mod.io.fwd_rs1  === 0.U) -> reg_mod.io.rdata1,
  (structural_Detector_mod.io.fwd_rs1  ===  1.U) -> reg_mod.io.wdata,
))
// rs2
ID_EX_mod.io.rs2_in := MuxCase(0.S, Array(
  (structural_Detector_mod.io.fwd_rs2  === 0.U) -> reg_mod.io.rdata2,
  (structural_Detector_mod.io.fwd_rs2  ===  1.U) -> reg_mod.io.wdata,
))
// Hazard detection
when(hazard_Detection_mod.io.ctrl_forward === "b1".U) {
    ID_EX_mod.io.memwr_in := 0.U
    ID_EX_mod.io.memrd_in := 0.U
    ID_EX_mod.io.br_in := 0.U
    ID_EX_mod.io.regwr_in := 0.U
    ID_EX_mod.io.memToreg_in := 0.U
    ID_EX_mod.io.AluOp_in := 0.U
    ID_EX_mod.io.OpA_sel_in := 0.U
    ID_EX_mod.io.OpB_sel_in  := 0.U
    ID_EX_mod.io.nextpc_sel_in := 0.U
} .otherwise {
    ID_EX_mod.io.memwr_in := Control_mod.io.memwrite
    ID_EX_mod.io.memrd_in := Control_mod.io.memread
    ID_EX_mod.io.br_in := Control_mod.io.Branch
    ID_EX_mod.io.regwr_in := Control_mod.io.regwrite.asUInt
    ID_EX_mod.io.memToreg_in := Control_mod.io.memtoreg
    ID_EX_mod.io.AluOp_in := Control_mod.io.aluop
    ID_EX_mod.io.OpA_sel_in := Control_mod.io.opA 
    ID_EX_mod.io.OpB_sel_in  := Control_mod.io.opB 
    ID_EX_mod.io.nextpc_sel_in := Control_mod.io.next_pc_sel
}
branch_forward_mod.io.ID_EX_REGRD := ID_EX_mod.io.rd_sel_out
branch_forward_mod.io.ID_EX_MEMRD := ID_EX_mod.io.memrd_out
branch_forward_mod.io.EX_MEM_REGRD := EX_MEM_mod.io.rd_sel_out
branch_forward_mod.io.MEM_WB_REGRD := MEM_WB_mod.io.rd_sel_out
branch_forward_mod.io.EX_MEM_MEMRD := EX_MEM_mod.io.memrd_out
branch_forward_mod.io.MEM_WB_MEMRD := MEM_WB_mod.io.memrd_out
branch_forward_mod.io.rs1_sel := IF_ID_mod.io.inst_in_out(19,15)
branch_forward_mod.io.rs2_sel := IF_ID_mod.io.inst_in_out(24, 20)
branch_forward_mod.io.ctrl_branch := Control_mod.io.Branch

Branch_Logic_mod.io.rs1 := reg_mod.io.rdata1
Branch_Logic_mod.io.rs2 := reg_mod.io.rdata2
Branch_Logic_mod.io.funct3 := IF_ID_mod.io.inst_in_out(14,12)

// jalr
Jalr_mod.io.rs1 := reg_mod.io.rdata1 
Jalr_mod.io.imm :=  Im_Gen_mod.io.i_imm
// RS1 branch forward unit
Branch_Logic_mod.io.rs1 := MuxCase(0.S, Array(
  (branch_forward_mod.io.forward_rs1 === "b0000".U) -> reg_mod.io.rdata1,
  (branch_forward_mod.io.forward_rs1 === "b0001".U) -> Alu_mod.io.out,
  (branch_forward_mod.io.forward_rs1 === "b0010".U) -> EX_MEM_mod.io.Alu_out_out,
  (branch_forward_mod.io.forward_rs1 === "b0011".U) -> reg_mod.io.wdata,
  (branch_forward_mod.io.forward_rs1 === "b0100".U) -> Data_Memory_mod.io.output,
  (branch_forward_mod.io.forward_rs1 === "b0101".U) -> reg_mod.io.wdata,
  (branch_forward_mod.io.forward_rs1 === BitPat("b????")) -> reg_mod.io.rdata1

))
Jalr_mod.io.rs1 := MuxCase(0.S, Array(
  (branch_forward_mod.io.forward_rs1 === "b0000".U) -> reg_mod.io.rdata1,
  (branch_forward_mod.io.forward_rs1 === "b0001".U) -> reg_mod.io.rdata1,
  (branch_forward_mod.io.forward_rs1 === "b0010".U) -> reg_mod.io.rdata1,
  (branch_forward_mod.io.forward_rs1 === "b0011".U) -> reg_mod.io.rdata1,
  (branch_forward_mod.io.forward_rs1 === "b0100".U) -> reg_mod.io.rdata1,
  (branch_forward_mod.io.forward_rs1 === BitPat("b????")) -> reg_mod.io.rdata1
))
// RS2 branch logic unit
Branch_Logic_mod.io.rs2 := MuxCase(0.S, Array(
  (branch_forward_mod.io.forward_rs2 === "b0000".U) -> reg_mod.io.rdata2 ,
  (branch_forward_mod.io.forward_rs2 === "b0001".U) -> Alu_mod.io.out ,
  (branch_forward_mod.io.forward_rs2 === "b0010".U) -> EX_MEM_mod.io.Alu_out_out ,
  (branch_forward_mod.io.forward_rs2 === "b0011".U) -> reg_mod.io.wdata ,
  (branch_forward_mod.io.forward_rs2 === "b0100".U) -> Data_Memory_mod.io.output ,
  (branch_forward_mod.io.forward_rs2 === "b0101".U) -> reg_mod.io.wdata ,
  (branch_forward_mod.io.forward_rs2 === BitPat("b????")) -> reg_mod.io.rdata2
))
// Decode Execute satage
ID_EX_mod.io.pc :=  IF_ID_mod.io.pc_in_out
ID_EX_mod.io.pc4 :=  IF_ID_mod.io.pc4_in_out
ID_EX_mod.io.memwr_in := Control_mod.io.memwrite
ID_EX_mod.io.br_in := Control_mod.io.Branch
ID_EX_mod.io.memrd_in := Control_mod.io.memread
ID_EX_mod.io.regwr_in := Control_mod.io.regwrite.asUInt
ID_EX_mod.io.memToreg_in := Control_mod.io.memtoreg
ID_EX_mod.io.AluOp_in := Control_mod.io.aluop
ID_EX_mod.io.OpA_sel_in := Control_mod.io.opA 
ID_EX_mod.io.OpB_sel_in := Control_mod.io.opB
ID_EX_mod.io.nextpc_sel_in := Control_mod.io.next_pc_sel
ID_EX_mod.io.funct3_in :=  IF_ID_mod.io.inst_in_out(14, 12)
ID_EX_mod.io.funct7_in :=  IF_ID_mod.io.inst_in_out(30)
ID_EX_mod.io.rs1_in := reg_mod.io.rdata1
ID_EX_mod.io.rs2_in := reg_mod.io.rdata2
ID_EX_mod.io.rs1_sel_in := IF_ID_mod.io.inst_in_out(19, 15)
ID_EX_mod.io.rs2_sel_in := IF_ID_mod.io.inst_in_out(24, 20)
ID_EX_mod.io.rd_sel_in :=  IF_ID_mod.io.inst_in_out(11, 7)

hazard_Detection_mod.io.IF_ID_INST := IF_ID_mod.io.inst_in_out
hazard_Detection_mod.io.ID_EX_MEMREAD := ID_EX_mod.io.memrd_out
hazard_Detection_mod.io.ID_EX_REGRD := ID_EX_mod.io.rd_sel_out
hazard_Detection_mod.io.pc_in := IF_ID_mod.io.pc4_in_out
hazard_Detection_mod.io.current_pc := IF_ID_mod.io.pc_in_out

when(hazard_Detection_mod.io.inst_forward === "b1".U) {
    IF_ID_mod.io.inst_in := hazard_Detection_mod.io.inst_out.asUInt
    IF_ID_mod.io.pc_in := hazard_Detection_mod.io.current_pc_out.asUInt}
     .otherwise {
        IF_ID_mod.io.inst_in := Memory_mod.io.output
    }
when(hazard_Detection_mod.io.pc_forward === "b1".U) {
  Pc_mod.io.in := hazard_Detection_mod.io.pc_out
} .otherwise {
    when(Control_mod.io.next_pc_sel === "b01".U) {
      when(Branch_Logic_mod.io.output === 1.U && Control_mod.io.Branch === 1.B) {
        Pc_mod.io.in := Im_Gen_mod.io.sb_imm.asUInt
        IF_ID_mod.io.pc_in := 0.U
        IF_ID_mod.io.pc4_in := 0.U
        IF_ID_mod.io.inst_in := 0.U
      } .otherwise {
        Pc_mod.io.in := Pc_mod.io.pc4
      }
}.elsewhen(Control_mod.io.next_pc_sel === "b10".U) {
      Pc_mod.io.in := Im_Gen_mod.io.uj_imm.asUInt
      IF_ID_mod.io.pc_in := 0.U
      IF_ID_mod.io.pc4_in := 0.U
      IF_ID_mod.io.inst_in := 0.U
    }.elsewhen(Control_mod.io.next_pc_sel === "b11".U) {
      Pc_mod.io.in := (Jalr_mod.io.out).asUInt
      IF_ID_mod.io.pc_in := 0.U
      IF_ID_mod.io.pc4_in := 0.U
      IF_ID_mod.io.inst_in := 0.U
    }
      .otherwise {
      Pc_mod.io.in := Pc_mod.io.pc4
    }}

EX_MEM_mod.io.regwr_in := ID_EX_mod.io.regwr_out
EX_MEM_mod.io.memToreg_in := ID_EX_mod.io.memToreg_out
EX_MEM_mod.io.rs2_in := ID_EX_mod.io.rs2_out
EX_MEM_mod.io.Alu_out_in := Alu_mod.io.out 
EX_MEM_mod.io.rd_sel_in := ID_EX_mod.io.rd_sel_out
// IN A
Alu_mod.io.in_A := 0.S
when (ID_EX_mod.io.OpA_sel_out === "b10".U) {
    Alu_mod.io.in_A := ID_EX_mod.io.pc4_out.asSInt
  } .otherwise{
when(forward_Unit_mod.io.forward_a === "b00".U) {
  Alu_mod.io.in_A := ID_EX_mod.io.rs1_out 
} .elsewhen(forward_Unit_mod.io.forward_a === "b01".U) {
  Alu_mod.io.in_A := EX_MEM_mod.io.Alu_out_out
} .elsewhen(forward_Unit_mod.io.forward_a === "b10".U) {
  Alu_mod.io.in_A := reg_mod.io.wdata
} .otherwise {
  Alu_mod.io.in_A := ID_EX_mod.io.rs1_out
}}
//InB

// immidiate gen mux
ID_EX_mod.io.imm := 0.S
// val immdate = RegInit(0.S(32.W))

ID_EX_mod.io.imm  := MuxCase(0.S, Array(
  (Control_mod.io.extendsel === "b00".U) -> Im_Gen_mod.io.i_imm,
  (Control_mod.io.extendsel === "b01".U) -> Im_Gen_mod.io.s_imm,
  (Control_mod.io.extendsel === "b10".U) -> Im_Gen_mod.io.u_imm
))
// ID_EX_mod.io.imm := immdate
Alu_mod.io.in_B := 0.S
when(ID_EX_mod.io.OpB_sel_out === 1.B){
		Alu_mod.io.in_B := ID_EX_mod.io.imm_out
	when (forward_Unit_mod.io.forward_b === "b00".U){EX_MEM_mod.io.rs2_in := ID_EX_mod.io.rs2_out}
		.elsewhen (forward_Unit_mod.io.forward_b === "b01".U  ){EX_MEM_mod.io.rs2_in:= EX_MEM_mod.io.Alu_out_out}
		.elsewhen (forward_Unit_mod.io.forward_b === "b10".U){EX_MEM_mod.io.rs2_in := reg_mod.io.wdata}
    .otherwise {
		EX_MEM_mod.io.rs2_in := ID_EX_mod.io.rs2_out
		}
	}                      
	.otherwise{
		when(forward_Unit_mod.io.forward_b === "b00".U) {
   Alu_mod.io.in_B :=  ID_EX_mod.io.rs2_out
    EX_MEM_mod.io.rs2_in:=  ID_EX_mod.io.rs2_out
  } .elsewhen(forward_Unit_mod.io.forward_b === "b01".U) {
    Alu_mod.io.in_B := EX_MEM_mod.io.Alu_out_out
    EX_MEM_mod.io.rs2_in := EX_MEM_mod.io.Alu_out_out
  } .elsewhen(forward_Unit_mod.io.forward_b === "b10".U) {
    Alu_mod.io.in_B := reg_mod.io.wdata
    EX_MEM_mod.io.rs2_in := reg_mod.io.wdata
  } .otherwise {
    Alu_mod.io.in_B:=  ID_EX_mod.io.rs2_out
    EX_MEM_mod.io.rs2_in :=  ID_EX_mod.io.rs2_out
	}}

Alu_mod.io.alu_Op := Alu_Control_mod.io.alucontrol
// mem
EX_MEM_mod.io.memwr_in := ID_EX_mod.io.memwr_out
EX_MEM_mod.io.memrd_in := ID_EX_mod.io.memrd_out
// datamem
Data_Memory_mod.io.addr := (EX_MEM_mod.io.Alu_out_out).asUInt
Data_Memory_mod.io.data := EX_MEM_mod.io.rs2_out 
Data_Memory_mod.io.wen := EX_MEM_mod.io.memwr_out
Data_Memory_mod.io.ren := EX_MEM_mod.io.memrd_out 
// WRITE BACK (WB) STAGE 
MEM_WB_mod.io.regwr_in := EX_MEM_mod.io.regwr_out
MEM_WB_mod.io.memToreg_in := EX_MEM_mod.io.memToreg_out
MEM_WB_mod.io.memrd_in := EX_MEM_mod.io.memrd_out
MEM_WB_mod.io.memwrite_in := EX_MEM_mod.io.memwr_out
MEM_WB_mod.io.in_dataMem := Data_Memory_mod.io.output
MEM_WB_mod.io.in_ALU_out := EX_MEM_mod.io.Alu_out_out
MEM_WB_mod.io.rd_sel_in := EX_MEM_mod.io.rd_sel_out

reg_mod.io.wdata := MuxCase(0.S, Array(
  (MEM_WB_mod.io.memToreg_out === 0.B) -> MEM_WB_mod.io.Alu_out ,
  (MEM_WB_mod.io.memToreg_out === 1.B) -> MEM_WB_mod.io.dataMem_out
))
reg_mod.io.RegWrite := MEM_WB_mod.io.regwr_out
reg_mod.io.rd := MEM_WB_mod.io.rd_sel_out



// // register out going into ID_EX inputs
// ID_EX_mod.io.rs1_in := reg_mod.io.rdata1 
// ID_EX_mod.io.rs2_in := reg_mod.io.rdata2

// ID_EX_mod.io.rs1_in := reg_mod.io.rdata1 
// ID_EX_mod.io.rs2_in := reg_mod.io.rdata2

// reg_mod.io.wdata := 0.S

// reg_mod.io.wdata := MuxCase(0.S, Array(
//   (Control_mod.io.memtoreg === 0.B) -> MEM_WB_mod.io.Alu_out ,
//   (Control_mod.io.memtoreg === 1.B) -> MEM_WB_mod.io.dataMem_out
//
// ID_EX_mod.io.imm := immdate
// EXECUTION Memory (EX) STAGE
// Alu_mod.io.in_A := ID_EX_mod.io.rs1_out
// val mux = Mux (Control_mod.io.opB, immdate, ID_EX_mod.io.rs2_out )
// Alu_mod.io.in_B := mux
// Alu_Control_mod.io.aluop := Control_mod.io.aluop
// EX_MEM_mod.io.memwr_in := Control_mod.io.memwrite
// EX_MEM_mod.io.memrd_in := Control_mod.io.memread
// Memory Stage
// EX_MEM_mod.io.Alu_out_in := MEM_WB_mod.io.Alu_out
// EX_MEM_mod.io.memrd_in := MEM_WB_mod.io.memrd_out
// EX_MEM_mod.io.regwr_in := MEM_WB_mod.io.regwr_out
// EX_MEM_mod.io.memToreg_in := MEM_WB_mod.io.memToreg_out
// EX_MEM_mod.io.rd_sel_in := MEM_WB_mod.io.rd_sel_out
io.out := 0.U
}
