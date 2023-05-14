package C_Bison.Scanner;

import C_Bison.Language.Easy_C.Tokens;
import C_Flex.*;
import C_Flex.callback.Common;
import C_Flex.callback.ICallback;

import java.util.List;

import static C_Flex.callback.Common.acceptFinishedKeep;

public class C_Scanner implements IScanner {
    private final DFA dfa;

    private static class States {
        private static final int INITIAL = 0;
        private static final int IDENTIFIER = 1;
        private static final int INTEGER = 2;
        private static final int length = 16;
    }

    private static class Identifier_Keep implements ICallback{
        public void execute(DFA dfa, DFA_event e) {
            String lexeme = dfa.getLexeme();
            if(lexeme.equals("if")){
                acceptFinishedKeep(dfa, Tokens.IF);
            }
            else if(lexeme.equals("for")){
                acceptFinishedKeep(dfa, Tokens.FOR);
            }
            else if(lexeme.equals("return")){
                acceptFinishedKeep(dfa, Tokens.RETURN);
            }
            else if(lexeme.equals("int")){
                acceptFinishedKeep(dfa, Tokens.INT);
            }
            else if(lexeme.equals("void")){
                acceptFinishedKeep(dfa, Tokens.VOID);
            }
            else {
                acceptFinishedKeep(dfa, Tokens.IDENTIFIER);
            }
        }
    }

    private static class Callbacks{
        private static final ICallback ACCEPT = new Common.Accept();
        private static final ICallback FORWARD = new Common.Forward();
        private static final ICallback ESCAPE = new Common.Escape();
        private static final ICallback IDENTIFIER_KEEP = new Identifier_Keep();
        private static final ICallback DISCARD_KEEP = new Common.Discard_Keep();
        private static ICallback KEEP(Integer token_type) {
            return new Common.Accept_Keep(token_type);
        }
    }

    public C_Scanner(){
        dfa = new DFA();
        dfa.initSize(C_Scanner.States.length);
        //Set Rules
        setGeneralRules();
    }

    @Override
    public List<DFA_lexing> scan(String code) {
        dfa.processString(code);
        return dfa.tokens_list;
    }

    private void setGeneralRules(){
        dfa.addEdge(new DFA_edge(States.INITIAL, States.INITIAL, '\n', Callbacks.ESCAPE));
    }
}