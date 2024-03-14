package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Setter
@Getter
@ToString
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "compilation_event_link",
                joinColumns = @JoinColumn(name = "compilation_id"),
                inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    private boolean pinned;

    private String title;
}
