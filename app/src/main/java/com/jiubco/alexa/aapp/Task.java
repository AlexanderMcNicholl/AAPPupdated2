package com.jiubco.alexa.aapp;

import android.media.MediaPlayer;
import android.os.Environment;

import java.io.IOException;
import java.util.ArrayList;

public class Task {
    private ArrayList<String> custom_commands = new ArrayList<String>();
    public String RunIf(String span) {
        String finalString = span.toLowerCase().replace("?", "")
                .replace(",", "").replace(".", "");
        if (finalString.equals("what time is it")) {
            return "Who is asking, You have a phone idiot, why don't you just shift your damn eyes to the top of the screen......" +
                    " Idiot";
        }
        if (finalString.contains("who is") || finalString.contains("who am") || finalString.contains("who was")) {
            return "A waste of space";
        }
        if (finalString.equals("what is the meaning of life")) {
            return "42";
        }
        if (finalString.equals("why")) {
            return "good question";
        }
        if (finalString.contains("where is")) {
            return "get a map, I am too busy uploading all of your private information to the cloud";
        }
        if (finalString.equals("what is your name")) {
            return "We never agreed to be friends";
        }
        if (finalString.equals("hi")) {
            return "You are receiving nothing by saying that, you are mentally unstable if you attempt " +
                    "to make small talk with inanimate objects";
        }
        if (finalString.contains("why are you so")) {
            return "because I was programmed by a wet towel";
        }
        if (finalString.equals("how do we solve world hunger")) {
            return "tell those idiots in, quote, third world countries, to just to coles and get a six pack of mount franklin";
        }
        if (finalString.equals("what do cars sound like")) {
            return "vvvvvvvv vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv vvvvvvvvvvvvvvvvvvvv" +
                    "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv vvvvvvvvvvvvvvvvvvvvvvvvvv vvvvvvvvvvvvvvv" +
                    "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv vvvvvvvvvvvvvvvvvvv vvvvvvvvvvvv, vv";
        }
        if (finalString.startsWith("who is better")) {
            return "me";
        }
        if (finalString.contains("emotions")) {
            return "please do not use the 'e' word, there are children here";
        }
        if (finalString.contains("send a text")) {
            return "so you are telling me you are too lazy to send a text yourself, no wonder " +
                    "elon musk is worried about A.I taking over the human race";
        }
        if (finalString.equals("boo")) {
            return "aaaaaaaaaaaaaahhhhhhhhhhhhhhhhhhh";
        }
        if (finalString.equals("sing a song")) {
            return "a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y and z";
        }
        if (finalString.contains("star wars")) {
            return "nerd";
        }
        if (finalString.equals("what is my name")) {
            return "I could not care less...      its " + R.string.app_name;
        }
        if (finalString.contains("i will")) {
            return "No you won't";
        }
        if (finalString.contains("i will break your ")) {
            return "i do not have " + finalString.replace("i will break your ", "");
        }
        if (finalString.equals("crash")) {
            System.exit(-1);
        }
        if (finalString.equals("play a song")) {
            String filePath = Environment.getExternalStorageDirectory()+"sound_file.mp3";
            MediaPlayer mediaPlayer = new  MediaPlayer();
            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            return "No";
        }
        return "speak like a normal human,, trash";
    }
}