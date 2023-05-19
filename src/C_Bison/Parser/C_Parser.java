package C_Bison.Parser;

import C_Bison.Grammar.LL1.LL1_Parser;
import C_Bison.Grammar.LL1.LL1_Types.*;
import C_Bison.Language.Easy_C.Easy_C_Tokens;
import C_Bison.Language.Easy_C.Lexing.Easy_C_Workflow;
import Filesystem.FileIO;
import C_Bison.Grammar.AST;

public class C_Parser implements IParser {
    private final static String grammar_file = "Grammars/Easy_C.bison";
    private Parser_Exception parser_exception;
    private String grammar_text;
    private LL1_Parser ll1_parser;

    public C_Parser(){
        ll1_parser = new LL1_Parser();
        grammar_text = FileIO.readFile(grammar_file);
        ll1_parser.Pre_Process(grammar_text, Easy_C_Tokens.Token_Map);
        parser_exception = ll1_parser.get_Exception();
    }

    public AST Parse_Tokens(String code){
        Easy_C_Workflow easy_c_workflow = new Easy_C_Workflow();
        easy_c_workflow.process_code(code);
        AST ast = ll1_parser.Parse_Tokens(easy_c_workflow.getTokenList());
        parser_exception = ll1_parser.get_Exception();
        return ast;
    }

    public Parser_Exception get_Exception(){
        return parser_exception;
    }

    public String get_Grammar_Text(){
        return grammar_text;
    }
}
