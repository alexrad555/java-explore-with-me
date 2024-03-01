package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StatResponse {
    private final String app;
    private final String uri;
    private final Long hits;

    @JsonCreator
    public StatResponse(
            @JsonProperty("app") String app,
            @JsonProperty("uri") String uri,
            @JsonProperty("hits") Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
