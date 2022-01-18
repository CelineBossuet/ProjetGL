package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.IMAProgram;

public class EqualsDefinition extends MethodDefinition {
    IMAProgram body;
    /**
     * @param type      Return type of the method
     * @param location  Location of the declaration of the method
     * @param signature List of arguments of the method
     * @param index     Index of the method in the class. Starts from 0.
     */
    public EqualsDefinition(Type type, Location location, Signature signature, int index, IMAProgram body) {
        super(type, location, signature, index);
        this.body=body;
    }

    public void codeGen(IMAProgram prog){
        prog.addLabel(getLabel());
        prog.append(body);
    }
}
