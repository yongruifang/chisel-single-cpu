package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class LessThanSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in less_than" in {
     test(new Top("src/hex/less_than.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
   }
}
