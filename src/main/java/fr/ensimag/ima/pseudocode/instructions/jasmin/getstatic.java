package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.jasmin.BinaryInstructionJasmin;
import fr.ensimag.ima.pseudocode.jasmin.PrintStreamOp;
import fr.ensimag.ima.pseudocode.jasmin.SystemOut;

public class getstatic extends BinaryInstructionJasmin {

  public getstatic(SystemOut systemOut, PrintStreamOp printStream) { // TODO A FAIRE generalize instruction to not only
    // print call
    super(systemOut, printStream);
  }
}
