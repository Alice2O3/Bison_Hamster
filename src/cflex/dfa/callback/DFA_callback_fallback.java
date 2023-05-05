package cflex.dfa.callback;

import cflex.dfa.DFA;
import cflex.dfa.DFA_event;

public class DFA_callback_fallback implements ICallback{
    @Override
    public void execute(DFA dfa, DFA_event e) {
        System.out.printf("[Fallback] Processing (%d --'%c'-> %d)%n", e.current_node.getIndex(), e.getChar(), e.next_node.getIndex());
    }
}