package single

import chisel3._
import chisel3.util._

import common.Constants._
import common.InstPattern._

class Core extends Module{
  val io = IO(new Bundle{
    val imem = Flipped(new IMemPortIO())
    val dmem = Flipped(new DMemPortIO())
    val exit = Output(Bool())
  })
  /* 基础设施 */
  val regfile = Mem(32, UInt(WORD_LEN.W))
  val pc_reg = RegInit(START_ADDR)
  val csr_regfile = Mem(4096, UInt(WORD_LEN.W))
  /* IF取指 */
  val inst = io.imem.inst
  // pc_reg := pc_reg + 4.U(WORD_LEN.W)
  val pc_plus4 = pc_reg + 4.U(WORD_LEN.W)
  val br_flg = Wire(Bool())
  val br_target = Wire(UInt(WORD_LEN.W))
  val jmp_flg = (inst === JAL || inst === JALR)
  val alu_out = Wire(UInt(WORD_LEN.W))

  val pc_next = MuxCase(pc_plus4, Seq(
    br_flg -> br_target,
    jmp_flg -> alu_out
  ))
  pc_reg := pc_next
  io.imem.addr := pc_reg
  /*=============== 译码阶段 ================*/
  val rs1_addr = inst(19, 15)
  val rs2_addr = inst(24, 20)
  val wb_addr = inst(11, 7) // 即rd
  val rs1_data = Mux((rs1_addr=/=0.U(WORD_LEN.W)), 
    regfile(rs1_addr), 0.U(WORD_LEN.W))
  val rs2_data = Mux((rs2_addr=/=0.U(WORD_LEN.W)),
    regfile(rs2_addr), 0.U(WORD_LEN.W))
  val csr_addr = inst(31,20)
  val csr_rdata = csr_regfile(csr_addr)
  val imm_i = inst(31,20)
  val imm_i_sext = Cat(Fill(20, imm_i(11)), imm_i)
  val imm_s = Cat(inst(31,25), inst(11,7))
  val imm_s_sext = Cat(Fill(20, imm_i(11)), imm_i)
  val imm_b = Cat(inst(31), inst(7), inst(30, 25), inst(11,8))
  val imm_b_sext = Cat(Fill(19, imm_b(11)), imm_b, 0.U(1.W))
  val imm_j = Cat(inst(31), inst(19, 12), inst(20), inst(30,21))
  val imm_j_sext = Cat(Fill(11, imm_j(19)), imm_j, 0.U(1.W))
  val imm_u = inst(31, 12)
  val imm_u_shifted = Cat(imm_u, Fill(12, 0.U))
  val imm_z = inst(19, 15)
  val imm_z_uext = Cat(Fill(27, 0.U), imm_z)
  val csignals = ListLookup(
    inst, List(ALU_X, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_MEM, CSR_X),
    Array(
      LW -> List(ALU_ADD, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_MEM, CSR_X),
      SW -> List(ALU_ADD, OP1_RS1, OP2_IMS, MEN_S, REN_X, WB_X, CSR_X),
      ADD -> List(ALU_ADD, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      ADDI -> List(ALU_ADD, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_ALU, CSR_X),
      SUB -> List(ALU_SUB, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      AND -> List(ALU_AND, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      OR -> List(ALU_OR, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      XOR -> List(ALU_XOR, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      ANDI -> List(ALU_AND, OP1_RS1, OP2_IMS, MEN_X, REN_S, WB_ALU, CSR_X),
      ORI -> List(ALU_OR, OP1_RS1, OP2_IMS, MEN_X, REN_S, WB_ALU, CSR_X),
      XORI -> List(ALU_XOR, OP1_RS1, OP2_IMS, MEN_X, REN_S, WB_ALU, CSR_X),
      SLL -> List(ALU_SLL, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      SRL -> List(ALU_SRL, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      SRA -> List(ALU_SRA, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      SLLI -> List(ALU_SLL, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_ALU, CSR_X),
      SRLI -> List(ALU_SRL, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_ALU, CSR_X),
      SRAI -> List(ALU_SRA, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_ALU, CSR_X),
      SLT -> List(ALU_SLT, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      SLTU -> List(ALU_SLTU, OP1_RS1, OP2_RS2, MEN_X, REN_S, WB_ALU, CSR_X),
      SLTI -> List(ALU_SLT, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_ALU, CSR_X),
      SLTIU -> List(ALU_SLTU, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_ALU, CSR_X),
      BEQ -> List(BR_BEQ, OP1_RS1, OP2_RS2, MEN_X, REN_X, WB_X, CSR_X),
      BNE -> List(BR_BNE, OP1_RS1, OP2_RS2, MEN_X, REN_X, WB_X, CSR_X),
      BGE -> List(BR_BLT, OP1_RS1, OP2_RS2, MEN_X, REN_X, WB_X, CSR_X),
      BGEU -> List(BR_BGE, OP1_RS1, OP2_RS2, MEN_X, REN_X, WB_X, CSR_X),
      BLT -> List(BR_BLTU, OP1_RS1, OP2_RS2, MEN_X, REN_X, WB_X, CSR_X),
      BLTU -> List(BR_BGEU, OP1_RS1, OP2_RS2, MEN_X, REN_X, WB_X, CSR_X),
      JAL -> List(ALU_ADD, OP1_PC, OP2_IMJ, MEN_X, REN_S, WB_PC, CSR_X),
      JALR -> List(ALU_JALR, OP1_RS1, OP2_IMI, MEN_X, REN_S, WB_PC, CSR_X),
      LUI -> List(ALU_ADD, OP1_X, OP2_IMU, MEN_X, REN_S, WB_ALU, CSR_X),
      AUIPC -> List(ALU_ADD, OP1_PC, OP2_IMU, MEN_X, REN_S, WB_ALU, CSR_X),
      CSRRW -> List(ALU_COPY1, OP1_RS1, OP2_X, MEN_X, REN_S, WB_CSR, CSR_W),
      CSRRWI -> List(ALU_COPY1, OP1_IMZ, OP2_X, MEN_X, REN_S, WB_CSR, CSR_W),
      CSRRS -> List(ALU_COPY1, OP1_RS1, OP2_X, MEN_X, REN_S, WB_CSR, CSR_S),
      CSRRSI -> List(ALU_COPY1, OP1_IMZ, OP2_X, MEN_X, REN_S, WB_CSR, CSR_S),
      CSRRC -> List(ALU_COPY1, OP1_RS1, OP2_X, MEN_X, REN_S, WB_CSR, CSR_C),
      CSRRCI -> List(ALU_COPY1, OP1_IMZ, OP2_X, MEN_X, REN_S, WB_CSR, CSR_C),
      
      
    )
  )
  val exe_fun :: op1_sel :: op2_sel :: mem_wen :: rf_wen :: wb_sel :: csr_cmd ::Nil = csignals
  
  val op1_data = MuxCase(0.U(WORD_LEN.W), Seq(
    (op1_sel === OP1_RS1) -> rs1_data,
    (op1_sel === OP1_PC) -> pc_reg,
  )) 
  val op2_data = MuxCase(0.U(WORD_LEN.W), Seq(
    (op2_sel === OP2_RS2) -> rs2_data,
    (op2_sel === OP2_IMI) -> imm_i_sext,
    (op2_sel === OP2_IMS) -> imm_s_sext,
    (op2_sel === OP2_IMJ) -> imm_j_sext,
    (op2_sel === OP2_IMU) -> imm_u_shifted,
  ))
  
  /*================ EX阶段 ==================*/
  
  alu_out := MuxCase(0.U(WORD_LEN.W), Seq(
    (exe_fun === ALU_ADD) -> (op1_data + op2_data),
    (exe_fun === ALU_SUB) -> (op1_data - op2_data),
    (exe_fun === ALU_AND) -> (op1_data & op2_data),
    (exe_fun === ALU_OR) -> (op1_data | op2_data),
    (exe_fun === ALU_XOR) -> (op1_data ^ op2_data),
    (exe_fun === ALU_SLL) -> (op1_data << op2_data(4,0))(31,0),
    (exe_fun === ALU_SRL) -> (op1_data >> op2_data(4,0)),
    (exe_fun === ALU_SRA) -> (op1_data.asSInt >> op2_data(4,0)).asUInt,
    (exe_fun === ALU_SLT) -> (op1_data.asSInt <  op2_data.asSInt).asUInt,
    (exe_fun === ALU_SLTU) -> (op1_data <  op2_data).asUInt,
    (exe_fun === ALU_JALR) -> ((op1_data + op2_data) & ~1.U(WORD_LEN.W)),
    // jmp_flg 在IF阶段声明并使用
  ))
 
  br_flg := MuxCase(false.B, Seq(
    (exe_fun === BR_BEQ) -> (op1_data === op2_data),
    (exe_fun === BR_BNE) -> !(op1_data === op2_data),
    (exe_fun === BR_BLT) -> (op1_data.asSInt < op2_data.asSInt),
    (exe_fun === BR_BGE) -> !(op1_data.asSInt < op2_data.asSInt),
    (exe_fun === BR_BLTU) -> (op1_data < op2_data),
    (exe_fun === BR_BGEU) -> !(op1_data < op2_data),
  ))
  br_target := pc_reg + imm_b_sext

  
 /*================ MEM阶段 =================*/
  io.dmem.addr := alu_out
  // io.dmem.write_enable := (inst === SW) // same, can lookup
  io.dmem.write_enable := mem_wen
  io.dmem.write_data := rs2_data
  /*================ WB阶段 ==================*/
   val csr_wdata = MuxCase(0.U(WORD_LEN.W), Seq(
    (csr_cmd === CSR_W) -> op1_data,
    (csr_cmd === CSR_S) -> (csr_rdata | op1_data),
    (csr_cmd === CSR_C) -> (csr_rdata &  ~op1_data),
    ))
  when(csr_cmd > 0.U) {
    csr_regfile(csr_addr) := csr_wdata
  }
  val wb_data = MuxCase(alu_out, Seq(
    (wb_sel === WB_MEM) -> io.dmem.read_data,
    (wb_sel === WB_PC) -> pc_plus4,
    (wb_sel === WB_CSR) -> csr_rdata,
  ))
  when(rf_wen === REN_S) {  
    regfile(wb_addr) := wb_data
  }

  /* =========测试辅助信号: exit信号==========*/
  io.exit := (inst === 0x00000000.U(WORD_LEN.W)) 
  /* 调试打印 */
  printf(p"pc_reg: 0x${Hexadecimal(pc_reg)}\n")
  printf(p"inst: 0x${Hexadecimal(inst)}\n")
  printf(p"rs1_addr: ${rs1_addr}\n")
  printf(p"rs1_data: 0x${Hexadecimal(rs1_data)}\n")
  printf(p"rs2_addr: ${rs2_addr}\n")
  printf(p"rs2_data: 0x${Hexadecimal(rs2_data)}\n")
  printf(p"wb_addr, 0x${Hexadecimal(wb_addr)}\n")
  printf(p"alu_out: 0x${Hexadecimal(alu_out)}\n")
  printf(p"wb_data: 0x${Hexadecimal(wb_data)}\n")
  printf(p"dmem.wen: 0x${Hexadecimal(io.dmem.write_enable)}\n")
  printf(p"dmem.wdata: 0x${Hexadecimal(io.dmem.write_data)}\n")
  printf(p"imm_b: 0x${Hexadecimal(imm_b)}\n")
  printf(p"imm_b_sext: 0x${Hexadecimal(imm_b_sext)}\n")
  printf(p"imm_j_sext: 0x${Hexadecimal(imm_b_sext)}\n")
  printf(p"branch.flag: ${br_flg}\n")
  printf(p"branch.target: 0x${Hexadecimal(br_target)}\n")
  printf(p"jmp.flag: ${jmp_flg}\n") 
  printf("-----------\n")
}
