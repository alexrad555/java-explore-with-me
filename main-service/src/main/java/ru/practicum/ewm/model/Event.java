package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Setter
@Getter
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private BigDecimal lat;

    @Column(nullable = false)
    private BigDecimal lon;

    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.NEW;

    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

}
