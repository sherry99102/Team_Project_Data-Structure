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
    private final Map<String, Integer> keywordWeights = Map.of(
        "政大", 5,
        "課程", 3,
        "甜", 10,
        "涼", 8,
        "加簽", 6,
        "有趣", 7,
        "實用", 8
    );

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

        // 計算一般關鍵字的出現次數
        for (String keyword : keywords) {
            score += text.split(keyword, -1).length - 1; // 計算關鍵字出現次數
        }

        // 計算特定評價關鍵字的加權分數
        for (Map.Entry<String, Integer> entry : keywordWeights.entrySet()) {
            String evalKeyword = entry.getKey();
            int weight = entry.getValue();
            if (text.contains(evalKeyword)) {
                score += weight; // 加入權重
            }
        }

        return score;
    }
}
