package dk.via.ahlang;


import javax.swing.*;

public class TestDriverParser {
	private static final String EXAMPLES_DIR = System.getProperty("user.dir") + "\\lang\\src\\dk\\via\\ahlang";
	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );

		if( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
			Scanner s = new Scanner( in );
			Parser p = new Parser( s );

			p.parseProgram();
		}

	}
}
