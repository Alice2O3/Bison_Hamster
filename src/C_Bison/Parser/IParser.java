package C_Bison.Parser;

import C_Bison.Grammar.AST.IAST_Node;

public interface IParser {
    IAST_Node Parse_Tokens(String code);
    String get_Grammar_Text();
    String get_Symbol_Info();
}
