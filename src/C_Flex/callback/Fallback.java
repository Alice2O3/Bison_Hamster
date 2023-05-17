package C_Flex.callback;

import C_Flex.DFA;
import C_Flex.DFA_Types.*;

public class Fallback implements ICallback{
    @Override
    public void execute(DFA dfa, DFA_event e) {
        System.out.printf("[Fallback] Processing (%d --'%c'-> %d)%n", e.current_node.getIndex(), e.getChar(), e.next_node.getIndex());
    }
}