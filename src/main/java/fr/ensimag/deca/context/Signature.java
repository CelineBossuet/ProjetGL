package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    public void verifySameSignature(Signature signature, Location loc) throws ContextualError{
        if(this.args.size()!=signature.args.size()){
            throw new ContextualError("Methods have different number of arguments", loc);
        }
        for (int i=0; i<size(); i++){
            if(!paramNumber(i).sameType(signature.paramNumber(i))){
                throw new ContextualError("Methods have "+paramNumber(i)+" and "
                        +signature.paramNumber(i)+" types for the same paramater", loc);
            }
        }
    }

}
