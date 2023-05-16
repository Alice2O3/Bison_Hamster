package C_Bison.Scanner;

import C_Flex.Types.*;

import java.util.List;

public interface IScanner {
    DFA_lexing_list scan(String code);
}
