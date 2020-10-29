import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class bornBabyCompiler {
    public static void parser(String location) {
        try {
//constructor of file class having file as argument
            File file = new File(location);
            FileWriter firstWriter = new FileWriter("codeWithoutComment.out");
            FileInputStream filePointer = new FileInputStream(file);     //opens a connection to an actual file
            int r = 0;
            StringBuilder code = new StringBuilder();
            while ((r = filePointer.read()) != -1) {
                if (r == '/') {
                    char temp = (char) filePointer.read();
                    if (temp == '/') {
                        while (temp != '\n') {
                            temp = (char) filePointer.read();
                        }
                    } else if (temp == '*') {
                        temp = (char) filePointer.read();
                        while (temp != '/') {
                            temp = (char) filePointer.read();
                        }
                    }
                } else {
                    firstWriter.write((char) r);
                    code.append((char)r);
                }
            }
            firstWriter.close();
            String[] array = code.toString().split("\r\n");
            for (int i = 0; i < array.length; i++) {
                String temp = array[i].trim();
                if(temp.equalsIgnoreCase("begin")){
                    compileAssembly(array, i+1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compileAssembly(String[] array, int index) throws IOException {
        FileWriter firstWriter = new FileWriter("code.s");
        String assemblyData = ".data\n";
        String codeEnd = "\tli\t$v0,\t10\t\n\tsyscall";
        char variables = 'A';
        char[] variablesUsed = new char[26];
        int variablesUsedCount = 0;
        String assemblyPrint = "\tli\t$v0,\t4\t\n\tla\t$a0\t";
        StringBuilder code = new StringBuilder();
        String[] printCommands = new String[array.length];
        int printCommandCounter = -1;
        String codeString = "";
        while(index<array.length && !array[index].trim().equalsIgnoreCase("end")){
            codeString = codeString + array[index] + " ";
            index++;
        }
        String[] tokens = codeString.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if(tokens[i].equalsIgnoreCase("print")){
                i++;
                printCommandCounter++;
                printCommands[printCommandCounter] = ":\t.asciiz\t\"";
                while(i< tokens.length && !tokens[i].equalsIgnoreCase("print")){
                    printCommands[printCommandCounter] = printCommands[printCommandCounter] + tokens[i] + " ";
                    i++;
                }
                printCommands[printCommandCounter] = printCommands[printCommandCounter] + "\\n\"\n";
                i--;
            }
        }
        code.append(assemblyData);
        for (int i = 0; i <= printCommandCounter; i++) {
            code.append(variables).append(printCommands[i]);
            variablesUsed[variablesUsedCount] = variables;
            variablesUsedCount++;
            variables++;
        }
        code.append("\n.text\nmain:\n");
        for (int i = 0; i < variablesUsedCount; i++) {
            code.append(assemblyPrint).append(variablesUsed[i]).append("\n\tsyscall\n");
        }
        code.append(codeEnd);
        firstWriter.write(code.toString());
        firstWriter.close();
        System.out.println(code.toString());
    }

    public static void main(String[] args) {
        System.out.println("Please input file location");
        Scanner s = new Scanner(System.in);
        String location = s.nextLine();
        parser(location);
    }
}
