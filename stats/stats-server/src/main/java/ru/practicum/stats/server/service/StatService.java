package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.StatData;
import ru.practicum.stats.server.dao.ApplicationRepository;
import ru.practicum.stats.server.dao.HitRepository;
import ru.practicum.stats.server.dao.StatRepository;
import ru.practicum.stats.server.entity.Application;
import ru.practicum.stats.server.entity.Hit;
import ru.practicum.stats.server.exception.ValidationException;
import ru.practicum.stats.server.report.Statistics;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatService {

    private final StatRepository statRepository;
    private final ApplicationRepository applicationRepository;
    private final HitRepository hitRepository;

    private static final Map<String, Application> APPLICATION_CACHE = new HashMap<>();

    public List<Statistics> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Время начало позже времини окончания");
        }
        return statRepository.findAll(start, end, uris, unique);
    }

    public void createHit(StatData statData) {
        Application application = getApplicationByName(statData.getApp());
        Hit hit = new Hit();
        hit.setIp(statData.getIp());
        hit.setUri(statData.getUri());
        hit.setHitTime(statData.getTimestamp());
        hit.setApplication(application);
        hitRepository.save(hit);
    }

    private Application getApplicationByName(String applicationName) {
        Application application = APPLICATION_CACHE.get(applicationName);
        if (application != null) {
            return application;
        }

        Optional<Application> applicationOptional = applicationRepository.findByName(applicationName);
        if (applicationOptional.isPresent()) {
            application = applicationOptional.get();
        } else {
            application = new Application(applicationName);
            applicationRepository.save(application);
        }
        APPLICATION_CACHE.put(applicationName, application);
        return application;
    }
}
