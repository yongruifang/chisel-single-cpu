package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class AddSubSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in add-sub" in {
     test(new Top("src/hex/add_sub.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
   }
}
