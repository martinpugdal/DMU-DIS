package ScreenScraping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLTest {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://valutakurser.dk");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        StringBuilder content = new StringBuilder();

        while ((line = br.readLine()) != null) {
            content.append(line);
        }
        br.close();

        Pattern usdPattern = Pattern.compile("\\{\"currencyName\":\"Amerikanske dollar\",\"symbol\":\"USD\".*?\"actualValue\":(\\d+\\.\\d+)");
        Matcher usdMatcher = usdPattern.matcher(content.toString());

        if (usdMatcher.find()) {
            double usdToDkkRate = Double.parseDouble(usdMatcher.group(1)) / 100.0;
            System.out.println("USD to DKK exchange rate: " + usdToDkkRate);
        } else {
            System.out.println("USD to DKK exchange rate not found.");
        }

        //System.out.println(content);
    }
}

