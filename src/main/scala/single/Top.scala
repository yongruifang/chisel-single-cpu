package single

import chisel3._
import chisel3.util._

class Top(memoryFile: String = "") extends Module {
  val io = IO(new Bundle {
    val exit = Output(Bool())
  })
  val core = Module(new Core())
  val memory = Module(new Memory(memoryFile))
  // 连线
  core.io.imem <> memory.io.imem
  io.exit := core.io.exit
}
