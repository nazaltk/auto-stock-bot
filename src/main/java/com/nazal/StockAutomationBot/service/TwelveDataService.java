package com.nazal.StockAutomationBot.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.num.DecimalNum;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TwelveDataService {

    @Value("${trading.api.key:ef6ff0472e03470e99a3ca31a6a944fe}")
    private String apiKey;

    @Value("${trading.interval:5min}")
    private String interval;

    @Value("${trading.outputsize:50}")
    private int outputSize;

    private static final String BASE_URL = "https://api.twelvedata.com/time_series";

    public BarSeries fetchTimeSeries(String symbol) throws Exception {
        String url = BASE_URL + "?symbol=" + symbol +
                "&interval=" + interval +
                "&outputsize=" + outputSize +
                "&apikey=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        String json = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode values = root.get("values");

        if (values == null) throw new RuntimeException("No data found for " + symbol);

        List<Bar> bars = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (JsonNode node : values) {
            ZonedDateTime time = LocalDateTime.parse(node.get("datetime").asText(), formatter)
                    .atZone(ZoneId.of("Asia/Kolkata"));
            Bar bar = BaseBar.builder()
                    .timePeriod(Duration.ofMinutes(5))
                    .endTime(time)
                    .openPrice(DecimalNum.valueOf(node.get("open").asDouble()))
                    .highPrice(DecimalNum.valueOf(node.get("high").asDouble()))
                    .lowPrice(DecimalNum.valueOf(node.get("low").asDouble()))
                    .closePrice(DecimalNum.valueOf(node.get("close").asDouble()))
                    .volume(DecimalNum.valueOf(node.get("volume").asDouble()))
                    .build();
            bars.add(bar);
        }
        return new BaseBarSeriesBuilder().withName("TCS").withBars(bars).build();
    }
}
