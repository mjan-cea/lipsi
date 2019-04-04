/*
 * Copyright: 2017, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Test Lipsi.
 */

package lipsi


import chisel3._
import chisel3.iotesters.PeekPokeTester

class LipsiTester(dut: Lipsi) extends PeekPokeTester(dut) {

  var run = true
  var maxInstructions = 30
  while(run) {
    peek(dut.io.dbg.pc)
    peek(dut.io.dbg.accu)
    peek(dut.io.dbg.exit)    
    // peek(dut.mem.io.rdAddr)
    // peek(dut.stateReg) possible in Chisel 2
    step(1)
    maxInstructions -= 1
    run = peek(dut.io.dbg.exit) == 0 && maxInstructions > 0
    // poke(dut.io.din, maxInstructions)
  }
  expect(dut.io.dbg.accu, 0, "Accu shall be zero at the end of a test case.\n")

}

object LipsiTester {
  def main(args: Array[String]): Unit = {
    println("Testing Lipsi")
    iotesters.Driver.execute(Array[String]("--fint-write-vcd"/*"--backend-name", "verilator"*/), () => new Lipsi(args(0))) {
      c => new LipsiTester(c)
    }
      /* Chisel 2
    chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
      "--compile", "--vcd", "--targetDir", "generated"),
      () => Module(new Lipsi(args(0)))) {
        f => new LipsiTester(f)
      }
      */
  }
}
