package ru.stonlex.api.java.mail;

import ru.stonlex.api.java.types.AbstractCacheManager;

public final class MailManager extends AbstractCacheManager<MailSender> {

    /**
     * Получение MailSender из кеша.
     *
     * Если его там нет, то он автоматически туда добавляется,
     * возвращая тот объект, что был добавлен туда.
     *
     * @param username - имя пользователя отправителя
     * @param password - пароль отправилеля
     */
    public MailSender getMailSender(String username, String password) {
        return getComputeCache(username, f -> new MailSender(username, username, password, "smtp.mail.ru"));
    }

    /**
     * Отправить сообщение на почту
     *
     * @param subject - тема сообщения
     * @param content - содержимое сообщения
     * @param toMail - email адрес получателя
     */
    public void sendMessage(MailSender mailSender, String subject, String content, String toMail) {
        mailSender.sendMessage(subject, content, toMail);
    }

}
