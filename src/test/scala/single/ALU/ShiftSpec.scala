package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class ShiftSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in shift" in {
     test(new Top("src/hex/shift.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
   }
}
