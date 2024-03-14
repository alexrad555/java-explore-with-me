package ru.practicum.ewm.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class EventSearchParam {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private String sort;
    private Integer from;
    private Integer size;
    private List<Long> users;
    private List<EventStatus> states;
    private List<Long> events;
    private boolean showPublishedOnly;

    public EventSearchParam(String text, List<Long> categories, Boolean paid,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                            boolean onlyAvailable, String sort, int from, int size, boolean showPublishedOnly) {
        this.text = text;
        this.categories = categories;
        this.paid = paid;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.onlyAvailable = onlyAvailable;
        this.sort = sort;
        this.from = from;
        this.size = size;
        this.showPublishedOnly = showPublishedOnly;
    }

    public EventSearchParam(List<Long> categories, LocalDateTime rangeStart,
                            LocalDateTime rangeEnd, int from, int size,
                            List<Long> users, List<EventStatus> states, boolean showPublishedOnly) {
        this.categories = categories;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.from = from;
        this.size = size;
        this.users = users;
        this.states = states;
        this.showPublishedOnly = showPublishedOnly;
    }

    public EventSearchParam(LocalDateTime rangeStart, int from, int size, List<Long> users, boolean showPublishedOnly) {
        this.rangeStart = rangeStart;
        this.from = from;
        this.size = size;
        this.users = users;
        this.showPublishedOnly = showPublishedOnly;
    }

    public EventSearchParam(List<Long> events, boolean showPublishedOnly) {
        this.rangeStart = LocalDateTime.MIN;
        this.events = events;
        this.showPublishedOnly = showPublishedOnly;
    }
}
