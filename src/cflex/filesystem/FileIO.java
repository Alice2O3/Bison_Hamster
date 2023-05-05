package cflex.filesystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    public static List<String> readFileIntoList(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return lines;
    }

    public static String readFile(String filePath){
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    public static void writeFile(List<String> lines, String filePath){
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
            for(String line: lines){
                wr.write(line);
                wr.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
