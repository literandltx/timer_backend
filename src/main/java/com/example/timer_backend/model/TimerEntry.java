package com.example.timer_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timer_entries")
public class TimerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Label label;

    @Column(name = "duration_seconds", nullable = false)
    private Long durationSeconds;

    @Column(name = "start_time", nullable = false)
    private Long startTime;
}
