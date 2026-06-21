package it.univr.pl.type;

public enum SimpleType implements ExpType {
    INT("int"),
    DEC("dec"),
    BOOL("bool"),
    CHAR("char"),
    STRING("string");

    private final String name;

    SimpleType(String name) { this.name = name; }

    @Override
    public String getName() { return name; }

    @Override
    public boolean isCompatible(Type other) {
        if (this == other) return true;
        return this == SimpleType.DEC && other == SimpleType.INT; // solo una var DEC può accogliere un INT, non viceversa
    }

    @Override
    public boolean canDownCastTo(ExpType other) {
        return this == SimpleType.DEC && other == SimpleType.INT;
    }

}
