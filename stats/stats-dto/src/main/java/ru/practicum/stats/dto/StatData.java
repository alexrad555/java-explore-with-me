package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@ToString
public class StatData {
    @NotNull
    private final String app;
    @NotNull
    private final String uri;
    @NotNull
    private final String ip;
    @NotNull
    private final LocalDateTime timestamp;

    public StatData(String app,
                    String uri,
                    String ip,
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
