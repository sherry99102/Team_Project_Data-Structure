package app;

import engine.SearchEngine;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 設定要抓取的目標網址
        List<String> urls = List.of("https://www.dcard.tw/f/nccu", "https://medium.com");

        // 設定關鍵字的權重
        Map<String, Double> weights = Map.of(
                "老師名", 2.0,
                "課程名稱", 1.5,
                "課程特色", 1.0
        );

        // 初始化搜尋引擎
        SearchEngine engine = new SearchEngine(urls, weights);

        // 使用者輸入的搜尋關鍵字
        List<String> searchKeywords = List.of("老師名", "課程名稱");

        // 執行搜尋並返回結果
        List<Map.Entry<String, Double>> results = engine.search(searchKeywords);

        // 輸出結果
        results.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}
