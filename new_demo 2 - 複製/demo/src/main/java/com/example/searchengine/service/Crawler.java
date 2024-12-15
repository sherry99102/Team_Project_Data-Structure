package com.example.searchengine.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class Crawler {
    public String fetchContent(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                            .timeout(5000)
                            .get();
        System.out.println("Fetched HTML Content: \n" + doc.html()); // 打印抓取的 HTML
        return doc.html();
    }
}

