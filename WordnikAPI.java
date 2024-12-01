package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WordnikAPI {
    private static final String API_KEY = "your_wordnik_api_key";

    // Fetch a list of random words from Wordnik
    public static String[] fetchWordList(int limit) throws Exception {
        String url = "https://api.wordnik.com/v4/words.json/randomWords?limit=" + limit + "&api_key=" + API_KEY;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Extract words from the response JSON
        JSONArray jsonArray = new JSONArray(response.toString());
        String[] words = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject wordObject = jsonArray.getJSONObject(i);
            words[i] = wordObject.getString("word");
        }
        return words;
    }

    // Fetch a definition for a specific word from Wordnik
    public static String fetchDefinition(String word) throws Exception {
        String url = "https://api.wordnik.com/v4/word.json/" + word + "/definitions?limit=1&api_key=" + API_KEY;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Parse the response JSON to extract the definition
        JSONArray jsonArray = new JSONArray(response.toString());
        if (jsonArray.length() > 0) {
            JSONObject definitionObject = jsonArray.getJSONObject(0);
            return definitionObject.getString("text");
        } else {
            return "Definition not found.";
        }
    }
}
