package single

import chisel3._
import chisel3.util._

import common.Constants._

class Core extends Module{
  val io = IO(new Bundle{
    val imem = Flipped(new IMemPortIO())
    val exit = Output(Bool())
  })
  /* 基础设施 */
  val regfile = Mem(32, UInt(WORD_LEN.W))
  val pc_reg = RegInit(START_ADDR)
  /* 取指 */
  pc_reg := pc_reg + 4.U(WORD_LEN.W)
  io.imem.addr := pc_reg
  val inst = io.imem.inst
  /* 译码 */
  val rs1_addr = inst(19, 15)
  val rs2_addr = inst(24, 20)
  val wb_addr = inst(11, 7)
  val rs1_data = Mux((rs1_addr=/=0.U(WORD_LEN.W)), 
    regfile(rs1_addr), 0.U(WORD_LEN.W))
  val rs2_data = Mux((rs2_addr=/=0.U(WORD_LEN.W)),
    regfile(rs2_addr), 0.U(WORD_LEN.W))
  /* exit信号*/
  io.exit := (inst === 0x00000000.U(WORD_LEN.W)) 
  /* 调试打印 */
  printf(p"pc_reg: 0x${Hexadecimal(pc_reg)}\n")
  printf(p"inst: 0x${Hexadecimal(inst)}\n")
  printf(p"rs1_addr: ${rs1_addr}\n")
  printf(p"rs1_data: 0x${Hexadecimal(rs1_data)}\n")
  printf(p"rs2_addr: ${rs2_addr}\n")
  printf(p"rs2_data: 0x${Hexadecimal(rs2_data)}\n")
  printf("-----------\n")
}
