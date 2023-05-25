package C_Bison.Parser;

import C_Bison.Grammar.AST.IAST_Node;

public interface IParser {
    IAST_Node Parse_Tokens(String code);
}
