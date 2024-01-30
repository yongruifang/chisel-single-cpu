package common

import chisel3._
import chisel3.util._

object InstPattern {
  val LW = BitPat("b?????????????????010?????0000011")
  val SW = BitPat("b?????????????????010?????0100011")
  val ADD = BitPat("b0000000??????????000?????0110011")
  val SUB = BitPat("b0100000??????????000?????0110011")
  val ADDI = BitPat("b?????????????????000?????0010011")
  val AND = BitPat("b0000000??????????111?????0110011")
  val OR = BitPat("b0000000??????????110?????0110011")
  val XOR = BitPat("b0000000??????????100?????0110011")
  val ANDI = BitPat("b?????????????????111?????0010011")
  val ORI = BitPat("b?????????????????110?????0010011")
  val XORI = BitPat("b?????????????????100?????0010011")
  val SLL = BitPat("b0000000??????????001?????0110011")
  val SRL = BitPat("b0000000??????????101?????0110011")
  val SRA = BitPat("b0100000??????????101?????0110011")
  val SLLI = BitPat("b0000000??????????001?????0010011")
  val SRLI = BitPat("b0000000??????????101?????0010011")
  val SRAI = BitPat("b0100000??????????101?????0010011")
  val SLT = BitPat("b0000000??????????010?????0110011")
  val SLTU = BitPat("b0000000??????????011?????0110011")
  val SLTI = BitPat("b?????????????????010?????0010011")
  val SLTIU = BitPat("b?????????????????010?????0110011")
}
