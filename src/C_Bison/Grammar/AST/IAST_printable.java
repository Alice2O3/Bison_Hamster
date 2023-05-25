package C_Bison.Grammar.AST;

public interface IAST_printable {
    String printInfoStart(Integer layer);
    String printInfoEnd(Integer layer, Boolean list_end);
}
