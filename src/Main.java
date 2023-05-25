import C_Bison.Grammar.AST.AST_Abstract_Render;
import C_Bison.Grammar.AST.IAST_Node;
import C_Bison.Parser.C_Parser;
import C_Bison.PreProcess.PreProcess_C;
import Util.FileIO;

public class Main {
    private final static String grammar_file = "Grammars/Easy_C.bison";
    private final static String input_file = "Main/main.c";
    private final static String preprocessed_file = "Main/main.pp.c";
    private final static String ast_file = "Main/AST.json";

    //Tribute to anime artist Bison Hamster (https://www.pixiv.net/users/333556)
    private final static String Bison_Hamster_ASCII = "img/Bison_Hamster_ASCII.txt";
    private final static String Bison_Hamster_Hi = String.format("Hello, I'm Bison Hamster!\n" +
            "I will help you parse grammar rule from \"%s\" and save AST files to \"%s\" >ω<!\n", grammar_file, ast_file);
    private final static String Bison_Hamster_Finished = String.format("AST file saved to \"%s\"! >ω<\n", ast_file);

    public static void main(String[] args) {
        //Show Pic
        String bison_hamster_show = FileIO.readFile(Bison_Hamster_ASCII);
        System.out.print(bison_hamster_show);
        System.out.print(Bison_Hamster_Hi);

        //Preprocessing code
        String source_code = FileIO.readFile(input_file);
        if(source_code == null){
            System.out.print("File Read Error!\n");
            return;
        }
        PreProcess_C preprocess = new PreProcess_C();
        String formatted_code = preprocess.process(source_code);
        FileIO.writeFile(formatted_code, preprocessed_file);

        //Process Tokens
        String source_code_2 = FileIO.readFile(preprocessed_file);
        if(source_code_2 == null){
            System.out.print("File Read Error!\n");
            return;
        }
        C_Parser parser = new C_Parser();
        System.out.printf("\nProcessing:\n%s\n", parser.get_Grammar_Text());
        IAST_Node ast = parser.Parse_Tokens(source_code_2);
        if(ast == null){
            System.out.print("AST Parsing Error!\n");
            return;
        }
        String node_info = AST_Abstract_Render.convert_to_json(ast);
        System.out.print(bison_hamster_show);
        System.out.print(Bison_Hamster_Finished);
        FileIO.writeFile(node_info, ast_file);
    }
}