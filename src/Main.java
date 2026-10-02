import java.util.*;
import java.io.IOException;

public class Main {

    public static void main(String args[]) {

        // --------------------------------------------------
        // 1. Check command-line argument
        // --------------------------------------------------

        if (args.length != 1) {
            System.out.println("Usage: java Main <csv-file>");
            return;
        }

        String filePath = args[0];

        try {

            // --------------------------------------------------
            // 2. Read CSV data
            // --------------------------------------------------

            List<Respondent> respondents =
                    CsvReader.read(filePath);

            System.out.println(
                    "Respondents loaded: "
                            + respondents.size()
            );


            // --------------------------------------------------
            // 3. Analyze language popularity
            // --------------------------------------------------

            Map<String, Integer> languageCount =
                    LanguageProcessor.countLanguages(respondents);

            List<Map.Entry<String, Integer>> sortedLanguages =
                    LanguageProcessor.sortLanguages(languageCount);

            double averageLanguages =
                    LanguageProcessor.averageLanguagesPerRespondent(
                            respondents
                    );


            // --------------------------------------------------
            // 4. Display basic statistics
            // --------------------------------------------------

            System.out.println(
                    "\nAverage language per respondent: "
                            + averageLanguages
            );

            System.out.println("\nLanguage popularity:");

            int languageLimit =
                    Math.min(10, sortedLanguages.size());

            for (int i = 0; i < languageLimit; i++) {

                Map.Entry<String, Integer> entry =
                        sortedLanguages.get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + entry.getKey()
                                + " => "
                                + entry.getValue()
                );
            }


            // --------------------------------------------------
            // 5. Find common language pairs
            // --------------------------------------------------

            Map<String, LanguagePair> pairCount =
                    LanguageProcessor.countLanguagePairs(
                            respondents
                    );

            List<LanguagePair> sortedPairs =
                    LanguageProcessor.sortLanguagePairs(
                            pairCount
                    );


            // --------------------------------------------------
            // 6. Display top language pairs
            // --------------------------------------------------

            System.out.println("\nTop 10 language pairs:");

            int pairLimit =
                    Math.min(10, sortedPairs.size());

            for (int i = 0; i < pairLimit; i++) {

                LanguagePair pair =
                        sortedPairs.get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + pair.getPairName()
                                + " -> "
                                + pair.getCount()
                );
            }


            // --------------------------------------------------
            // 7. Find the best partner for each language
            // --------------------------------------------------

            Map<String, LanguagePartner> bestPartners =
                    LanguageProcessor.findBestPartners(
                            pairCount
                    );

            List<LanguagePartner> sortedPartners =
                    LanguageProcessor.sortBestPartners(
                            bestPartners
                    );

            List<LanguagePartner> uniquePartners = 
                    LanguageProcessor.removeDuplicatePartners(sortedPartners);


            // --------------------------------------------------
            // 8. Display top language partnerships
            // --------------------------------------------------

            System.out.println("\nMost common partner:");

            int partnerLimit =
                    Math.min(10, uniquePartners.size());

            for (int i = 0; i < partnerLimit; i++) {

                LanguagePartner partner =
                        uniquePartners.get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + partner.getLanguage()
                                + " -> "
                                + partner.getPartner()
                                + " ("
                                + partner.getCount()
                                + ")"
                );
            }


            // --------------------------------------------------
            // 9. Generate JSON report for dashboard
            // --------------------------------------------------

            ReportGenerator.generate(
                    "../dashboard/report.json",
                    respondents.size(),
                    averageLanguages,
                    sortedLanguages,
                    sortedPairs,
                    uniquePartners
            );


        } catch (IOException e) {

            System.out.println("Could not read the file!");
            System.out.println(e.getMessage());
        }
    }
}