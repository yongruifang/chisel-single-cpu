package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class CProgramTest extends AnyFreeSpec with ChiselScalatestTester {
   "should pass gcd.hex" in {
      test(new Top("src/cprogram/gcd.hex.txt")) {dut =>  
        while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
        }
        // dut.io.gp.expect(1.U)
      }
   } 
}  
