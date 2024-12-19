package com.example.searchengine.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CourseRanker {

    // 自定義評價關鍵字與對應的加權分數
    // 從配置文件中讀取關鍵字和權重
    private Map<String, Integer> loadKeywordWeights() {
        Map<String, Integer> keywordWeights = new HashMap<>();
        keywordWeights.put("政大", 1);
        keywordWeights.put("課程", 3);
        keywordWeights.put("甜", 10);
        keywordWeights.put("涼", 8);
        keywordWeights.put("加簽", 100);
        keywordWeights.put("有趣", 7);
        keywordWeights.put("期末", 20);
        keywordWeights.put("老師", 20);
        keywordWeights.put("上課", 20);
        keywordWeights.put("期中", 20);
        keywordWeights.put("英文", 20);
        return keywordWeights;
    }

    private final Map<String, Integer> keywordWeights = loadKeywordWeights();


    public Map<String, Integer> rankKeywords(Map<String, String> results, List<String> keywords) {
        Map<String, Integer> rankedResults = new HashMap<>();
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String title = entry.getKey();
            String content = entry.getValue();

            // 先檢查標題，只有標題包含關鍵字才進一步檢查內文
            if (containsAnyKeyword(title, keywords)) {
                int score = calculateKeywordScore(title, content, keywords);
                rankedResults.put(title, score);
            }
        }

        return rankedResults.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // 降序排序
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    // 檢查文字是否包含任何關鍵字
    private boolean containsAnyKeyword(String text, List<String> keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true; // 只要有一個關鍵字匹配，就返回 true
            }
        }
        return false;
    }

    // 計算標題和內文的關鍵字分數
    private int calculateKeywordScore(String title, String content, List<String> keywords) {
        int score = 0;

        // 標題關鍵字加權處理
        for (String keyword : keywords) {
            if (title.contains(keyword)) {
                score += 5; // 標題中的關鍵字加分更高
            }
        }

        // 內文關鍵字計算
        for (String keyword : keywords) {
            score += content.split(keyword, -1).length - 1; // 計算內文中關鍵字的出現次數
        }

        // 特定關鍵字的加權 (應用於標題和內文)
        for (Map.Entry<String, Integer> entry : keywordWeights.entrySet()) {
            String evalKeyword = entry.getKey();
            int weight = entry.getValue();

            if (title.contains(evalKeyword)) {
                score += weight * 2; // 標題中的關鍵字權重更高
            }
            if (content.contains(evalKeyword)) {
                score += weight; // 內文中的關鍵字
            }
        }

        return score;
    }
}
