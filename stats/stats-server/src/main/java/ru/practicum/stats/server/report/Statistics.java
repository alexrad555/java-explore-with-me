package ru.practicum.stats.server.report;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Statistics {
    private String app;
    private String uri;
    private int hits;
}
