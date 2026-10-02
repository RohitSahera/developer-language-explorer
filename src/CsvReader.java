import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {
    public static List<Respondent> read(String filePath) throws IOException {
        List<Respondent> respondents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            //skip header
            reader.readLine();
            String row;
            while ((row =reader.readLine())!=null){
             String[] parts=row.split(",",2);
             if(parts.length<2){
                System.out.println("Skipping malformed row: "+row);
                continue;
             }
             int id;
             try{
                id=Integer.parseInt(parts[0]);
             } catch(NumberFormatException e){
                System.out.println("Skipping row with invalid ID: "+row);
                continue;
             }
             String languageText=parts[1].replace("\"","");
             if(languageText.trim().isEmpty()){
                System.out.println("Skipping row with no language data: "+ row);
                continue;
             }
             String[] languages=languageText.split(";");
             List<String> languageList = Arrays.asList(languages);
             Respondent respondent = new Respondent(id, languageList);
             respondents.add(respondent);
            }
        }
        return respondents;
    }
}
