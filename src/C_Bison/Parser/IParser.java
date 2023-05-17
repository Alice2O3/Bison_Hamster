package C_Bison.Parser;

import C_Bison.Grammar.AST;

public interface IParser {
    AST Parse_Tokens(String code);
}
