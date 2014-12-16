/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.ligandanalysis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.ChainImpl;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.PDBStatus;
import org.biojava.bio.structure.SVDSuperimposer;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.StructureImpl;
import org.biojava.bio.structure.StructureTools;
import org.biojava.bio.structure.align.gui.jmol.StructureAlignmentJmol;
import org.biojava.bio.structure.align.util.AtomCache;
import org.biojava.bio.structure.io.FileParsingParameters;
import org.biojava.bio.structure.jama.Matrix;
import org.biojava3.structure.StructureIO;

/**
 *
 * @author Zaid
 */
public class App {
    public static LinkedList<LigandResult> LgLigandResults = new LinkedList<>();
    public static void HEMStaticReader()throws IOException, StructureException, ClassNotFoundException{
        InputStream file = new FileInputStream("D:\\BioJava\\serialized\\pdbout200000.ser");
        ObjectInput input= new ObjectInputStream(file);
        LinkedList<LigandResult> DeserializedList;
        DeserializedList = (LinkedList<LigandResult>)input.readObject();
        for(int i=1;i<500;i++){
            LigandResult lg=DeserializedList.get(i);
            System.out.println("-----------------------");
            System.out.println(lg.getResidueGroup().getPDBName());
            System.out.println(lg.getHemeGroup().getPDBName());
        }
        //OutputObject(DeserializedList);
    }
    public static void OutputObject(LinkedList<LigandResult> DeserializedList) throws FileNotFoundException, IOException{
        FileOutputStream fileOut = new FileOutputStream("D:\\BioJava\\serialized\\ObjectSerialized\\1.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        for(int i=0;i<DeserializedList.size();i++){
            out.writeObject(DeserializedList.get(i));
        }
        out.close();
        fileOut.close();
        System.err.println("File saved");
    }
    public static void HEMStatic() throws IOException, StructureException {
        MysqlDBService.LoadConnection();
        //Get all PDB
        SortedSet<String> ss = getAllIDs();
        // Create a queue
        LinkedList<String> queue = new LinkedList<String>();
        //Put all the PDB IDs in a queue
        for (String ID : ss) {
            queue.add(ID);
        }
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //Run the Analysis
        for (int i = 100000; i <= 103734; i++) {
            ProteinWorkerThread worker = new ProteinWorkerThread(queue.get(i));
            worker.run();
            System.out.println("I count: "+i);
        }
        FileOutputStream fileOut =
         new FileOutputStream("D:\\BioJava\\serialized\\pdbout110000.ser");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(LgLigandResults);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved in /tmp/employee.ser");
//        Iterator<String> ProteinIDs = ss.iterator();
//        Hashtable<String, Integer> inside = new Hashtable<String, Integer>();
//        Hashtable<String, Hashtable<String, Integer>> outside = new Hashtable<>();
//        int counter = 0;
//        inside = new Hashtable<String, Integer>();
//        while (ProteinIDs.hasNext()) {
//            String ProteinID = ProteinIDs.next();
//            Structure st = getStructure(ProteinID);
//            LinkedList<Group> HEMs = getAllHEM(st);
//            for (int i = 0; i < HEMs.size(); i++) {
//                Group HEM = HEMs.get(i);
//                //get HEM atoms
//                List<Atom> HEMAtoms = HEM.getAtoms();
//                for (Atom HEMAtom : HEMAtoms) {
//                    //Iterate through chains
//                    for (Chain c : st.getChains()) {
//                        //Iterate through groups
//                        for (Group g : c.getAtomGroups()) {
//                            //If group is HEM ignore
//                            if (!(g.getPDBName().equalsIgnoreCase("HEM"))) {
//                                for (Atom a : g.getAtoms()) {
//                                    double distance = Calc.getDistance(a, HEMAtom);
//                                    if (distance < 4) {
//                                        if (!(g.getType().equals("amino"))) {
//                                            continue;
//                                        }
//                                        ///////get elements names
////                                        inside = new Hashtable<String, Integer>();
////                                        System.out.println(g.getPDBName());
////                                        //Check if the HEM atom is in the 
////                                        if (outside.containsKey(HEMAtom.getFullName())) {
////                                            inside = outside.get(HEMAtom.getFullName());
////                                            //Check if the element being bonded to.
////                                            if (inside.containsKey(a.getElement().toString())) {
////                                                inside.put(a.getElement().toString(), inside.get(a.getElement().toString()) + 1);
////                                            } else {
////                                                inside.put(a.getElement().toString(), 1);
////                                                outside.put(HEMAtom.getFullName(), inside);
////                                            }
////
////                                        } else {
////                                            inside = new Hashtable<String, Integer>();
////                                            inside.put(a.getElement().toString(), 1);
////                                            outside.put(HEMAtom.getFullName(), inside);
////                                        }
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
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                }
//
//            }
////            if (counter > 100) {
////                break;
////            }
//            counter++;
//        }
//
//        Set<String> OutsideKeys = outside.keySet();
//
//        for (String OutsideKey : OutsideKeys) {
//            System.out.println("HEM Element :" + OutsideKey + ", Count :");
//            Set<String> InsideKeys = outside.get(OutsideKey).keySet();
//            for (String InsideKey : InsideKeys) {
//                System.out.println("\t" + InsideKey + ":" + outside.get(OutsideKey).get(InsideKey));
//            }
//        }

    }

    public static void main(String args[]) throws IOException, StructureException, ClassNotFoundException {
        String pdbLocation = "D:\\pdb";
        AtomCache cache = new AtomCache();
        cache.setPath(pdbLocation);
        StructureIO.setAtomCache(cache);
        //Generatre the HEM Statistics
        //HEMStatic();
        HEMStaticReader();
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

    public static SortedSet<String> getAllIDs() {
        return PDBStatus.getCurrentPDBIds();
    }

    public static LinkedList<Group> getAllHEM(Structure structure) {
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

    public static void Imposer() {
        try {
            StructureAlignmentJmol jmolPanel = new StructureAlignmentJmol();
            Structure structure1 = getStructure("4MU8");
            Structure structure2 = getStructure("4LPI");
            Structure structure3 = getStructure("4LPI");

            Group g1 = getHEM(structure1);
            Group g2 = getHEM(structure2);
            List<Atom> atom1 = g1.getAtoms();
            List<Atom> atom2 = g2.getAtoms();
            System.out.println(atom1.size());
            System.out.println(atom2.size());
            Atom[] atomArray1 = atom1.toArray(new Atom[atom1.size()]);
            Atom[] atomArray2 = atom2.toArray(new Atom[atom2.size()]);
            SVDSuperimposer svds = new SVDSuperimposer(atomArray1, atomArray2);
            Matrix rotMatrix = svds.getRotation();
            Atom tranMatrix = svds.getTranslation();

            Calc.rotate(structure2, rotMatrix);
            Calc.shift(structure2, tranMatrix);
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

            Group g3 = getHEM(structure3);
            Chain c3 = new ChainImpl();
            c3.setName("C");
            c3.addGroup(g3);
            //newstruc.addChain(c3);
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
}
