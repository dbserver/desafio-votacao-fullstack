package br.com.jardelkuhn.backend.global.handler;

import br.com.jardelkuhn.backend.global.dto.ServerErrorDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoggingHandler {
    private static LoggingHandler instance;

    public static LoggingHandler getInstance() {
        if (instance == null) {
            instance = new LoggingHandler();
        }

        return instance;
    }

    public ServerErrorDTO handleException(Exception e) {
        // gerar um código de erro que o usuário/suporte possa rastrear
        String errorCode = UUID.randomUUID().toString();
        LocalDateTime errorDateTime = LocalDateTime.now();

        // ideal gravar em SYSLOG ou alguma ferramenta dedicada
        System.out.println(errorCode + ";" + errorDateTime + ";");
        e.printStackTrace();

        return new ServerErrorDTO(errorCode, errorDateTime, "Ocorreu um erro interno, por favor tente novamente ou contate o suporte informando o código: #" + errorCode);
    }
}
