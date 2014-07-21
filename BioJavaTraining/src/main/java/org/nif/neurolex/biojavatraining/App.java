/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nif.neurolex.biojavatraining;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.ChainImpl;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.SVDSuperimposer;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.StructureImpl;
import org.biojava.bio.structure.StructureTools;
import org.biojava.bio.structure.align.gui.jmol.StructureAlignmentJmol;
import org.biojava.bio.structure.jama.Matrix;
import org.biojava3.structure.StructureIO;

/**
 *
 * @author Zaid
 */
public class App {

    public static void main(String args[]) {
        try {
            StructureAlignmentJmol jmolPanel = new StructureAlignmentJmol();
            Structure structure1 = getStructure("4MU8");
            Structure structure2 = getStructure("4LPI");
            Structure structure3 = getStructure("4LPI");
            
            Group g1=getHEM(structure1);
            Group g2=getHEM(structure2);
            List<Atom> atom1=g1.getAtoms();
            List<Atom> atom2=g2.getAtoms();
            System.out.println(atom1.size());
            System.out.println(atom2.size());
            Atom[] atomArray1=atom1.toArray(new Atom[atom1.size()]);
            Atom[] atomArray2=atom2.toArray(new Atom[atom2.size()]);
            SVDSuperimposer svds = new SVDSuperimposer(atomArray1,atomArray2);
            Matrix rotMatrix = svds.getRotation();
            Atom tranMatrix = svds.getTranslation();
            Calc.shift(structure2, tranMatrix);
            Calc.rotate(structure2, rotMatrix);
            //FileOutputStream out= new FileOutputStream("D:\\output.pdb");
            //PrintStream p =  new PrintStream( out );
            //p.println(structure2.toPDB());
            //p.close();
            
            Structure newstruc = new StructureImpl();
            Chain c1 = new ChainImpl();
            c1.setName("A");
            c1.addGroup(g1);
            newstruc.addChain(c1);
            
            Chain c2 = new ChainImpl();
            c2.setName("B");
            c2.addGroup(g2);
            newstruc.addChain(c2);
            
            Group g3=getHEM(structure3);
            Chain c3 = new ChainImpl();
            c3.setName("C");
            c3.addGroup(g3);
            newstruc.addChain(c3);
            //p.println(newstruc.toPDB());
            //p.close();
            
            jmolPanel.setStructure(newstruc);
            jmolPanel.evalString("select * ; color chain;");            
            jmolPanel.evalString("select *; spacefill off; wireframe off; cartoon on;  ");
            jmolPanel.evalString("select ligands; cartoon off; wireframe 0.3; spacefill 0.5; color cpk;");
                // and let's print out how many atoms are in this structure
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Structure getStructure(String ProteinID) throws IOException, StructureException {
        Structure structure = StructureIO.getStructure(ProteinID);
        // and let's print out how many atoms are in this structure
        return structure;
    }

    public static Group getHEM(Structure structure) {
        List<Chain> chains = structure.getChains();
        for (Chain c : chains) {
            for (Group g : c.getAtomGroups()) {
                if (g.getPDBName().equalsIgnoreCase("HEM")) {
//                    System.out.println(c.getName());
//                    System.out.println(g.getChainId());
                    return g;
                }
            }

        }
        return null;

    }
}
