package it.univr.pl.type;

public interface ExpType extends Type {

    String getName();

    boolean canDownCastTo(ExpType other);
}
