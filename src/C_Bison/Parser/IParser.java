package C_Bison.Parser;

import C_Bison.Grammar.AST.IAST_node;

public interface IParser {
    IAST_node Parse_Tokens(String code);
}
