package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class LogicSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in logic" in {
     test(new Top("src/hex/logic.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
   }
}
