/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nif.neurolex.biojavatraining;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava3.structure.StructureIO;
import org.biojava.bio.structure.align.util.AtomCache;



/**
 *
 * @author Zaid
 */
public class ProteinWorkerThread implements Runnable {

    String ProteinID;

    public ProteinWorkerThread(String ProteinID) {
        this.ProteinID=ProteinID;
    }

    @Override
    public void run() {
        String pdbLocation = "D:\\pdb";
        AtomCache cache = new AtomCache();
        cache.setPath(pdbLocation);
        StructureIO.setAtomCache(cache);
        try {
            Structure st = getStructure(ProteinID);
            LinkedList<Group> HEMs = getAllHEM(st);
            for (int i = 0; i < HEMs.size(); i++) {
                Group HEM = HEMs.get(i);
                //get HEM atoms
                List<Atom> HEMAtoms = HEM.getAtoms();
                for (Atom HEMAtom : HEMAtoms) {
                    //Iterate through chains
                    for (Chain c : st.getChains()) {
                        //Iterate through groups
                        for (Group g : c.getAtomGroups()) {
                            //If group is HEM ignore
                            if (!(g.getPDBName().equalsIgnoreCase("HEM"))) {
                                for (Atom a : g.getAtoms()) {
                                    double distance = Calc.getDistance(a, HEMAtom);
                                    if (distance < 4) {
                                    if(!(g.getType().equals("amino"))){
                                        continue;
                                    }   
                                        ///////get elements names
//                                        inside = new Hashtable<String, Integer>();
//                                        System.out.println(g.getPDBName());
//                                        //Check if the HEM atom is in the 
//                                        if (outside.containsKey(HEMAtom.getFullName())) {
//                                            inside = outside.get(HEMAtom.getFullName());
//                                            //Check if the element being bonded to.
//                                            if (inside.containsKey(a.getElement().toString())) {
//                                                inside.put(a.getElement().toString(), inside.get(a.getElement().toString()) + 1);
//                                            } else {
//                                                inside.put(a.getElement().toString(), 1);
//                                                outside.put(HEMAtom.getFullName(), inside);
//                                            }
//
//                                        } else {
//                                            inside = new Hashtable<String, Integer>();
//                                            inside.put(a.getElement().toString(), 1);
//                                            outside.put(HEMAtom.getFullName(), inside);
//                                        }
//                                        ///////get group name
//                                        inside = new Hashtable<String, Integer>();
//                                        System.out.println(g.getPDBName());
//                                        ///Check if the HEM atom is in the 
//                                        if (outside.containsKey(HEMAtom.getElement().toString())) {
//                                            inside = outside.get(HEMAtom.getElement().toString());
//                                            //Check if the element being bonded to.
//                                            if (inside.containsKey(g.getPDBName())) {
//                                                inside.put(g.getPDBName().toString(), inside.get(g.getPDBName()) + 1);
//                                            } else {
//                                                inside.put(g.getPDBName(), 1);
//                                                outside.put(HEMAtom.getElement().toString(), inside);
//                                            }
//
//                                        } else {
//                                            inside = new Hashtable<String, Integer>();
//                                            inside.put(g.getPDBName(), 1);
//                                            outside.put(HEMAtom.getElement().toString(), inside);
//                                        }
//                                        /////////*
                                        LigandResult lg =new LigandResult();
                                        lg.setHemeGroup(HEM);
                                        lg.setResidueGroup(g);
                                        lg.setHemeAtom(HEMAtom);
                                        lg.setResidueAtom(a);
                                        System.out.println(ProteinID);
//                                        System.out.println(HEMAtom.getElement());
//                                        System.out.println(g.getPDBName());
                                        App.LgLigandResults.add(lg);
                                        System.out.println(App.LgLigandResults.size());
                                    }
                                }
                            }
                        }
                    }

                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (StructureException ex) {
            ex.printStackTrace();
        }
    }

    public  Structure getStructure(String ProteinID) throws IOException, StructureException {
        String pdbLocation = "D:\\pdb";
        AtomCache cache = new AtomCache();
        cache.setPath(pdbLocation);
        StructureIO.setAtomCache(cache);
        Structure structure = StructureIO.getStructure(ProteinID);
        // and let's print out how many atoms are in this structure
        return structure;
    }
        public  LinkedList<Group> getAllHEM(Structure structure) {
        LinkedList<Group> HEMs = new LinkedList<>();
        List<Chain> chains = structure.getChains();
        for (Chain c : chains) {
            for (Group g : c.getAtomGroups()) {
                if (g.getPDBName().equalsIgnoreCase("HEM")) {
                    HEMs.add(g);
                }
            }

        }
        return HEMs;

    }
}
