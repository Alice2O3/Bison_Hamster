package C_Bison.Parser;

import C_Bison.Grammar.LL1.LL1_Parser;
import C_Bison.Grammar.LL1.LL1_Types.*;
import C_Bison.Language.Easy_C.Easy_C_Tokens;
import C_Bison.Language.Easy_C.Lexing.Easy_C_Workflow;
import Filesystem.FileIO;
import C_Bison.Grammar.AST;

public class C_Parser implements IParser {
    private final static String grammar_file = "Grammars/Easy_C.bison";
    public AST Parse_Tokens(String code){
        Easy_C_Workflow easy_c_workflow = new Easy_C_Workflow();
        easy_c_workflow.process_code(code);
        LL1_Parser ll1_parser = new LL1_Parser();
        Parser_Exception e = ll1_parser.Parse_Tokens(FileIO.readFile(grammar_file), easy_c_workflow.getTokenList(), Easy_C_Tokens.Token_Map);
        if(e != Parser_Exception.NORMAL){
            return null;
        }
        return ll1_parser.get_AST();
    }
}
