/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.ligandanalysis;

import java.io.IOException;
import java.sql.SQLException;
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
        this.ProteinID = ProteinID;
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
                                        if (!(g.getType().equals("amino"))) {
                                            continue;
                                        }
                                        //Get the data
                                        LigandResult lg = new LigandResult();
                                        lg.setHemeGroup(HEM);
                                        lg.setResidueGroup(g);
                                        lg.setHemeAtom(HEMAtom);
                                        lg.setResidueAtom(a);
                                        lg.setChainID(g.getChainId());
                                        lg.setProteinID(ProteinID);
                                        System.out.println(ProteinID);
                                        //Update 
                                        String SQLStatement = "INSERT INTO liganddata (PDBID, ChainID, ResidueNumber, ResidueName, ResidueBindingAtom, Distance, HemeGroupNumber,HemeGroupResidueID)"
                                                + "values(" + "'" + lg.getProteinID() + "', '" + lg.getChainID() + "', '" + lg.getResidueGroup().getResidueNumber().getSeqNum().toString() +"', '"+ lg.getResidueGroup().getPDBName()+"', '" +lg.getResidueAtom().getName().toString()+"', "+ distance + ", " + lg.getHemeGroup().getResidueNumber().getSeqNum() + ", '" + lg.getHemeAtom().getName() + "')";
                                        try {
                                            System.out.println(SQLStatement);
                                            MysqlDBService.UpdateDB(SQLStatement);
                                        } catch (SQLException ex) {
                                            Logger.getLogger(ProteinWorkerThread.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(ProteinWorkerThread.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        //App.LgLigandResults.add(lg);
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

    public Structure getStructure(String ProteinID) throws IOException, StructureException {
        String pdbLocation = "D:\\pdb";
        AtomCache cache = new AtomCache();
        cache.setPath(pdbLocation);
        StructureIO.setAtomCache(cache);
        Structure structure = StructureIO.getStructure(ProteinID);
        // and let's print out how many atoms are in this structure
        return structure;
    }

    public LinkedList<Group> getAllHEM(Structure structure) {
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
