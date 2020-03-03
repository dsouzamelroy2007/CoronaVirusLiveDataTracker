package com.tracker.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tracker.app.model.ReportingLocationStatistics;
import com.tracker.app.service.DataService;

import java.util.List;

@Controller
public class ResultsController {

    @Autowired
    DataService coronaVirusDataService;

    @GetMapping("/reportedCases")
    public String home(Model model) {
        List<ReportingLocationStatistics> allStats = coronaVirusDataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "ReportedCases";
    }
    
    @GetMapping("/curedCases")
    public String curedCases(Model model) {
        List<ReportingLocationStatistics> allStats = coronaVirusDataService.getCuredStats();
        int totalCuredCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalCuredCases", totalCuredCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "CuredCases";
    }
}
