package single

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFileInline

import common.Constants._

// 定义取指令的IO接口
class IMemPortIO extends Bundle {
  val addr = Input(UInt(WORD_LEN.W))
  val inst = Output(UInt(WORD_LEN.W))
}
class DMemPortIO extends Bundle {
  val addr = Input(UInt(WORD_LEN.W))
  val read_data = Output(UInt(WORD_LEN.W))
  val write_enable = Input(Bool())
  val write_data = Input(UInt(WORD_LEN.W))
}
class Memory(memoryFile: String = "") extends Module {
  val io = IO(new Bundle {
    val imem = new IMemPortIO()
    val dmem = new DMemPortIO()
  })
  /*基础设施*/
  val mem = Mem(16384, UInt(8.W))
  /*加载内存*/
  // loadMemoryFromFile(mem, <path>)
  loadMemoryFromFileInline(mem, memoryFile)
  /*取指*/
  io.imem.inst := Cat(
    mem(io.imem.addr + 3.U(WORD_LEN.W)),
    mem(io.imem.addr + 2.U(WORD_LEN.W)),
    mem(io.imem.addr + 1.U(WORD_LEN.W)),
    mem(io.imem.addr)
  )
  /*LW*/
  io.dmem.read_data := Cat(
    mem(io.dmem.addr + 3.U(WORD_LEN.W)),
    mem(io.dmem.addr + 2.U(WORD_LEN.W)),
    mem(io.dmem.addr + 1.U(WORD_LEN.W)),
    mem(io.dmem.addr)
  )
  when(io.dmem.write_enable) {
    mem(io.dmem.addr) := io.dmem.write_data(7, 0)
    mem(io.dmem.addr + 1.U) := io.dmem.write_data(15, 8)
    mem(io.dmem.addr + 2.U) := io.dmem.write_data(23, 16)
    mem(io.dmem.addr + 3.U) := io.dmem.write_data(31, 24)
  }
}
