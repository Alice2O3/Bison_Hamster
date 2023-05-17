package C_Bison.Language.Easy_C;

public class Easy_C_Tokens {
    public static final int IDENTIFIER = 0;
    public static final int INTEGER = 1;
    public static final int INT = 2;
    public static final int VOID = 3;
    public static final int LEFT_SMALL_BRACKET = 4;
    public static final int RIGHT_SMALL_BRACKET = 5;
    public static final int LEFT_BIG_BRACKET = 6;
    public static final int RIGHT_BIG_BRACKET = 7;
    public static final int COMMA = 8;
    public static final int STATEMENT_END = 9;
    public static final int PLUS = 10;
    public static final int MINUS = 11;
    public static final int ASSIGN = 12;
    public static final int ASSIGNADD = 13;
    public static final int ASSIGNMINUS = 14;
    public static final int SMALLER = 15;
    public static final int GREATER = 16;
    public static final int SMALLEREQUAL = 17;
    public static final int GREATEREQUAL = 18;
    public static final int EQUALTO = 19;
    public static final int FOR = 20;
    public static final int IF = 21;
    public static final int RETURN = 22;
    public static final int EOF = 23;
    public static final String[] Token_Map = {
            "Identifier", "Integer", "Int", "Void", "Left_small_bracket", "Right_small_bracket",
            "Left_big_bracket", "Right_big_bracket", "Comma", "Statement_end", "Plus",
            "Minus", "Assign", "AssignAdd", "AssignMinus", "Smaller",
            "Greater", "SmallerEqual", "GreaterEqual", "Equalto", "For",
            "If", "Return", "EOF"
    };
}
