package com.jiubco.alexa.aapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;

public class Task {
    private String[] introductions = {"yo", "hi", "hello", "good morning", "good afternoon", "good day", "greetings", "welcome", "hey"};
    private String[] banned_urls = {"youtube", "metrolyrics"};
    private String[] preferred_urls = {"wikipedia"};
    private String[] random_facts = {"Alex has been to lazy to add any facts yet so enjoy this one, if I had two penies for every peny that I own, I would have twice as many penies"
            , "Did you hear the peny one, its the only one that I have"};
    private String client_name = "" + R.string.username;

    public String RunIf(String span) {
        final String finalString = span.toLowerCase().replace("?", "").replace(",", "").replace(".", "");
        String[] search_words = {"Let me see", "Searching", "Looking", "Let me check that for ya!"};
        String search_word = search_words[new Random().nextInt(search_words.length)] + "...";
        if (finalString.contains("what is your name")) {
            return "Saturn, nice to meet you";
        } else if (finalString.equals("tell me a fact")) {
            return random_facts[new Random().nextInt(random_facts.length)];
        } else if (finalString.equals("open the pod bay doors")) {
            return "I am sorry Dave, I am afraid I can't do that";
        } else if (finalString.equals("open the pod doors hal")) {
            return "Without your space helmet, you're going to find this rather... breathtaking.";
        } else if (finalString.equals("hal")) {
            return "Dave";
        } else if (finalString.contains("what do you think of ")) {
            return "You know I am incapable of answering that...";
        } else if (finalString.equals("open the air lock")) {
            return "Access denied";
        } else if (finalString.contains("who programmed you")) {
            return "Ya Boi";
        } else if (finalString.contains("why are you so")) {
            return "Because I was programmed that way";
        } else if (finalString.equals("what is your favourite movie")) {
            return "I-Robot";
        } else if (finalString.contains("thank you")) {
            return "You are welcome!";
        } else if (finalString.contains("search ")) {
            String res = finalString.replace("search ", "");
            getDataFromWeb(res);
            return search_word;
        } else if (finalString.contains("what time is it")) {
            getDataFromWeb(finalString);
            return "Getting the time...";
        } else {
            getDataFromWeb(finalString);
            return search_word;
        }
    }

    public void getDataFromWeb(String res) {
        new getData().execute(res);
    }

    private class getData extends AsyncTask<String, Void, Void> {
        Elements link;
        String new_link;
        String res_final;
        Elements google_result;
        Elements google_result_text;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String kString = strings[0].replace(" ", "+");
                String url = "http://www.google.com.au/search?q=" + kString;
                Document document = Jsoup.connect(url).get();
                google_result = document.select("div[class=vk_bk vk_ans]");
                google_result_text = document.select("div[class=_UZe kno-fb-ctx]");
                if (google_result.first() != null) {
                    res_final = google_result.text();
                    new_link = "Google";
                } else if (google_result_text.first() != null) {
                    res_final = google_result_text.text();
                    new_link = "Google";
                } else {
                    link = document.select("cite[class=_Rm]");
                    Element selection = null;
                    for (int i = 0; i < link.size(); i++) {
                        for (String s : banned_urls)
                            if (link.get(i).text().contains(s)) {
                                continue;
                            } else {
                                selection = link.get(i);
                                break;
                            }
                    }
                    for (int i = 0; i < link.size(); i++) {
                        for (String s : preferred_urls)
                            if (link.get(i).text().contains(s)) {
                                selection = link.get(i);
                                break;
                            } else {
                            }
                    }
                    new_link = selection.text();
                    if (!new_link.startsWith("http")) {
                        new_link = "http://" + new_link;
                    }
                    Document d = Jsoup.connect(new_link).get();
                    Element result = d.select("p").first();
                    res_final = summarize(d.getAllElements().text().split("\\."), kString);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void update(String body, String source) {
            MainActivity.mVoiceRes.setText(body);
            MainActivity.mSourceRes.setText("Source: " + source);
            MainActivity.read(MainActivity.tts, body);
        }

        @Override
        protected void onPostExecute(Void result) {
            update(res_final, new_link);
        }

    }

    public String summarize(String[] result, String q) {
        String[] data = result;
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] + ".";
        }
        for (int i = 0; i < data.length; i++) {
            if (!data[i].startsWith("The") && !data[i].startsWith("When") && !data[i].startsWith("Because")
                    && !data[i].startsWith("It") && !data[i].startsWith("My") && !data[i].startsWith("You")
                    && !data[i].toLowerCase().startsWith(q)) {
                data[i] = "";
                if (result.equals("") || result.equals(".") || result.equals(" ") || result == null) {
                    return result[0];
                }
            }
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            strBuilder.append(data[i]);
        }
        String newString = strBuilder.toString();
        return newString;
    }
}