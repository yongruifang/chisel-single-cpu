package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class CSRSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work in csrrc" in {
     test(new Top("src/hex/csrrc.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }
   "mycpu should work in csrrci" in {
     test(new Top("src/hex/csrrci.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }

   "mycpu should work in csrrs" in {
     test(new Top("src/hex/csrrs.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }

   "mycpu should work in csrrsi" in {
     test(new Top("src/hex/csrrsi.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }

   "mycpu should work in csrrw" in {
     test(new Top("src/hex/csrrw.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }

   "mycpu should work in csrrwi" in {
     test(new Top("src/hex/csrrwi.hex.txt")) {dut => 
       while(!dut.io.exit.peek().litToBoolean){
          dut.clock.step()
       } 
     }
  }


}
