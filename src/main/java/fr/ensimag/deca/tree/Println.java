package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.bytecode.pseudocode.instructions.invokevirtual;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex  if true, then float should be displayed as hexadecimal
     *                  (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("Println codeGenInst");
        super.codeGenInst(compiler, returnLabel, local);
        compiler.addInstruction(new WNL());
    }

    @Override
    protected void codeGenInstJasmin(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("Println codeGenInst");
        super.codeGenInstJasmin(compiler, returnLabel, local);
        compiler.addInstruction(new invokevirtual());
    }

    @Override
    String getSuffix() {
        return "ln";
    }
}
