package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class FetchSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work through fetch.hex" in {
      test(new Top()) {dut => 
        dut.clock.step()
      }
   } 
}  
