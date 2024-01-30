package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class LISpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in lui" in {
     test(new Top("src/hex/lui.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }
   "mycpu should work in auipc" in { 
     test(new Top("src/hex/auipc.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }

   }
}
