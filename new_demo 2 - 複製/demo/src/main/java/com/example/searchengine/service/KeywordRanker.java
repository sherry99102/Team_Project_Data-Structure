package com.example.searchengine.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class KeywordRanker {

    public Map<String, Integer> rankKeywords(Map<String, String> results, List<String> keywords) {
        Map<String, Integer> rankedResults = new HashMap<>();
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String title = entry.getKey();
            int score = calculateKeywordScore(title, keywords);
            rankedResults.put(title, score);
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

    private int calculateKeywordScore(String text, List<String> keywords) {
        int score = 0;
        for (String keyword : keywords) {
            score += text.split(keyword, -1).length - 1; // 計算關鍵字出現次數
        }
        return score;
    }
}
