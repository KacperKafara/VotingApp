package pl.kafara.voting.vote.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUtils {

    public static List<String> extractPrints(String title) {
        List<String> prints = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\bdruki? nr ([\\dA-Za-z\\-, ]+)");
        Matcher matcher = pattern.matcher(title);

        if(matcher.find()) {
            String match = matcher.group(1);
            String[] parts = match.split("[, ]+");
            for (String part : parts) {
                if (!part.equals("i") && !part.isEmpty()) {
                    prints.add(part.trim());
                }
            }
        }
        return prints;
    }
}
