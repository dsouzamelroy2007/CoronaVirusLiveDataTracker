package com.tracker.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tracker.app.model.ReportingLocationStatistics;

@Service
public class DataService {

	private static String CONFIRMED_CASES_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

	private List<ReportingLocationStatistics> allStats = new ArrayList<>();
	
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException {
		HttpClient client= HttpClient.newHttpClient();
		HttpRequest httpRequest=HttpRequest.newBuilder().uri(URI.create(CONFIRMED_CASES_URL))
				.build();
		
		HttpResponse<String> response=client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		//System.out.println(response.body());
		StringReader csvReader= new StringReader(response.body());
		 List<ReportingLocationStatistics> newStats = new ArrayList<>();
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {
            ReportingLocationStatistics locationStat = new ReportingLocationStatistics();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStat);
           // System.out.println(locationStat);
        }
        this.allStats = newStats;
	}
	
    public List<ReportingLocationStatistics> getAllStats() {
    	
         Collections.sort(allStats);
         return this.allStats;
    }
}
