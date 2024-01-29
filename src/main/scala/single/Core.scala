package single

import chisel3._
import chisel3.util._

import common.Constants._

class Core extends Module{
  val io = IO(new Bundle{
    val imem = Flipped(new IMemPortIO())
  })
  /* 基础设施 */
  val regfile = Mem(32, UInt(WORD_LEN.W))
  val pc_reg = RegInit(START_ADDR)
  /* 取指 */
  pc_reg := pc_reg + 4.U
  io.imem.addr := pc_reg
  val inst = io.imem.inst
  /* 调试打印 */
  printf(p"pc_reg: ${pc_reg}\n")
  printf(p"inst: ${inst}\n")
  printf("-----------\n")
}
