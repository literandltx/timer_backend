package com.example.timer_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timer_settings")
public class TimerSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "active_option_id",  nullable = false, unique = true)
    private TimerOption preference;

    @Column(name = "last_updated", nullable = false)
    private Long lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = Instant.now().toEpochMilli();
    }
}
