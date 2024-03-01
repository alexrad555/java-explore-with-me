package ru.practicum.stats.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uri;
    private String ip;
    private LocalDateTime hitTime;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private Application application;

}
