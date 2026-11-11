package ec.edu.uagraria.riegosmart.alertas;

import java.time.LocalDateTime;

/** Evento que el motor publica a sus observadores. */
public record Alerta(TipoAlerta tipo, String parcela, String detalle, LocalDateTime momento) {

    public Alerta {
        if (tipo == null || parcela == null) {
            throw new IllegalArgumentException("La alerta necesita tipo y parcela");
        }
    }
}
