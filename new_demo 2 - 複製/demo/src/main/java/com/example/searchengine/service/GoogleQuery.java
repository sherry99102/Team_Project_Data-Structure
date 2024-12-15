package com.example.searchengine.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleQuery {

    @Autowired
    private Crawler crawler;

    @Autowired
    private HTMLHandler htmlHandler;

    public Map<String, String> fetchResults(String query) throws IOException {
        String url = "https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8")+"&num=20";
        System.out.println("Query URL: " + url);

        // 抓取内容
        String content = crawler.fetchContent(url);
        if (content == null || content.isEmpty()) {
            System.err.println("Failed to fetch content for URL: " + url);
            return new HashMap<>();
        }

        // 解析内容
        Map<String, String> results = htmlHandler.parseResults(content);
        if (results.isEmpty()) {
            System.err.println("No results parsed from content.");
        }

        return results;
    }
}

