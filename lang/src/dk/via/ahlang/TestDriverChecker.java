package dk.via.ahlang;

import dk.via.ahlang.ast.Program;

import javax.swing.*;

public class TestDriverChecker {
    private static final String EXAMPLES_DIR = DirectoryUtil.getExamplesDir();
    public static void main(String args[])
    {
        JFileChooser fc = new JFileChooser( EXAMPLES_DIR );

        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            dk.via.ahlang.SourceFile in = new SourceFile(fc.getSelectedFile().getAbsolutePath());
            dk.via.ahlang.Scanner s = new Scanner(in);
            Parser p = new Parser(s);
            Checker c = new Checker();

            Program program = p.parseProgram();
            c.check(program);
        }
    }
}
