package ru.stonlex.api.java.mail;

import ru.stonlex.api.java.types.AbstractCacheManager;

public final class MailManager extends AbstractCacheManager<MailSender> {

    /**
     * Получение MailSender из кеша.
     *
     * Если его там нет, то он автоматически туда добавляется,
     * возвращая тот объект, что был добавлен туда.
     *
     * @param senderMail - email адрес отправителя
     * @param username - имя пользователя отправителя
     * @param password - пароль отправилеля
     * @param host - сервис отправления ('smtp.yandex.ru', 'smtp.gmail.com', 'smtp.mail.ru', и т.д.)
     */
    public MailSender getMailSender(String senderMail, String username, String password, String host) {
        return getComputeCache(senderMail, f -> new MailSender(senderMail, username, password, host));
    }

    /**
     * Отправить сообщение на почту
     *
     * @param subject - тема сообщения
     * @param content - содержимое сообщения
     * @param to - email адрес получателя
     */
    public void sendMessage(MailSender mailSender, String subject, String content, String to) {
        mailSender.sendMessage(subject, content, to);
    }

}
