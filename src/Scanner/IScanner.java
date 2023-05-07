package Scanner;

import Cflex.DFA_lexing;

import java.util.List;

public interface IScanner {
    List<DFA_lexing> scan(String code);
}
