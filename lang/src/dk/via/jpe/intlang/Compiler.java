/*
 * 04.10.2016 Minor edit
 * 15.10.2010 IScanner and IParser
 * 11.11.2009 New package structure
 * 26.10.2006 Original version
 */
  
package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.*;

import javax.swing.*;


public class Compiler
{
	private static final String EXAMPLES_DIR = "c:\\usr\\undervisning\\CMC\\IntLang\\examples";
	
	
	public static void main( String args[] )
	{
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );
		
		if( fc.showOpenDialog( null ) == fc.APPROVE_OPTION ) {
			String sourceName = fc.getSelectedFile().getAbsolutePath();
			
			SourceFile in = new SourceFile( sourceName );
			Scanner s = new Scanner( in );
			ParserOperatorPrecedence p = new ParserOperatorPrecedence( s );
			Checker c = new Checker();
			Encoder e = new Encoder();
		
			Program program = (Program) p.parseProgram();
			c.check( program );
			e.encode( program );
			
			String targetName;
			if( sourceName.endsWith( ".txt" ) )
				targetName = sourceName.substring( 0, sourceName.length() - 4 ) + ".tam";
			else
				targetName = sourceName + ".tam";
			
			e.saveTargetProgram( targetName );
		}
	}
}