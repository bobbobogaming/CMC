/*
 * 02.10.2016 Minor edit
 * 29.10.2009 New package structure
 * 22.10.2006 Original version
 */
 
package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.*;


public class IdEntry
{
	public int level;
	public String id;
	public Declaration attr;
	
	
	public IdEntry( int level, String id, Declaration attr )
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