package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.ima.pseudocode.DAddr;

public class VarID extends DAddr {

    private int id;

    public VarID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
