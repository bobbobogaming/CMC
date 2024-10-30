package dk.via.ahlang.ast;

public interface AST {
    Object visit( Visitor visitor, Object arg );
}
