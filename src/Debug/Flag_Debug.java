package Debug;

import Grammar.LL1.Types.LL1_flag;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Flag_Debug {
    public static void main(String[] args){
        List<Integer> V = Arrays.asList(1, 2, 3, 4, 5);
        Traverse_List(V);
    }

    public static void Change_val(LL1_flag b){
        b.val = true;
    }

    public static void Traverse_List(List<Integer> V){
        ListIterator<Integer> i = V.listIterator();
        while(i.hasNext()){
            Integer pi = i.next();
            System.out.printf("%d:", pi);
            ListIterator<Integer> j = V.listIterator(i.nextIndex());
            while(j.hasNext()){
                Integer pj = j.next();
                System.out.printf("%d", pj);
            }
            System.out.printf("\n");
        }
    }
}
