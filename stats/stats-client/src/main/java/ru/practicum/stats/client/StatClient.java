package ru.practicum.stats.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.StatData;
import ru.practicum.stats.dto.StatResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatClient {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate restTemplate;

    @Value("${stat.application.name}")
    private String applicationName;

    @Value("${stat.server.url}")
    private String statServerUrl;

    public void createHit(HttpServletRequest request) {
        final String hitUrl = statServerUrl + "/hit";
        StatData statData = new StatData(applicationName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatData> requestEntity = new HttpEntity<>(statData, headers);
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(hitUrl, requestEntity, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Hit создался");
            } else {
                log.error("ошибка при создании hit, код ответа сервера {}", response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("ошибка при создании hit на сервере статистики " + e.getMessage(), e);
        }
    }

    public List<StatResponse> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String statisticUrl = statServerUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        HashMap<String, String> params = new HashMap<>();
        params.put("start", start.format(TIME_FORMATTER));
        params.put("end", end.format(TIME_FORMATTER));
        params.put("uris", uris == null ? "" : String.join(",", uris));
        params.put("unique", unique.toString());
        try {
            ResponseEntity<StatResponse[]> response = restTemplate.getForEntity(statisticUrl, StatResponse[].class, params);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            log.error("ошибка при получении статистики, код ответа сервера {}", response.getStatusCodeValue());
        } catch (Exception e) {
            log.error("ошибка при получении статистики " + e.getMessage(), e);
        }
        return Collections.EMPTY_LIST;
    }


}
