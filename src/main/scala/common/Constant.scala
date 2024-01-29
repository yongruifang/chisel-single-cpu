package common

import chisel3._

object Constants {
  val WORD_LEN = 32
  val START_ADDR = 0.U

  val EXE_FUN_LEN = 5
  val ALU_X = 0.U(EXE_FUN_LEN.W)
  val ALU_ADD = 1.U(EXE_FUN_LEN.W)
  val ALU_SUB = 2.U(EXE_FUN_LEN.W)
  val ALU_AND = 3.U(EXE_FUN_LEN.W)
  val ALU_OR = 4.U(EXE_FUN_LEN.W)
  val ALU_XOR = 5.U(EXE_FUN_LEN.W)

  val OP1_LEN = 2
  val OP1_RS1 = 0.U(OP1_LEN.W)
  
  val OP2_LEN = 3
  val OP2_X = 0.U(OP2_LEN.W)
  val OP2_RS2 = 1.U(OP2_LEN.W)
  val OP2_IMI = 2.U(OP2_LEN.W)
  val OP2_IMS = 3.U(OP2_LEN.W)
}

