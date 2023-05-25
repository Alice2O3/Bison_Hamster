package C_Bison.Grammar.AST;

public interface IAST_Printable {
    String printInfoStart(Integer layer);
    String printInfoEnd(Integer layer, Boolean list_end);
}
