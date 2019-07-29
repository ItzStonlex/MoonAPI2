package ru.stonlex.api.java;

import lombok.Getter;
import ru.stonlex.api.java.logger.MoonLogger;
import ru.stonlex.api.java.mail.MailManager;
import ru.stonlex.api.java.schedulers.SchedulerManager;

public final class JavaMoonAPI {

    @Getter
    private static final MailManager mailManager           = new MailManager();

    @Getter
    private static final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private static final MoonLogger moonLogger             = new MoonLogger();


    public static void main(String[] args) {
        moonLogger.info("Test message 1");
    }

}
