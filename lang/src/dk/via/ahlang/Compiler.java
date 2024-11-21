package dk.via.ahlang;

import dk.via.ahlang.ast.Program;

import javax.swing.*;

public class Compiler {
    private static final String EXAMPLES_DIR = DirectoryUtil.getExamplesDir();


    public static void main(String args[])
    {
        JFileChooser fc = new JFileChooser(EXAMPLES_DIR);

        if(fc.showOpenDialog(null) == fc.APPROVE_OPTION) {
            String sourceName = fc.getSelectedFile().getAbsolutePath();

            SourceFile in = new SourceFile(sourceName);
            Scanner s = new Scanner(in);
            Parser p = new Parser(s);
            Checker c = new Checker();
            Encoder e = new Encoder();

            Program program = p.parseProgram();
            c.check(program);
            e.encode(program);

            String targetFileName = sourceName.split("\\\\")[sourceName.split("\\\\").length - 1];
            String targetFilePath = sourceName.substring(0, sourceName.length() - targetFileName.length()) + "tam\\";
            if (targetFileName.endsWith(".ah")) {

                targetFileName = targetFileName.substring(0, targetFileName.length() - 3) + ".tam";
            }
            else {
                targetFileName = targetFileName + ".tam";
            }

            e.saveTargetProgram(targetFilePath + targetFileName);
        }
    }
}
