package C_Bison.PreProcess;

public class PreProcess_C implements IPreProcess{
    public String process(String code) {
        String code_processed = code.replaceAll("/\\*[\\s\\S]*\\*/", "");

        String[] lines = code_processed.split("\n");
        StringBuilder sb = new StringBuilder();
        for(String line : lines){
            String line_processed = process_line(line);
            if(!line_processed.isEmpty()){
                sb.append(line_processed).append("\n");
            }
        }
        return sb.toString();
    }

    private String process_line(String line){
        return line.replaceAll("\\t","    ")
                .replaceAll("//.*|#.*", "")
                .replaceAll(" +", " ")
                .trim();
    }
}
