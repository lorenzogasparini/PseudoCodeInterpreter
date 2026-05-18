package it.univr.pl.type;

import it.univr.pl.value.*;

public class TypeUtils {

    public static ExpType fromString(String str) {
        return switch (str) {
            case "int" -> SimpleType.INT;
            case "dec" -> SimpleType.DEC;
            case "bool" -> SimpleType.BOOL;
            case "string" -> SimpleType.STRING;

            default -> null;
        };
    }

    public static ExpType fromValue(ExpValue<?> value) {
        if (value instanceof IntValue)
            return SimpleType.INT;
        if (value instanceof DecValue)
            return SimpleType.DEC;
        if (value instanceof BoolValue)
            return SimpleType.BOOL;
        if (value instanceof StringValue)
            return SimpleType.STRING;

        return null; // unreachable code
    }

    public static ExpValue<?> castValue(ExpValue<?> value, ExpType targetType) {
        ExpType valueType = fromValue(value);

        if (valueType == SimpleType.INT) {
            assert value instanceof IntValue; // always true
            IntValue intValue = (IntValue) value;
            if (targetType == SimpleType.DEC)
                return new DecValue((double) intValue.toJavaValue().intValue());
        }

        if (valueType == SimpleType.DEC) {
            assert value instanceof DecValue; // always true
            DecValue decValue = (DecValue) value;
            if (targetType == SimpleType.INT)
                return new IntValue((int) decValue.toJavaValue().doubleValue());
        }

        return value;
    }

}
