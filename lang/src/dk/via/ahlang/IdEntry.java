package dk.via.ahlang;

import dk.via.ahlang.ast.Statement;

public class IdEntry
{
    public int level;
    public String id;
    public Statement attr;


    public IdEntry( int level, String id, Statement attr )
    {
        this.level = level;
        this.id = id;
        this.attr = attr;
    }


    public String toString()
    {
        return level + "," + id;
    }
}
