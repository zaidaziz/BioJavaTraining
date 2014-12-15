package org.nif.neurolex.biojavatraining;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Group;
import java.io.Serializable;
public class LigandResult implements Serializable{

    private Group HemeGroup;
    private Group ResidueGroup;
    private Atom ResidueAtom;
    private Atom HemeAtom;
    private String ProteinID;
    public void setHemeGroup(Group HemeGroup) {
        this.HemeGroup = HemeGroup;
    }
    public Group getHemeGroup(){
        return this.HemeGroup;
    }
    public void setResidueGroup(Group ResidueGroup) {
        this.ResidueGroup = ResidueGroup;
    }
    public Group getResidueGroup(){
        return this.ResidueGroup;
    }
    
    public void setResidueAtom(Atom ResidueAtom) {
        this.HemeGroup = HemeGroup;
    }
    public Atom getResidueAtom(){
        return this.ResidueAtom;
    }
    
    public void setHemeAtom(Atom HemeAtom) {
        this.HemeAtom = HemeAtom;
    }
    public Atom getHemeAtom(){
        return this.HemeAtom;
    }
    
    public void setProteinID(String ProteinID) {
        this.ProteinID = ProteinID;
    }
    public String getProteinID(){
        return this.ProteinID;
    }
    
}
