package LL1;
import LL1.Types.*;

import java.io.*;

public class FileIO {
    public static LL1_list read_from_file(File f){
        LL1_list V = new LL1_list();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while (true) {
                String line = br.readLine();
                if(line == null){
                    break;
                }
                V.val.add(IO.str_to_vec(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return V;
    }

    public static void write_to_file(File f, LL1_list V){
        if(V == null){
            return;
        }
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(f))) {
            for(LL1_rule rule: V.val){
                wr.write(IO.vec_to_str(rule));
                wr.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
