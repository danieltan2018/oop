package oop.project;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class PortnetConnector {

    private static String apiKey;

    @Value("${portnet.apikey}")
    public void setApiKey(String value) {
        PortnetConnector.apiKey = value;
    }

    public static void getUpdate(String dateFrom, String dateTo) {
        String url = "https://api.pntestbox.com/vsspp/pp/bizfn/berthingSchedule/retrieveByBerthingDate/v1.2";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Apikey", apiKey);

        Map<String, Object> map = new HashMap<>();
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            JsonArray vesselArray = (JsonArray) jsonObject.get("results").getAsJsonArray();
            Database.insert(vesselArray);
        }
    }

    @Scheduled(cron = "${portnet.cron.advance}")
    public static void dailyTask() {
        getUpdate(LocalDate.now().plusDays(1).toString(), LocalDate.now().plusDays(7).toString());
    }

    @Scheduled(cron = "${portnet.cron.current}")
    public static void hourlyTask() {
        getUpdate(LocalDate.now().toString(), LocalDate.now().toString());
    }
}