public class LanguagePair {
    private String language1;
    private String language2;
    private int count;
    public LanguagePair(String language1, String language2){
        this.language1=language1;
        this.language2=language2;
        this.count=0;
    }
    public String getLanguage1(){
        return language1;
    }
    public String getLanguage2(){
        return language2;
    }
    public int getCount(){
        return count;
    }
    public void incrementCount(){
        count++;
    }
    public String getPairName(){
        return language1 + " + " + language2;
    }
}
