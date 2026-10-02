import java.util.List;
public class Respondent {
    private int id;
    private List<String> languages;
    public Respondent(int id, List<String> languages){
        this.id=id;
        this.languages=languages;
    }
    public int getId(){
        return id;
    }
    public List<String> getLanguages(){
        return languages;
    }
}
