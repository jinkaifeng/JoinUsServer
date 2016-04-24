package com.northgatecode.joinus.services;

/**
 * Created by qianliang on 24/4/2016.
 */
public class ForumService {
    public static int getLeveByScore(int score) {
        int level = 1;
        int scoreCap = 20;
        while (score > scoreCap) {
            level++;
            scoreCap += level * 20;
        }
        return level;
    }
}
