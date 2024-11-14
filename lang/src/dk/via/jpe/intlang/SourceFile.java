/*
 * 02.10.2016 Minor edit
 * 25.09.2009 New package structure
 * 22.09.2006 Original version (adapted from Watt&Brown)
 */
 
package dk.via.jpe.intlang;


import java.io.*;


public class SourceFile
{
	public static final char EOL = '\n';
	public static final char EOT = 0;
	
	
	private FileInputStream source;
	
	
	public SourceFile( String sourceFileName )
	{
		try {
			source = new FileInputStream( sourceFileName );
		} catch( FileNotFoundException ex ) {
			System.out.println( "*** FILE NOT FOUND *** (" + sourceFileName + ")" );
			System.exit( 1 );
		}
	}
	
	
	public char getSource()
	{
		try {
			int c = source.read();
			if( c < 0 )
				return EOT;
			else
				return (char) c;
		} catch( IOException ex ) {
			return EOT;
		}
	}
}