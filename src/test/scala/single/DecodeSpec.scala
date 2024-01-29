package single 
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._

class DecodeSpec extends AnyFreeSpec with ChiselScalatestTester {
   "mycpu should work through decode.hex" in {
     test(new Top("src/hex/decode.hex.txt")) {dut => 
        
     }
   }
}
