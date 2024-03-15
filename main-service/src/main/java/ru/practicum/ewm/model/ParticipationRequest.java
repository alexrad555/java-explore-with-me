package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participant_requests")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    private Event event;

    @ManyToOne
    private User requester;

    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;

    public ParticipationRequest(Event event, User requester) {
        this.event = event;
        this.requester = requester;
    }
}
