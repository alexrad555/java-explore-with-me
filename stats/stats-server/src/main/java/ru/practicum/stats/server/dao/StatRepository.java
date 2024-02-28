package ru.practicum.stats.server.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.server.report.Statistics;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class StatRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String STAT_QUERY =
            "select a.name as app, h.uri as uri, count(%s) as hits " +
            "  from hits h " +
            "  join applications a on a.id = h.app_id " +
            " where h.hit_time >= :timeFrom and h.hit_time < :timeTo";
    private static final String STAT_QUERY_URI = STAT_QUERY + " and uri in (:uris) ";
    private static final String COUNT_UNIQUE = "distinct ip";
    private static final String COUNT_ALL = "*";
    private static final String GROUP_BY = " group by a.name, h.uri";

    private static final BeanPropertyRowMapper<Statistics> rowMapper = new BeanPropertyRowMapper<>(Statistics.class);

    public List<Statistics> findAll(LocalDateTime timeFrom, LocalDateTime timeTo, List<String> uris, boolean unique) {

        boolean useUris = uris != null && !uris.isEmpty();

        Map<String, Object> params = new HashMap<>();
        params.put("timeFrom", timeFrom);
        params.put("timeTo", timeTo);
        if (useUris) {
            params.put("uris", uris);
        }

        final String count = unique ? COUNT_UNIQUE : COUNT_ALL;
        String query = String.format(useUris ? STAT_QUERY_URI : STAT_QUERY, count) + GROUP_BY;

        return jdbcTemplate.query(query, params, rowMapper);
    }

    public List<Statistics> findAll(LocalDateTime timeFrom, LocalDateTime timeTo, boolean unique) {
        return findAll(timeFrom, timeTo, null, unique);
    }
}