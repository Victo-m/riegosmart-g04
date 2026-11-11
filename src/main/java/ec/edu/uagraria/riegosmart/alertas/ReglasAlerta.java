package ec.edu.uagraria.riegosmart.alertas;

import ec.edu.uagraria.riegosmart.dominio.TurnoRiego;

import java.time.LocalDateTime;
import java.util.Optional;

/** Las tres reglas del tema G04, cada una como lambda. Los umbrales vienen del Singleton. */
public final class ReglasAlerta {

    private static final ConfiguracionRiego CONFIG = ConfiguracionRiego.INSTANCIA;

    private ReglasAlerta() { }

    /** HUMEDAD_SUELO_BAJA: la humedad medida es menor que el umbral minimo. */
    public static ReglaAlerta humedadSueloBaja() {
        return lectura -> {
            double minimo = CONFIG.umbralHumedadMinima();
            if (lectura.humedadSuelo() < minimo) {
                return Optional.of(new Alerta(TipoAlerta.HUMEDAD_SUELO_BAJA, lectura.parcela(),
                        "humedad " + lectura.humedadSuelo() + " % < " + minimo + " %",
                        lectura.momento()));
            }
            return Optional.empty();
        };
    }

    /** TURNO_VENCIDO: el turno no se ejecuto y ya paso su fin mas la tolerancia. */
    public static ReglaAlerta turnoVencido() {
        return lectura -> {
            TurnoRiego turno = lectura.turno();
            if (turno == null || turno.ejecutado()) {
                return Optional.empty();
            }
            LocalDateTime limite = turno.fin().plusMinutes(CONFIG.toleranciaTurnoMinutos());
            if (lectura.momento().isAfter(limite)) {
                return Optional.of(new Alerta(TipoAlerta.TURNO_VENCIDO, lectura.parcela(),
                        "turno " + turno.codigo() + " sin ejecutar; limite " + limite,
                        lectura.momento()));
            }
            return Optional.empty();
        };
    }

    /** CUOTA_EXCEDIDA: el consumo de agua supera el porcentaje permitido de la cuota. */
    public static ReglaAlerta cuotaExcedida() {
        return lectura -> {
            double permitido = lectura.cultivo().cuotaAguaM3() * CONFIG.porcentajeCuotaMaxima() / 100.0;
            if (lectura.consumoAguaM3() > permitido) {
                return Optional.of(new Alerta(TipoAlerta.CUOTA_EXCEDIDA, lectura.parcela(),
                        "consumo " + lectura.consumoAguaM3() + " m3 > " + permitido + " m3",
                        lectura.momento()));
            }
            return Optional.empty();
        };
    }
}
