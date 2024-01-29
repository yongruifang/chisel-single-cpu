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
  
  /*================ EX阶段 ==================*/
  val alu_out = MuxCase(0.U(WORD_LEN.W), Seq(
    (inst === LW) -> (rs1_data + imm_i_sext),
    (inst === SW) -> (rs1_data + imm_s_sext)
  ))
  /*================ MEM阶段 =================*/
  io.dmem.addr := alu_out
  io.dmem.write_enable := (inst === SW)
  io.dmem.write_data := rs2_data
  /*================ WB阶段 ==================*/
  val wb_data = io.dmem.read_data
  when(inst === LW) {
    regfile(wb_addr) := wb_data
  }
  /* exit信号*/
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
