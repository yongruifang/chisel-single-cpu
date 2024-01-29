package single

import chisel3._
import chisel3.util._

import common.Constants._

// 定义取指令的IO接口
class IMemPortIO extends Bundle {
  val addr = Input(UInt(WORD_LEN.W))
  val inst = Output(UInt(WORD_LEN.W))
}
class Memory extends Module {
  val io = IO(new Bundle {
    val imem = new IMemPortIO()
  })
  /*基础设施*/
  val mem = Mem(16384, UInt(8.W))
  /*加载内存*/
  // loadMemoryFromFile(mem, <path>)
  /*取指*/
  io.imem.inst := Cat (
    mem(io.imem.addr + 3.U(WORD_LEN.W)),
    mem(io.imem.addr + 2.U(WORD_LEN.W)),
    mem(io.imem.addr + 1.U(WORD_LEN.W)),
    mem(io.imem.addr)
  )
}
