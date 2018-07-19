package com.ef.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
@Data
public class CliParameters {

    private String accesslog = "";
    private LocalDateTime startDate;
    private DurationType duration;
    private int threshold;
}