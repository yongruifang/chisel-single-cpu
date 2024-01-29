package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class LwSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work through lw.hex" in {
     test(new Top("src/hex/lw.hex.txt")) {dut => 
       dut.clock.step() 
     }
   }
}
