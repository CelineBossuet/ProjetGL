package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;


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

    public void setCurrentGB(int currentGB) {this.currentGB = currentGB;}

    public void setCurrentLB(int currentLB) {this.currentLB = currentLB;}

    public void setMaxLB(int maxLB) {this.maxLB = maxLB;}

    public void initLGB(){
        currentGB=0;
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

}
