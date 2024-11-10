import java.io.*;
import java.util.*;

public class Lab3{
    public static void main(String[] args){
        System.out.println("Done");
        String sourceFilePath = "example.txt";
        String mntFilePath = "mnt.txt";
        String mdtFilePath = "mdt.txt";
        String alaFilePath = "ala.txt";
        String intermediateFilePath = "icode.txt";
        
        // Pass 1
        pass1(sourceFilePath, mntFilePath, mdtFilePath, alaFilePath,intermediateFilePath);
        
    }


    public static void pass1(String sourceFilePath, String mntFilePath, String mdtFilePath, String alaFilePath, String intermediateFilePath) {
        HashMap<Integer, String> mdt = new HashMap<>();
        LinkedHashMap<String, String> ala = new LinkedHashMap<>();
        int mdtIndex = 1;
        int alaIndex = 1;
    
        MacroNameTable mnt = new MacroNameTable();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
             BufferedWriter mntWriter = new BufferedWriter(new FileWriter(mntFilePath));
             BufferedWriter mdtWriter = new BufferedWriter(new FileWriter(mdtFilePath));
             BufferedWriter alaWriter = new BufferedWriter(new FileWriter(alaFilePath));
             BufferedWriter intermediateWriter = new BufferedWriter(new FileWriter(intermediateFilePath))) {
    
            String line = reader.readLine();
            boolean insideMacro = false;
            boolean insideBlock = false;
    
            while (line != null) {
                if (line.equals("MACRO")) {
                    line = reader.readLine();
                    String[] splitStr = line.split("\\s+");
    
                    mdt.put(mdtIndex, line);
                    mnt.add(splitStr[0], mdtIndex);
                    mntWriter.write(mdtIndex + "\t" + splitStr[0] + "\t" + mdtIndex + "\n");
    
                    for (int a = 1; a < splitStr.length; a++) {
                        ala.put("#" + alaIndex, splitStr[a]);
                        alaWriter.write("#" + alaIndex + "\t" + splitStr[a] + "\n");
                        alaIndex++;
                    }
    
                    line = reader.readLine();
                    while (line != null && !line.equals("MEND")) {
                        line = replaceDummiesWithIndices(line, ala);
                        mdt.put(mdtIndex, line);
                        mdtWriter.write(mdtIndex + "\t" + line + "\n");
                        mdtIndex++;
                        line = reader.readLine();
                    }
    
                    if (line != null && line.equals("MEND")) {
                        mdt.put(mdtIndex, line);
                        mdtWriter.write(mdtIndex + "\t" + line + "\n");
                        mdtIndex++;
                        line = reader.readLine();
                    }
                } else if (line.equals("START")) {
                    insideBlock = true;
                    line = reader.readLine();
                    intermediateWriter.write("START"+ "\n");
                    while (line != null && !line.equals("END")) {
                        intermediateWriter.write(line + "\n");
                        line = reader.readLine();
                    }
                    if (line != null && line.equals("END")) {
                        intermediateWriter.write(line + "\n");
                        insideBlock = false;
                        line = reader.readLine();
                    }
                } else {
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class MacroNameTable {
        private List<Integer> mntIndexList = new ArrayList<>();
        private List<String> mntNameList = new ArrayList<>();
        private List<Integer> mdtIndexList = new ArrayList<>();
        private int mntIndex = 1;

        public void add(String name, int mdtIndex) {
            mntIndexList.add(mntIndex);
            mntNameList.add(name);
            mdtIndexList.add(mdtIndex);
            mntIndex++;
        }

        public int getMntIndexByName(String name) {
            for (int i = 0; i < mntNameList.size(); i++) {
                if (mntNameList.get(i).equals(name)) {
                    return mntIndexList.get(i);
                }
            }
            return -1;
        }

        public int getMdtIndexByName(String name) {
            for (int i = 0; i < mntNameList.size(); i++) {
                if (mntNameList.get(i).equals(name)) {
                    return mdtIndexList.get(i);
                }
            }
            return -1;
        }

        public void displayTable() {
            System.out.printf("%-10s %-15s %-10s%n", "MNT Index", "MNT Name", "MDT Index");
            for (int i = 0; i < mntIndexList.size(); i++) {
                System.out.printf("%-10d %-15s %-10d%n", mntIndexList.get(i), mntNameList.get(i), mdtIndexList.get(i));
            }
        }
    }
    public static String replaceDummiesWithIndices(String line, LinkedHashMap<String, String> ala) {
        for (Map.Entry<String, String> entry : ala.entrySet()) {
            String index = entry.getKey();
            String value = entry.getValue();
            line = line.replace(value, index);
        }
        return line;
    }
}