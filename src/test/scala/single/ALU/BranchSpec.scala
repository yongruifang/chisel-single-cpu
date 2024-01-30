package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class BranchSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in branch" in {
     test(new Top("src/hex/branch.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
   }
}
