package C_Bison.Grammar.AST;

import C_Flex.DFA_Types.*;
import Util.StringIO;

public class AST_printable_abstract {
    public static class AST_node_abstract_finished extends AST_node implements IAST_printable {
        private final DFA_lexing lexing; //Terminal
        public AST_node_abstract_finished(DFA_lexing lexing_){
            lexing = lexing_;
        }

        @Override
        public String getSymbol(){
            return lexing.token;
        }

        @Override
        public String printInfoStart(Integer layer){
            return StringIO.tab_n(layer) +
                "\"value\": \"" +
                lexing.token +
                "\"\n";
        }

        @Override
        public String printInfoEnd(Integer layer, Boolean list_end){
            return "";
        }
    }

    public static class AST_node_abstract_non_finished extends AST_node implements IAST_printable {
        private final String symbol; //Non_terminal
        public AST_node_abstract_non_finished(String symbol_){
            symbol = symbol_;
        }

        @Override
        public String getSymbol(){
            return symbol;
        }

        @Override
        public String printInfoStart(Integer layer){
            return StringIO.tab_n(layer) +
                "\"type\": \"" +
                symbol +
                "\",\n" +
                StringIO.tab_n(layer) +
                "\"children\":\n" +
                StringIO.tab_n(layer) +
                "[\n" +
                StringIO.tab_n(layer + 1) +
                "{\n";
        }

        @Override
        public String printInfoEnd(Integer layer, Boolean list_end){
            StringBuilder sb = new StringBuilder();
            sb.append(StringIO.tab_n(layer + 1))
                    .append("}\n")
                    .append(StringIO.tab_n(layer))
                    .append("]");
            if(!list_end){
                sb.append("\n")
                        .append(StringIO.tab_n(layer - 1))
                        .append("},\n")
                        .append(StringIO.tab_n(layer - 1))
                        .append("{");
            }
            sb.append("\n");
            return sb.toString();
        }
    }
}
