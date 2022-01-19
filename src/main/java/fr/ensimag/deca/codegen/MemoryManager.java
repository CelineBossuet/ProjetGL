package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;


/**
 * Fichier permetant de gérer la mémoire en gardant les resgistres utilisés
 * @author gl13
 * @date 09/01/2022
 * */
public class MemoryManager {
    private int currentLB=0; //local base current register
    private int currentGB=0;
    private int maxLB;

    public int getCurrentGB() {return currentGB;}

    public int getMaxLB() {return maxLB;}

    public int getCurrentLB() {return currentLB;}

    public RegisterOffset getCurrentGBOperand(){
        return new RegisterOffset(currentGB, Register.GB);
    }


    public void initLGB(){
        currentLB=0;
        maxLB=0;
    }

    public RegisterOffset allocLB(int size){
        currentLB+=size;
        if (maxLB<currentLB){
            maxLB=currentLB;
        }
        return new RegisterOffset(currentGB, Register.LB);
    }

    public RegisterOffset allocGB(int size){
        currentGB+=size;
        return new RegisterOffset(currentGB, Register.GB);
    }

    public  void deallocLB(int size){
        currentLB-=size;
    }

    public void allocBSR(){
        if(currentLB +2 > maxLB){
            maxLB=currentLB+2;
        }
    }

    public RegisterOffset createConstant(DVal value, IMAProgram program){
        Validate.notNull(value!=null);
        DAddr add = allocGB(1);
        program.addInstruction(new LOAD(value, Register.getR(0)));
        program.addInstruction(new STORE(Register.getR(0), add));
        return new RegisterOffset(currentGB, Register.GB);
    }

}
