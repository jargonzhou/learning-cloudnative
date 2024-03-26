package com.spike.vertx.congrats;

import com.spike.vertx.commons.Constants;
import io.vertx.ext.mail.MailConfig;

public class MailerConfig {
    public static MailConfig config() {
        return new MailConfig()
                .setHostname(Constants.CongratsService.MAIL_HOSTNAME)
                .setPort(Constants.CongratsService.MAIL_PORT);
    }
}
