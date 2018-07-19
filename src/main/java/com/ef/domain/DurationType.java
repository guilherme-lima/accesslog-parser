package com.ef.domain;

import java.time.LocalDateTime;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
public enum DurationType {
    hourly {
        @Override
        public LocalDateTime getEndDateTime(LocalDateTime startDateTime) {
            return startDateTime.plusHours(1).minusNanos(1);
        }
    },

    daily {
        @Override
        public LocalDateTime getEndDateTime(LocalDateTime startDateTime) {
            return startDateTime.plusDays(1).minusNanos(1);
        }
    };

    public abstract LocalDateTime getEndDateTime(LocalDateTime startDateTime);
}