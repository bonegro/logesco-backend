package com.schools.logesco.log.entity;

import com.schools.logesco.eleve.entity.Eleve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_value", nullable = false)
    private String oldValue;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "new_value", nullable = false)
    private String newValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @Column(name = "field_value", nullable = false)
    private String fieldValue;
}
