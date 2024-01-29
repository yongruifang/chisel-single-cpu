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
  /* 取指 */
  pc_reg := pc_reg + 4.U(WORD_LEN.W)
  io.imem.addr := pc_reg
  val inst = io.imem.inst
  /*=============== 译码阶段 ================*/
  val rs1_addr = inst(19, 15)
  val rs2_addr = inst(24, 20)
  val wb_addr = inst(11, 7) // 即rd
  val rs1_data = Mux((rs1_addr=/=0.U(WORD_LEN.W)), 
    regfile(rs1_addr), 0.U(WORD_LEN.W))
  val rs2_data = Mux((rs2_addr=/=0.U(WORD_LEN.W)),
    regfile(rs2_addr), 0.U(WORD_LEN.W))
  val imm_i = inst(31,20)
  val imm_i_sext = Cat(Fill(20, imm_i(11)), imm_i)
  val imm_s = Cat(inst(31,25), inst(11,7))
  val imm_s_sext = Cat(Fill(20, imm_i(11)), imm_i)

  val csignals = ListLookup(
    inst, List(ALU_X, OP1_RS1, OP2_RS2),
    Array(
      LW -> List(ALU_ADD, OP1_RS1, OP2_IMI),
      SW -> List(ALU_ADD, OP1_RS1, OP2_IMS),
      ADD -> List(ALU_ADD, OP1_RS1, OP2_RS2),
      ADDI -> List(ALU_ADD, OP1_RS1, OP2_IMI),
      SUB -> List(ALU_SUB, OP1_RS1, OP2_RS2),
      AND -> List(ALU_AND, OP1_RS1, OP2_RS2),
      OR -> List(ALU_OR, OP1_RS1, OP2_RS2),
      XOR -> List(ALU_XOR, OP1_RS1, OP2_RS2),
      ANDI -> List(ALU_AND, OP1_RS1, OP2_IMS),
      ORI -> List(ALU_OR, OP1_RS1, OP2_IMS),
      XORI -> List(ALU_XOR, OP1_RS1, OP2_IMS),
    )
  )
  val exe_fun :: op1_sel :: op2_sel :: Nil = csignals
  
  val op1_data = MuxCase(0.U(WORD_LEN.W), Seq(
    (op1_sel === OP1_RS1) -> rs1_data
  ))
  val op2_data = MuxCase(0.U(WORD_LEN.W), Seq(
    (op2_sel === OP2_RS2) -> rs2_data,
    (op2_sel === OP2_IMI) -> imm_i_sext,
    (op2_sel === OP2_IMS) -> imm_s_sext
  ))
  
  /*================ EX阶段 ==================*/
  
  val alu_out = MuxCase(0.U(WORD_LEN.W), Seq(
    (exe_fun === ALU_ADD) -> (op1_data + op2_data),
    (exe_fun === ALU_SUB) -> (op1_data - op2_data),
    (exe_fun === ALU_AND) -> (op1_data & op2_data),
    (exe_fun === ALU_OR) -> (op1_data | op2_data),
    (exe_fun === ALU_XOR) -> (op1_data ^ op2_data),
  ))
 /*================ MEM阶段 =================*/
  io.dmem.addr := alu_out
  io.dmem.write_enable := (inst === SW) // same, can lookup
  io.dmem.write_data := rs2_data
  /*================ WB阶段 ==================*/
  val wb_data = MuxCase(alu_out, Seq(
    (inst === LW) -> io.dmem.read_data
  ))
  when(inst === LW || inst === ADD || inst === ADDI || inst === SUB
    || inst === AND || inst === OR || inst === XOR || inst === ANDI
    || inst === ORI || inst === XORI) {
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
  printf("-----------\n")
}
