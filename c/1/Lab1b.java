import java.util.*;
class Opcode {
    String statementClass;
    String machineCode;
    public Opcode(String statementClass, String machineCode) {
        this.statementClass = statementClass;
        this.machineCode = machineCode;
    }
}
public class Lab1b {
    private static final Map < String, Opcode > OPTAB = new HashMap < > ();
    private static final Map < String, String > REGTAB = new HashMap < > ();
    private static final Map < String, Integer > SYMTAB = new HashMap < > ();
    static {
        OPTAB.put("STOP", new Opcode("IS", "00"));
        OPTAB.put("ADD", new Opcode("IS", "01"));
        OPTAB.put("SUB", new Opcode("IS", "02"));
        OPTAB.put("MULT", new Opcode("IS", "03"));
        OPTAB.put("MOVER", new Opcode("IS", "04"));
        OPTAB.put("MOVEM", new Opcode("IS", "05"));
        OPTAB.put("COMP", new Opcode("IS", "06"));
        OPTAB.put("BC", new Opcode("IS", "07"));
        OPTAB.put("DIV", new Opcode("IS", "08"));
        OPTAB.put("READ", new Opcode("IS", "09"));
        OPTAB.put("PRINT", new Opcode("IS", "10"));
        OPTAB.put("DC", new Opcode("DL", "01"));
        OPTAB.put("DS", new Opcode("DL", "02"));

        OPTAB.put("START", new Opcode("AD", "01"));
        OPTAB.put("END", new Opcode("AD", "02"));
        OPTAB.put("ORIGIN", new Opcode("AD", "03"));
        OPTAB.put("EQU", new Opcode("AD", "04"));
        OPTAB.put("LTORG", new Opcode("AD", "05"));
        REGTAB.put("AREG", "1");
        REGTAB.put("BREG", "2");
        REGTAB.put("CREG", "3");
        REGTAB.put("DREG", "4");
    }
    public static void main(String[] args) {
        List < String > instructions = Arrays.asList(
            "START 200",
            "MOVER AREG D1",
            "ADD AREG D2",
            "D1 DC 5",
            "D2 DS 3",
            "END"
        );
        int lc = 0;
        boolean startFound = false;
        List < String > labels = new ArrayList < > ();
        List < String > mnemonics = new ArrayList < > ();
        List < String > op1s = new ArrayList < > ();
        List < String > op2s = new ArrayList < > ();
        for (String instruction: instructions) {
            String label = "None";
            String mnemonic = "";
            String op1 = "[]";
            String op2 = "[]";
            String[] parts = instruction.split("\\s+");
            if (parts.length == 1) {
                mnemonic = parts[0];
            } else if (parts.length == 2) {
                mnemonic = parts[0];

                op2 = parts[1];
            } else if (parts.length == 3) {
                if (isMnemonic(parts[0])) {
                    mnemonic = parts[0];
                    op1 = parts[1];
                    op2 = parts[2];
                } else {
                    label = parts[0];
                    mnemonic = parts[1];
                    op2 = parts[2];
                }
            } else if (parts.length == 4) {
                label = parts[0];
                mnemonic = parts[1];
                op1 = parts[2];
                op2 = parts[3];
            }
            if (mnemonic.equals("START") && parts.length == 2) {
                lc = Integer.parseInt(parts[1]);
                startFound = true;
            }
            if (!label.equals("None")) {
                SYMTAB.put(label, lc);
            }
            labels.add(label);
            mnemonics.add(mnemonic);
            op1s.add(op1);
            op2s.add(op2);
            if (startFound && !mnemonic.equals("START") && !mnemonic.equals("END")) {
                lc++;
            }
        }
        // Print intermediate machine code with symbols
        System.out.println("\nIntermediate Machine Code:");
        System.out.println("LC\tOpcode\t\tOp1\tOp2\t\t\t");
        lc = startFound ? Integer.parseInt(instructions.get(0).split("\\s+")[1]) : 0;

        // Print machine code for instructions
        for (int i = 0; i < mnemonics.size(); i++) {
            String mnemonic = mnemonics.get(i);
            String op1 = op1s.get(i);
            String op2 = op2s.get(i);
            Opcode opcode = OPTAB.get(mnemonics.get(i));
            if (opcode == null) {
                System.out.println("Error: Unknown mnemonic " + mnemonic);
                continue;
            }
            String machineCode = opcode.statementClass + " " + opcode.machineCode;
            if (REGTAB.containsKey(op1)) {
                op1 = REGTAB.get(op1);
            }
            if (SYMTAB.containsKey(op2)) {
                op2 = "S " + SYMTAB.get(op2);
            } else if (isNumeric(op2)) {
                op2 = "C " + op2;
            }
            if (opcode.statementClass.equals("IS")) {
                System.out.printf("%d\t%s\t\t%s\t%s\n", lc++, machineCode, op1, op2);
            } else if (opcode.statementClass.equals("DL")) {
                System.out.printf("%d\t%s\t\t-\t%s\n", lc++, machineCode, op2);
            } else if (opcode.statementClass.equals("AD")) {
                // Skip printing the END statement
                if (mnemonic.equals("END")) {
                    continue;
                }
                System.out.printf("-\t%s\t\t-\t%s\n", machineCode, op2);
            }
        }
        // Print symbol table
        System.out.println("\nSymbol Table:");
        System.out.println("Label\tAddress");
        System.out.println("----------------");

        for (Map.Entry < String, Integer > entry: SYMTAB.entrySet()) {
            System.out.printf("%s\t%d\n", entry.getKey(), entry.getValue());
        }
    }
    private static boolean isMnemonic(String str) {
        return OPTAB.containsKey(str);
    }
    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}