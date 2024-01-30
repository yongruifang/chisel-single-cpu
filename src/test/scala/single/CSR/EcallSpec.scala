package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class EcallSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work through ecall.hex" in {
     test(new Top("src/hex/ecall.hex.txt")) {dut => 
       dut.clock.step() 
     }
   }
}
