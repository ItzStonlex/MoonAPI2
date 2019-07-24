package ru.stonlex.api.java;

import lombok.Getter;
import ru.stonlex.api.java.mail.MailManager;
import ru.stonlex.api.java.schedulers.SchedulerManager;

public final class JavaMoonAPI {

    @Getter
    private static final MailManager mailManager           = new MailManager();

    @Getter
    private static final SchedulerManager schedulerManager = new SchedulerManager();

    public static void main(String[] args) {
        //logic
    }

}
