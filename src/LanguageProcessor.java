import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanguageProcessor {
    public static Map<String, Integer> countLanguages(List<Respondent> respondents) {
        Map<String, Integer> languageCount = new HashMap<>();
        for (Respondent respondent : respondents) {
            for (String language : respondent.getLanguages()) {
                languageCount.put(
                        language,
                        languageCount.getOrDefault(language, 0) + 1);
            }
        }
        return languageCount;
    }

    public static List<Map.Entry<String, Integer>> sortLanguages(Map<String, Integer> languageCount) {
        List<Map.Entry<String, Integer>> sortedLanguages = new ArrayList<>(languageCount.entrySet());
        sortedLanguages.sort(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed());
        return sortedLanguages;
    }

    public static double averageLanguagesPerRespondent(List<Respondent> respondents) {
        if (respondents.isEmpty()){
            return 0.0;
        }
        int totalLanguages = 0;
        for (Respondent respondent : respondents) {
            totalLanguages += respondent.getLanguages().size();
        }
        return (double) totalLanguages / respondents.size();
    }

    public static Map<String, LanguagePair> countLanguagePairs(List<Respondent> respondents) {
        Map<String, LanguagePair> pairCount = new HashMap<>();
        for (Respondent respondent : respondents) {
            List<String> languages = respondent.getLanguages();
            for (int i = 0; i < languages.size(); i++) {
                for (int j = i + 1; j < languages.size(); j++) {
                    String language1 = languages.get(i);
                    String language2 = languages.get(j);
                    String key = language1 + "|" + language2;
                    LanguagePair pair = pairCount.get(key);
                    if (pair == null) {
                        pair = new LanguagePair(language1, language2);
                        pairCount.put(key, pair);
                    }
                    pair.incrementCount();
                }
            }
        }
        return pairCount;
    }

    public static List<LanguagePair> sortLanguagePairs(
            Map<String, LanguagePair> pairCount) {

        List<LanguagePair> sortedPairs = new ArrayList<>(pairCount.values());

        sortedPairs.sort(
                Comparator.comparing(LanguagePair::getCount).reversed());

        return sortedPairs;
    }

    public static Map<String, LanguagePartner> findBestPartners(
            Map<String, LanguagePair> pairCount) {

        Map<String, LanguagePartner> bestPartners = new HashMap<>();

        for (LanguagePair pair : pairCount.values()) {

            String language1 = pair.getLanguage1();
            String language2 = pair.getLanguage2();
            int count = pair.getCount();

            LanguagePartner current1 = bestPartners.get(language1);

            if (current1 == null || count > current1.getCount()) {
                bestPartners.put(
                        language1,
                        new LanguagePartner(language1, language2, count));
            }

            LanguagePartner current2 = bestPartners.get(language2);

            if (current2 == null || count > current2.getCount()) {
                bestPartners.put(
                        language2,
                        new LanguagePartner(language2, language1, count));
            }
        }

        return bestPartners;
    }

    public static List<LanguagePartner> sortBestPartners(
            Map<String, LanguagePartner> bestPartners) {
        List<LanguagePartner> sortedPartners = new ArrayList<>(bestPartners.values());
        sortedPartners.sort(Comparator.comparing(LanguagePartner::getCount).reversed());
        return sortedPartners;
    }

    public static List<LanguagePartner> removeDuplicatePartners(
            List<LanguagePartner> sortedPartners) {

        List<LanguagePartner> uniquePartners = new ArrayList<>();

        Set<String> seenPairs = new HashSet<>();

        for (LanguagePartner partner : sortedPartners) {

            String language1 = partner.getLanguage();
            String language2 = partner.getPartner();

            // Create the same key regardless of direction.
            // JavaScript + HTML/CSS
            // HTML/CSS + JavaScript
            // will both become the same key.
            String pairKey;

            if (language1.compareTo(language2) < 0) {
                pairKey = language1 + "|" + language2;
            } else {
                pairKey = language2 + "|" + language1;
            }

            // Only keep the first occurrence.
            if (!seenPairs.contains(pairKey)) {

                seenPairs.add(pairKey);
                uniquePartners.add(partner);
            }
        }

        return uniquePartners;
    }
}
