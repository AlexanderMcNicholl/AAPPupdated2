package com.jiubco.alexa.aapp.SummarizeElements;

public class Sentence {

    public int score;
    public String s;
    public String[] words;

    public Sentence(String s, int score) {
        this.score = score;
        this.s = s;
        words = s.split("\\s+");
    }
}