package pdb.ligandanalysis;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Group;
import java.io.Serializable;
public class LigandResult implements Serializable{

    private Group HemeGroup;
    private Group ResidueGroup;
    private Atom ResidueAtom;
    private Atom HemeAtom;
    private String ProteinID;
    private String ChainID;
    
    public void setChainID(String ChainID) {
        this.ChainID = ChainID;
    }
    public String getChainID(){
        return this.ChainID;
    }
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
        this.ResidueAtom = ResidueAtom;
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
