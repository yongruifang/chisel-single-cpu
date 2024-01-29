package single

import chisel3._
import chisel3.util._

class Top extends Module {
  val core = Module(new Core())
  val memory = Module(new Memory())
  // 连线
  core.io.imem <> memory.io.imem
}
