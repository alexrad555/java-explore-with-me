package ru.practicum.ewm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.model.EventSearchParam;
import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.model.ParticipationRequestStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class EventSearchRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_BEG =
            "select e.*, c.name category_name, u.name user_name " +
            "  from ( " +
                "select e.id, e.category_id, e.annotation, e.event_date, e.user_id, e.paid, e.title, e.create_date, " +
                "       e.lat, e.lon, e.description, e.participant_limit, e.publish_date, e.request_moderation, " +
                "       count(r.*) confirmed_requests, e.status " +
                "  from events e " +
                "  left join participant_requests r on r.event_id = e.id and r.status = :request_status " +
                " where 1=1 ";

    private static final String AND_TEXT = " and (lower(e.annotation) like :text or " +
            "lower(e.description) like :text)";
    private static final String AND_CATEGORIES = " and e.category_id in (:categories) ";
    private static final String AND_USERS = " and e.user_id in (:users) ";
    private static final String AND_STATUSES = " and e.status in (:statuses) ";
    private static final String AND_RANGE_START = " and e.event_date >= :rangeStart ";
    private static final String AND_RANGE_END = " and e.event_date < :rangeEnd ";
    private static final String AND_PAID = " and e.paid = :paid ";
    private static final String AND_EVENTS = " and e.id in (:events) ";

    private static final String GROUP_BY = " group by e.id";
    private static final String HAVING = " having e.participant_limit > count(r.*)";

    private static final String OFFSET_LIMIT = " offset :offset fetch first :limit rows only";

    private static final String QUERY_END = ") e";

    private static final String JOIN_CATEGORY = " join categories c on c.id = e.category_id ";
    private static final String JOIN_USERS = " join users u on u.id = e.user_id ";

    private static final String ORDER_BY = " order by e.event_date desc";

    public List<EventFullDto> findAll(EventSearchParam searchParam) {

        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("request_status", ParticipationRequestStatus.CONFIRMED.name());

        sb.append(QUERY_BEG);

        LocalDateTime dateFrom =
                searchParam.getRangeStart() == null && searchParam.getRangeEnd() == null ?
                LocalDateTime.now().plusSeconds(1) : searchParam.getRangeStart();

        if (dateFrom != null) {
            params.put("rangeStart", dateFrom);
            sb.append(AND_RANGE_START);
        }

        if (searchParam.getRangeEnd() != null) {
            params.put("rangeEnd", searchParam.getRangeEnd());
            sb.append(AND_RANGE_END);
        }
        if (searchParam.getText() != null && !searchParam.getText().isEmpty()) {
            params.put("text", "%" + searchParam.getText().toLowerCase() + "%");
            sb.append(AND_TEXT);
        }
        if (searchParam.getCategories() != null && !searchParam.getCategories().isEmpty()) {
            params.put("categories", searchParam.getCategories());
            sb.append(AND_CATEGORIES);
        }
        if (searchParam.getPaid() != null) {
            params.put("paid", searchParam.getPaid());
            sb.append(AND_PAID);
        }
        if (searchParam.getUsers() != null && !searchParam.getUsers().isEmpty()) {
            params.put("users", searchParam.getUsers());
            sb.append(AND_USERS);
        }
        if (searchParam.getEvents() != null && !searchParam.getEvents().isEmpty()) {
            params.put("events", searchParam.getEvents());
            sb.append(AND_EVENTS);
        }
        if (searchParam.getStates() != null && !searchParam.getStates().isEmpty()) {
            params.put("statuses", searchParam.getStates().stream().map(Enum::name).collect(Collectors.toList()));
            sb.append(AND_STATUSES);
        } else if (searchParam.isShowPublishedOnly()) {
            params.put("statuses", List.of(EventStatus.PUBLISHED.name()));
            sb.append(AND_STATUSES);
        }
        sb.append(GROUP_BY);

        if (searchParam.isOnlyAvailable()) {
            sb.append(HAVING);
        }
        if (searchParam.getFrom() != null && searchParam.getSize() != null) {
            params.put("offset", searchParam.getFrom());
            params.put("limit", searchParam.getSize());
            sb.append(OFFSET_LIMIT);
        }

        sb.append(QUERY_END);
        sb.append(JOIN_CATEGORY);
        sb.append(JOIN_USERS);
        sb.append(ORDER_BY);

        final String query = sb.toString();

        return jdbcTemplate.query(query, params, this::mapRow);
    }

    public Optional<EventFullDto> findById(Long id, boolean showPublishedOnly) {

        StringBuilder sb = new StringBuilder();
        sb.append(QUERY_BEG);
        sb.append(" and e.id = :id ");

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("request_status", ParticipationRequestStatus.CONFIRMED.name());
        if (showPublishedOnly) {
            sb.append(AND_STATUSES);
            params.put("statuses", List.of(EventStatus.PUBLISHED.name()));
        }
        sb.append(GROUP_BY);
        sb.append(QUERY_END);
        sb.append(JOIN_CATEGORY);
        sb.append(JOIN_USERS);
        String query = sb.toString();

        return jdbcTemplate.query(query, params, this::mapRow).stream().findFirst();
    }

    private EventFullDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryDto category = new CategoryDto(
                rs.getLong("category_id"),
                rs.getString("category_name"));

        UserShortDto user = new UserShortDto(
                rs.getLong("user_id"),
                rs.getString("user_name"));

        LocationDto location = new LocationDto(
                rs.getBigDecimal("lat"),
                rs.getBigDecimal("lon"));

        Timestamp timestamp = rs.getTimestamp("publish_date");
        LocalDateTime publishDate = timestamp ==null ? null : timestamp.toLocalDateTime();

        return new EventFullDto(category,
                rs.getString("annotation"),
                rs.getLong("confirmed_requests"),
                rs.getTimestamp("event_date").toLocalDateTime(),
                rs.getLong("id"),
                user,
                rs.getBoolean("paid"),
                rs.getString("title"),
                0L,
                rs.getTimestamp("create_date").toLocalDateTime(),
                location,
                rs.getString("description"),
                rs.getInt("participant_limit"),
                publishDate,
                rs.getBoolean("request_moderation"),
                EventStatus.valueOf(rs.getString("status")));
    }
}