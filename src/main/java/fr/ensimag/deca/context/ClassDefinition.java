package fr.ensimag.deca.context;

import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class ClassDefinition extends TypeDefinition {
    private Label constructorLabel;
    private VTable vTable;
    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    private final Environment<ExpDefinition> members;
    private final ClassDefinition superClass;

    public VTable getvTable() {
        return vTable;
    }

    public void setvTable(VTable vTable) {
        this.vTable = vTable;
    }

    public Label getConstructorLabel() {
        return constructorLabel;
    }

    public void setConstructorLabel(Label l){
        this.constructorLabel=l;
    }

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }

    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }



    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }



    public Environment<ExpDefinition> getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        Environment<ExpDefinition> parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new Environment<ExpDefinition>(parent);
        this.superClass = superClass;
    }

}
