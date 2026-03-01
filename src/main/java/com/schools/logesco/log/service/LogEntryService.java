package com.schools.logesco.log.service;

import com.schools.logesco.common.EleveMapper;
import com.schools.logesco.eleve.dto.EleveDto;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.repository.EleveRepository;
import com.schools.logesco.log.dto.LogEntryDto;
import com.schools.logesco.log.entity.LogEntry;
import com.schools.logesco.log.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LogEntryService {

    private final LogEntryRepository logEntryRepository;
    private final EleveRepository eleveRepository;

    public void logEleveChanges(Eleve oldEleve, Eleve newEleve, String username) {

        Map<String, Object> oldValues = extractFields(oldEleve);
        Map<String, Object> newValues = extractFields(newEleve);

        for (String field : oldValues.keySet()) {

            Object oldVal = oldValues.get(field);
            Object newVal = newValues.get(field);

            if (!Objects.equals(oldVal, newVal)) {

                LogEntry log = LogEntry.builder()
                        .eleve(oldEleve)
                        .username(username)
                        .date(LocalDateTime.now())
                        .fieldValue(field)
                        .oldValue(oldVal != null ? oldVal.toString() : "")
                        .newValue(newVal != null ? newVal.toString() : "")
                        .build();

                logEntryRepository.save(log);
            }
        }
    }

    public Page<LogEntryDto> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return logEntryRepository.findAll(pageable)
                .map(log -> {
                    Eleve el = eleveRepository.findById(log.getId()).orElseThrow();
                    return toDto(log, EleveMapper.toDto(el));
                });
    }

    /**
     * Extrait tous les champs simples d’un élève
     * + les champs imbriqués (classe.nom, tuteur.telephone, etc.)
     */
    private Map<String, Object> extractFields(Eleve eleve) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : Eleve.class.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(eleve);

                // Champs simples
                if (isSimple(field.getType())) {
                    map.put(field.getName(), value);
                }

                // Champs objets (classe, tuteur…)
                else if (value != null) {
                    for (Field sub : value.getClass().getDeclaredFields()) {
                        sub.setAccessible(true);
                        Object subValue = sub.get(value);

                        if (isSimple(sub.getType())) {
                            map.put(field.getName() + "." + sub.getName(), subValue);
                        }
                    }
                }

            } catch (Exception ignored) {}
        }

        return map;
    }

    private boolean isSimple(Class<?> type) {
        return type.isPrimitive()
                || type.equals(String.class)
                || Number.class.isAssignableFrom(type)
                || type.equals(Boolean.class)
                || type.equals(LocalDate.class)
                || type.equals(LocalDateTime.class)
                || type.isEnum();
    }

    private LogEntryDto toDto(LogEntry entity, EleveDto eleveDto) {
        if (entity == null) return null;

        return new LogEntryDto(
                entity.getId(),
                entity.getOldValue(),
                entity.getUsername(),
                entity.getDate(),
                entity.getNewValue(),
                eleveDto,
                entity.getFieldValue()
        );
    }

}