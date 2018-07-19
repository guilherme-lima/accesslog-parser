package com.ef.usecase;

import com.ef.domain.AccessLog;
import com.ef.domain.CliParameters;
import com.ef.domain.DurationType;
import com.ef.domain.BlockedIp;
import com.ef.repository.AccessLogRepository;
import com.ef.repository.BlockedIpRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
@Component
@AllArgsConstructor
public class ReadFile {

    private AccessLogRepository accessLogRepository;
    private BlockedIpRepository blockedIpRepository;

    public void execute(String...args) throws DateTimeParseException, IOException {
        CliParameters cliParameters = getCliParameters(args);
        LocalDateTime endDate = cliParameters.getDuration().getEndDateTime(cliParameters.getStartDate());
        try (Stream<String> stream = Files.lines(Paths.get(cliParameters.getAccesslog() + "access.log"))) {
            stream.parallel().map(this::extractLineInfo).filter(Objects::nonNull).forEach(accessLogRepository::save);
            List<AccessLog> accessList = accessLogRepository.findAllByDateBetween(cliParameters.getStartDate(), endDate);
            List<String> ipsToBlock = accessList
                    .stream()
                    .collect(Collectors.groupingBy(AccessLog::getIp, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(x -> x.getValue() > cliParameters.getThreshold())
                    .map(Map.Entry::getKey).collect(Collectors.toList());
            ipsToBlock.parallelStream().forEach(ip -> {
                blockIp(ip, cliParameters);
                System.out.println(ip + ": " + blockIp(ip, cliParameters).getComment());
            });
        }
    }

    private CliParameters getCliParameters(String...args) throws DateTimeParseException {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        final String equal = "=";
        CliParameters cliParameters = new CliParameters();
        for (String arg : args)
            switch (arg.split(equal)[0]) {
                case "--accesslog":
                    cliParameters.setAccesslog(arg.split(equal)[1]);
                    break;
                case "--startDate":
                    cliParameters.setStartDate(LocalDateTime.parse(arg.split(equal)[1], dateTimeFormatter));
                    break;
                case "--duration":
                    cliParameters.setDuration(DurationType.valueOf(arg.split(equal)[1]));
                    break;
                case "--threshold":
                    cliParameters.setThreshold(Integer.parseInt(arg.split(equal)[1]));
            }
        return cliParameters;
    }

    private AccessLog extractLineInfo(String line) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        final String delimiter = "\\|";
        final String[] columnArr = line.split(delimiter);
        try {
            AccessLog accessLog = new AccessLog();
            accessLog.setDate(LocalDateTime.parse(columnArr[0], dateTimeFormatter));
            accessLog.setIp(columnArr[1]);
            accessLog.setRequest(columnArr[2].replace("\"", ""));
            accessLog.setStatus(Integer.parseInt(columnArr[3]));
            accessLog.setUserAgent(columnArr[4].replace("\"", ""));
            return accessLog;
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private BlockedIp blockIp(String ip, CliParameters cliParameters) {
        Object[] args = {
                cliParameters.getThreshold(),
                Timestamp.valueOf(cliParameters.getStartDate()),
                Timestamp.valueOf(cliParameters.getDuration().getEndDateTime(cliParameters.getStartDate()))
        };
        MessageFormat messageFormat = new MessageFormat("More than {0} requests between {1} and {2}.");
        messageFormat.format(args);
        BlockedIp blockedIp = new BlockedIp();
        blockedIp.setIp(ip);
        blockedIp.setComment(messageFormat.format(args));
        blockedIp.setDate(LocalDateTime.now());
        return blockedIpRepository.save(blockedIp);
    }
}