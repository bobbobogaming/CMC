package dk.via.ahlang;

import dk.via.ahlang.ast.Statement;

import java.util.*;

public class IdentificationTable {
    private List<IdEntry> idTable = new ArrayList<>();
    private int level;

    public IdentificationTable() {
        level = 0;
    }

    public void enter( String id, Statement attr )
    {
        IdEntry entry = find( id );

        if( entry != null && entry.level == level )
            System.out.println( id + " declared twice" );
        else
            idTable.add( new IdEntry( level, id, attr ) );
    }


    public Statement retrieve( String id )
    {
        IdEntry entry = find( id );

        if( entry != null )
            return entry.attr;
        else
            return null;
    }


    public void openScope()
    {
        ++level;
    }


    public void closeScope()
    {
        int pos = idTable.size() - 1;
        while( pos >= 0 && idTable.get(pos).level == level ) {
            idTable.remove( pos );
            pos--;
        }

        level--;
    }


    private IdEntry find( String id )
    {
        for( int i = idTable.size() - 1; i >= 0; i-- )
            if( idTable.get(i).id.equals( id ) )
                return idTable.get(i);

        return null;
    }
}
