package C_Flex.callback;

import C_Flex.DFA;
import C_Flex.Types.*;

public interface ICallback {
    void execute(DFA dfa, DFA_event e);
}