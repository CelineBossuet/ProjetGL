package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import org.apache.commons.lang.Validate;

import static fr.ensimag.ima.pseudocode.Register.getR;


public class RegisterManager {
    private int current;
    private int max;
    private int lastUsed=-1;

    public void initRegister(){
        this.lastUsed=1;
    }

    public int getLastUsed(){
        return this.lastUsed;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public GPRegister getCurrent() {return getR(current);}


    /**
     * Allocate one register, have to make sure that getCurrent() won't
     * return it until it has been freed.
     *
     * @return The register allocated. This register should not be used anymore
     *         until it is freed.
     */
    public GPRegister allocRegister() throws Exception {
        Validate.isTrue(current<=max); //pas de registre libre
        Validate.isTrue(lastUsed>0); //pas encore appelé la fonction initRegistre()

        GPRegister reg = getR(current);
        current++;
        if(this.lastUsed<this.current){
            this.lastUsed=this.current;
        }
        return reg;


    }


    /**
     * Deallocate the register passed as argument.
     *
     * allocate/release should be called in stack order.
     *
     * @param r Register to free
     */
    public void releaseRegister(GPRegister r){
        Validate.isTrue(lastUsed==-1); //pas initialisé le registre
        int i = r.getNumber();
        Validate.isTrue(i==current-1); //ce n'est pas le dernier registre appelé
        current--;
        if (current<0){
            throw new DecacInternalError("voulu déalloc trop de registre");
        }
    }
}
