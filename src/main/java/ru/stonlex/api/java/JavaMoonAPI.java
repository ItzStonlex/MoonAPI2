package ru.stonlex.api.java;

import lombok.Getter;
import ru.stonlex.api.java.logger.MoonLogger;
import ru.stonlex.api.java.mail.MailManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class JavaMoonAPI {

    @Getter
    private static final MailManager mailManager           = new MailManager();

    @Getter
    private static final MoonLogger moonLogger             = new MoonLogger();

    public static void main(String[] args) {
        moonLogger.info("Test message 1");
    }


    private static final ExecutorService CACHED_POOL_THREAD = Executors.newCachedThreadPool();

    /**
     * Асинхронное выполнение команды
     */
    public static void async(Runnable command) {
        CACHED_POOL_THREAD.submit(command);
    }

}
