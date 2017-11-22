package com.jiubco.alexa.aapp;

import android.os.AsyncTask;

import com.jiubco.alexa.aapp.SummarizeElements.Sentence;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Task {

    public static String[] data;

    private String[] introductions = {"yo", "hi", "hello", "good morning", "good afternoon", "good day", "greetings", "welcome", "hey"};
    private String[] banned_urls = {"youtube", "pinterest"};
    private String[] preferred_urls = {"wikipedia"};
    private String[] random_facts = {"Alex has been to lazy to add any facts yet so enjoy this one, if I had two pennies for every peny that I own, I would have twice as many penies"
            , "Did you hear the penny one, its the only one that I have"};
    private String[] keywords = {"The", "When", "Because", "Make"};

    public String RunIf(String span) {
        final String finalString = span.toLowerCase().replace("?", "").replace(",", "").replace(".", "").trim();
        String[] search_words = {"Let me see", "Searching", "Looking", "Let me check that for ya!"};
        String search_word = search_words[new Random().nextInt(search_words.length)] + "...";
        if (finalString.contains("what is your name")) {
            return "Saturn, nice to meet you";
        } else if (finalString.contains("sing ")) {
            String fin = finalString.replace("sing ", "") + " lyrics";
            getDataFromWeb(fin);
            return "Learning song...";
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
                    data = d.getAllElements().text().split("\\.");
                    res_final = summarize(data, kString);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void update(String body) {
            MainActivity.mVoiceRes.setText(body);
            MainActivity.read(MainActivity.tts, body);
        }

        @Override
        protected void onPostExecute(Void result) {
            update(res_final);
        }

    }

    double findCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0.0;
        for (String str1Word : str1.s.split("\\s+")) {
            for (String str2Word : str2.s.split("\\s+")) {
                if (str1Word.compareToIgnoreCase(str2Word) == 0) {
                    commonCount++;
                }
            }
        }
        return commonCount;
    }

    public String findMostCommonWord(String[] list) throws InterruptedException {
        int counter = 0;
        String mostFrequentWord = "";
        for (String streamed : list) {
            if (streamed.equals(mostFrequentWord)) {
                counter++;
            } else if (counter == 0) {
                mostFrequentWord = streamed;
                counter = 1;
            } else {
                counter--;
            }
        }
        return mostFrequentWord;
    }

    public String summarize(String[] res, String q) { //Res is the body ArrayList and q is the query.
        ArrayList<String> ar = new ArrayList<>();
        ArrayList<String> summary = new ArrayList<>();
        for (String si : res) {
            if (si.contains(q)) {
                summary.add(si);
            }
        }
//        for (int i = 0; i < res.length; i++) {
//            if (!res[i].contains(q)) {
//                res[i] = "";
//            } else {
//                ar.add(res[i]);
//            }
//        }
//
//        String finalRes = ar.toString().replace("[", "").replace("]", "").replace(",", "");
//        return finalRes;
        String result = summary.toString().replace("[", "").replace("]", "").replace(",", "");
        return result;
    }
}