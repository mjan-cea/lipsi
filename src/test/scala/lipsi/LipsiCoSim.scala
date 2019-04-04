/*
 * Copyright: 2017, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Test Lipsi.
 */

package lipsi

import lipsi.sim._
import chisel3._
import chisel3.iotesters.PeekPokeTester

class LipsiCoSim(dut: Lipsi, arg0: String) extends PeekPokeTester(dut) {

  val lsim = new LipsiSim(arg0)

  var run = true
  var maxInstructions = 30
  while(run) {

    println(s"PC Hard=${peek(dut.io.dbg.pc)}, PC soft=${lsim.pc}");
    println(s"Accu Hard=${peek(dut.io.dbg.accu)}, Accu soft=${lsim.accu}");
    println(s"Exit Hard=${peek(dut.io.dbg.exit)}");

    println(s"---------------------------------");
    println(s"");
    println(s"  accuReg=${peek(dut.io.dbg.accuReg)}");
    if (peek(dut.io.dbg.accuZero) == 0)
       println(s"  accuZero=FALSE"); else println(s"  accuZero=TRUE");
    println(s"  enaAccuReg=${peek(dut.io.dbg.enaAccuReg)}");
    if (peek(dut.io.dbg.enaIoReg) == 0)
       println(s"  enaIoReg=FALSE"); else println(s"  enaIoReg=TRUE");
    if (peek(dut.io.dbg.enaPcReg) == 0)
       println(s"  enaPcReg=FALSE"); else println(s"  enaPcReg=TRUE");
    println(s"  exitReg=${peek(dut.io.dbg.exitReg)}");
    println(s"  funcReg=${peek(dut.io.dbg.funcReg)}");
    println(s"  nextPC=${peek(dut.io.dbg.nextPC)}");
    println(s"  op=${peek(dut.io.dbg.op)}");
    println(s"  outReg=${peek(dut.io.dbg.outReg)}");
    println(s"  pcReg=${peek(dut.io.dbg.pcReg)}");
    println(f"  rdAddr=${peek(dut.io.dbg.rdAddr)}%x");
    println(s"  rdData=${peek(dut.io.dbg.rdData)}");
    println(s"  res=${peek(dut.io.dbg.res)}");
    println(s"  stateReg=${peek(dut.io.dbg.stateReg)}");
    println(s"  updPC=${peek(dut.io.dbg.updPC)}");
    println(s"  wrAddr=${peek(dut.io.dbg.wrAddr)}");
    println(s"");
    println(s"---------------------------------");

    expect(dut.io.dbg.pc, lsim.pc, "PC shall be equal.\n")
    expect(dut.io.dbg.accu, lsim.accu, "Accu shall be equal.\n")

    step(1)
    lsim.step()
    maxInstructions -= 1
    run = peek(dut.io.dbg.exit) == 0 && maxInstructions > 0
  }
  expect(dut.io.dbg.accu, 0, "Accu shall be zero at the end of a test case.\n")
}

object LipsiCoSim {
  def main(args: Array[String]): Unit = {
    println("Co-simulation of Lipsi")
    iotesters.Driver.execute(Array[String]("fint-vcd-show-underscored-vars", "--fint-write-vcd"), () => new Lipsi(args(0))) {
      c => new LipsiCoSim(c, args(0))
    }

    /* Chisel 2
    chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
      "--compile", "--vcd", "--targetDir", "generated"),
      () => Module(new Lipsi(args(0)))) {
        f => new LipsiCoSim(f, args(0))
      }
      */
  }
}
