package com.ef.domain;

import com.ef.domain.converter.LocalDateTimeConverter;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
@Data
@Entity
public class AccessLog {

    @Id
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime date;
    private String ip;
    private String request;
    private int status;
    private String userAgent;
}