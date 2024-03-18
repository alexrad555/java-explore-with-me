package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Setter
@Getter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private Event event;

    private LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime editDate;

    private LocalDateTime deletionDate;

    private LocalDateTime publishDate;

    private String message;

}
