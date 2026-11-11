package ec.edu.uagraria.riegosmart.dominio;

import java.time.LocalDateTime;

/**
 * Turno programado para regar una parcela.
 *
 * @param codigo          codigo generado por la fabrica (TUR-0001)
 * @param parcela         parcela que debe regarse
 * @param inicio          fecha y hora de inicio programada
 * @param duracionMinutos duracion del turno
 * @param ejecutado       true si el turno ya se cumplio
 */
public record TurnoRiego(String codigo, String parcela, LocalDateTime inicio,
                         int duracionMinutos, boolean ejecutado) implements ProductoRiego {

    public TurnoRiego {
        if (inicio == null) {
            throw new IllegalArgumentException("El turno necesita fecha de inicio");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duracion debe ser mayor a cero");
        }
    }

    /** Momento en que el turno debia terminar. */
    public LocalDateTime fin() {
        return inicio.plusMinutes(duracionMinutos);
    }

    /** Devuelve una copia del turno marcada como ejecutada (el record es inmutable). */
    public TurnoRiego marcarEjecutado() {
        return new TurnoRiego(codigo, parcela, inicio, duracionMinutos, true);
    }

    @Override
    public String descripcion() {
        return "Turno " + parcela + " " + inicio + " (" + duracionMinutos + " min)";
    }
}
