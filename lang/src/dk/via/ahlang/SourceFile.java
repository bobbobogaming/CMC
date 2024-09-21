package dk.via.ahlang;

import java.io.*;

public class SourceFile
{
    public static final char EOL = '\n';
    public static final char EOT = 0;


    private BufferedReader source;


    public SourceFile( String sourceFileName )
    {
        try {
            source = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFileName), "UTF-8"));
        } catch( FileNotFoundException ex ) {
            System.out.println( "*** FILE NOT FOUND *** (" + sourceFileName + ")" );
            System.exit( 1 );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
