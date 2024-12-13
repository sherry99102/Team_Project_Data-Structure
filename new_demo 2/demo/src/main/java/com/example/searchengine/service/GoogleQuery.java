package com.example.searchengine.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleQuery {
    // 改成public以符合老師的程式碼
    public String searchKeyword;
    public String url;
    public String content;

    public GoogleQuery(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        try {
            // 特別處理中文關鍵字的編碼
            String encodeKeyword = java.net.URLEncoder.encode(searchKeyword, "utf-8");
            this.url = "https://www.google.com/search?q=" + encodeKeyword + "&oe=utf8&num=20";

            // 這一行可以取消註解以查看沒有編碼的效果
            // this.url = "https://www.google.com/search?q=" + searchKeyword + "&oe=utf8&num=20";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
//
    private String fetchContent() throws IOException {
        String retVal = ""; // 修改為String類型，以符合老師的寫法

        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        // 設置 HTTP header
        conn.setRequestProperty("User-agent", "Chrome/107.0.5304.107");
        InputStream in = conn.getInputStream();

        InputStreamReader inReader = new InputStreamReader(in, "utf-8");
        BufferedReader bufReader = new BufferedReader(inReader);
        String line;

        while ((line = bufReader.readLine()) != null) {
            retVal += line;
        }
        return retVal;
    }

    public HashMap<String, String> query() throws IOException {
        if (content == null) {
            content = fetchContent();
        }

        HashMap<String, String> retVal = new HashMap<>();

        /*
         * 參考一些Jsoup文檔的資源
         * https://jsoup.org/apidocs/org/jsoup/nodes/package-summary.html
         * https://www.1ju.org/jsoup/jsoup-quick-start
         */

        // 使用Jsoup分析HTML字符串
        Document doc = Jsoup.parse(content);

        // 選擇特定的元素（標籤）以進行處理
        Elements lis = doc.select("div").select(".kCrYT");

        for (Element li : lis) {
            try {
                String citeUrl = li.select("a").get(0).attr("href").replace("/url?q=", "");
                String title = li.select("a").get(0).select(".vvjwJb").text();

                if (title.equals("")) {
                    continue; // 若標題為空則跳過
                }

                // 輸出結果至console，方便查看每次查詢的結果
                System.out.println("Title: " + title + " , url: " + citeUrl);

                // 將標題和網址加入HashMap
                retVal.put(title, citeUrl);

            } catch (IndexOutOfBoundsException e) {
                // 略過該錯誤，不輸出
            }
        }
        return retVal;
    }
}
