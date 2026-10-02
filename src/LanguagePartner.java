public class LanguagePartner {
    private String language;
    private String partner;
    private int count;
    public LanguagePartner(
        String language,
        String partner,
        int count){
            this.language=language;
            this.partner=partner;
            this.count=count;
        }
        public String getLanguage(){
            return language;
        }
        public String getPartner(){
            return partner;
        }
        public int getCount(){
            return count;
        }
}
