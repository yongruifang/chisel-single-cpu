package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class FetchSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work through fetch.hex" in {
      test(new Top("src/hex/fetch.hex.txt")) {dut => 
        /*
        dut.clock.step()
        println("exit: ", dut.io.exit.peek())
        println("exit: ", dut.io.exit.peek().litToBoolean)
        dut.clock.step()
        println("exit: ", dut.io.exit.peek().litToBoolean)
        dut.clock.step()
        println("exit: ", dut.io.exit.peek().litToBoolean)
        dut.clock.step()
        println("exit: ", dut.io.exit.peek().litToBoolean)
        */
        while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
        }
      }
   } 
}  
