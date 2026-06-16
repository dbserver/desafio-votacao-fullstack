package com.votacao.backend.infrastructure.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public final class AppDateTime {

    public static final ZoneId BRAZIL_ZONE = ZoneId.of("America/Sao_Paulo");

    static {
        TimeZone.setDefault(TimeZone.getTimeZone(BRAZIL_ZONE));
        System.setProperty("user.timezone", BRAZIL_ZONE.getId());
    }

    private AppDateTime() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(BRAZIL_ZONE);
    }
}
