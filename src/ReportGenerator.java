import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public static void generate(
            String filePath,
            int respondentCount,
            double averageLanguages,
            List<Map.Entry<String, Integer>> sortedLanguages,
            List<LanguagePair> sortedPairs,
            List<LanguagePartner> sortedPartners) throws IOException {

        try (FileWriter writer = new FileWriter(filePath)) {

            // --------------------------------------------------
            // Start JSON object
            // --------------------------------------------------

            writer.write("{\n");


            // --------------------------------------------------
            // Basic statistics
            // --------------------------------------------------

            writer.write(
                    "    \"respondents\": "
                            + respondentCount
                            + ",\n");

            writer.write(
                    "    \"averageLanguages\": "
                            + averageLanguages
                            + ",\n");


            // --------------------------------------------------
            // Top languages
            // --------------------------------------------------

            writer.write("    \"topLanguages\": [\n");

            int languageLimit =
                    Math.min(10, sortedLanguages.size());

            for (int i = 0; i < languageLimit; i++) {

                Map.Entry<String, Integer> entry =
                        sortedLanguages.get(i);

                writer.write(
                        "        {\"name\": \""
                                + entry.getKey()
                                + "\", \"count\": "
                                + entry.getValue()
                                + "}");

                if (i < languageLimit - 1) {
                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("    ],\n");


            // --------------------------------------------------
            // Top language pairs
            // --------------------------------------------------

            writer.write("    \"topPairs\": [\n");

            int pairLimit =
                    Math.min(10, sortedPairs.size());

            for (int i = 0; i < pairLimit; i++) {

                LanguagePair pair =
                        sortedPairs.get(i);

                writer.write(
                        "        {\"pair\": \""
                                + pair.getPairName()
                                + "\", \"count\": "
                                + pair.getCount()
                                + "}");

                if (i < pairLimit - 1) {
                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("    ],\n");


            // --------------------------------------------------
            // Best language partners
            // --------------------------------------------------

            writer.write("    \"bestPartners\": [\n");

            int partnerLimit =
                    Math.min(10, sortedPartners.size());

            for (int i = 0; i < partnerLimit; i++) {

                LanguagePartner partner =
                        sortedPartners.get(i);

                writer.write(
                        "        {\"language\": \""
                                + partner.getLanguage()
                                + "\", \"partner\": \""
                                + partner.getPartner()
                                + "\", \"count\": "
                                + partner.getCount()
                                + "}");

                if (i < partnerLimit - 1) {
                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("    ]\n");


            // --------------------------------------------------
            // Close JSON object
            // --------------------------------------------------

            writer.write("}\n");
        }
    }
}