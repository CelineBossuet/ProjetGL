package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import org.apache.commons.lang.Validate;

import static fr.ensimag.ima.pseudocode.Register.getR;

/**
 * Fichier permetant de gérer les Registres pour pouvoir les alloc et avoir le current
 * @author gl13
 * @date 10/01/2022
 */
public class RegisterManager {
    private int current=2;
    private int max=15;
    private int lastUsed=-1;

    public void initRegister(){
        assert(current ==2);
        //peut pas reinitialiser si des regitres sont déjà alloués
        this.lastUsed=1;
    }

    public int getLastUsed(){
        return this.lastUsed;
    }

    public int getMax() {
        return max;
    }

    public int getCurrentv(){return current;}

    public GPRegister getCurrent() {return getR(current);}


    /**
     * Alloc un registre tout en prenant en compte qu'il fait que getCurrent()
     * ne puisse pas le return tant qu'il n'a pas été free
     *
     * @return reg le registre allocated. A ne pas utiliser par qqun d'autre tant qu'il n'a pas été free
     */
    public GPRegister allocRegister() {
        Validate.isTrue(current<=max); //pas de registre libre
        //Validate.isTrue(lastUsed>0); //pas encore appelé la fonction initRegistre()

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
        //Validate.isTrue(lastUsed==-1); //pas initialisé le registre
        int i = r.getNumber();
        Validate.isTrue(i==current-1); //ce n'est pas le dernier registre appelé
        current--;
        if (current<0){
            throw new DecacInternalError("voulu déalloc trop de registre");
        }
    }
}
