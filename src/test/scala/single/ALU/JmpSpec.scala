package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class JmpSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in jal" in {
     test(new Top("src/hex/jal.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }
   "mycpu should work in jalr" in { 
     test(new Top("src/hex/jalr.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }

   }
}
