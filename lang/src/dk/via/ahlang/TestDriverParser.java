package dk.via.ahlang;


import dk.via.ahlang.ast.Program;

import javax.swing.*;

public class TestDriverParser {
	private static final String EXAMPLES_DIR = DirectoryUtil.getExamplesDir();
	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );

		if( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
			Scanner s = new Scanner( in );
			Parser p = new Parser( s );

			Program program = p.parseProgram();
			new ASTViewer(program);
		}

	}
}
