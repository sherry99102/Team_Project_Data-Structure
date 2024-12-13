package com.example.searchengine.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.searchengine.service.GoogleQuery;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam String studentId, Model model) {
        // 根據學號尾數選擇關鍵字
        String query = getKeywordByStudentId(studentId);

        GoogleQuery googleQuery = new GoogleQuery(query);
        try {
            HashMap<String, String> results = googleQuery.query();
            model.addAttribute("results", results);
            model.addAttribute("query", query);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error fetching results. Please try again later.");
        }
        return "index";
    }

    private String getKeywordByStudentId(String studentId) {
        char lastDigit = studentId.charAt(studentId.length() - 1);
        switch (lastDigit) {
            case '0': case '1': return "Tomato";
            case '2': case '3': return "Liver";
            case '4': case '5': return "Pokemon";
            case '6': case '7': return "Tissue";
            case '8': case '9': return "Process";
            default: return "Default"; // 預設關鍵字
        }
    }
}
