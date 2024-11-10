
import java.io.*;
import java.util.*;
 
public class Lab4 {
    public static void main(String[] args) {
        String mntFilePath = "mnt.txt";
        String mdtFilePath = "mdt.txt";
        String alaFilePath = "ala.txt";
        String intermediateFilePath = "icode.txt";
        pass2(intermediateFilePath, mntFilePath, mdtFilePath, alaFilePath);
    }
    
    
    public static void pass2(String intermediateFilePath, String mntFilePath, String mdtFilePath, String alaFilePath) {
        LinkedHashMap<String, String> ala = new LinkedHashMap<>();
        HashMap<Integer, String> mdt = new HashMap<>();
        MacroNameTable mnt = new MacroNameTable();
        LinkedHashMap<String, String> ala_act = new LinkedHashMap<>();
        int ala_actindex = 1;

        // Load MDT, MNT, and ALA from files
        loadMntFromFile(mntFilePath, mnt);
        loadMdtFromFile(mdtFilePath, mdt);
        loadAlaFromFile(alaFilePath, ala);

        System.out.println("Expanded Code: ");

        // Process intermediate file for expanded code generation
        try (BufferedReader reader = new BufferedReader(new FileReader(intermediateFilePath))) {
            String line = reader.readLine();
            while (line != null) {
                String[] splitStr = line.split("\\s+");
                int mntIndex = mnt.getMntIndexByName(splitStr[0]);

                if (mntIndex > -1) {
                    int t = 1;
                    while (t < splitStr.length) {
                        ala_act.put("#" + ala_actindex, splitStr[t]);
                        ala_actindex++;
                        t++;
                    }

                    int startIndex = mnt.getMdtIndexByName(splitStr[0]);
                    for (int i = startIndex ; i <= mdt.size(); i++) {
                        String mdtLine = mdt.get(i);
                        if (mdtLine.equals("MEND")) {
                            break;
                        }
                        mdtLine = replaceDummiesWithActuals(mdtLine, ala, splitStr);
                        System.out.println(mdtLine);
                    }
                } else {
                    System.out.println(line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public static String replaceDummiesWithActuals(String line, LinkedHashMap<String, String> ala, String[] actualParams) {
        int actualParamCount = actualParams.length - 1;
        int alaCount = ala.size();

        for (int i = 1; i <= alaCount; i++) {
            String actualParam = (actualParamCount >= i) ? actualParams[i] : actualParams[1];
            String dummyArg = "#" + i;
            line = line.replace(dummyArg, actualParam);
        }
        return line;
    }

    public static void loadMntFromFile(String mntFilePath, MacroNameTable mnt) {
        try (BufferedReader reader = new BufferedReader(new FileReader(mntFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                mnt.add(parts[1], Integer.parseInt(parts[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMdtFromFile(String mdtFilePath, HashMap<Integer, String> mdt) {
        try (BufferedReader reader = new BufferedReader(new FileReader(mdtFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                mdt.put(Integer.parseInt(parts[0]), parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAlaFromFile(String alaFilePath, LinkedHashMap<String, String> ala) {
        try (BufferedReader reader = new BufferedReader(new FileReader(alaFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                ala.put(parts[0], parts[1]);
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
}
