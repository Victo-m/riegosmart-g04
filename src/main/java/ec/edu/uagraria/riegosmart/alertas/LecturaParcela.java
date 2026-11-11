package ec.edu.uagraria.riegosmart.alertas;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.TurnoRiego;

import java.time.LocalDateTime;

/**
 * Lo que el motor revisa en cada ciclo: el estado de una parcela en un momento dado.
 *
 * @param cultivo        cultivo de la parcela (de aqui sale la cuota de agua)
 * @param humedadSuelo   humedad volumetrica del suelo medida por el sensor, en %
 * @param consumoAguaM3  agua consumida en el periodo, en m3
 * @param turno          ultimo turno programado (puede ser null si no hay)
 * @param momento        fecha y hora de la lectura
 */
public record LecturaParcela(Cultivo cultivo, double humedadSuelo, double consumoAguaM3,
                             TurnoRiego turno, LocalDateTime momento) {

    public LecturaParcela {
        if (cultivo == null || momento == null) {
            throw new IllegalArgumentException("La lectura necesita cultivo y momento");
        }
        if (humedadSuelo < 0 || humedadSuelo > 100) {
            throw new IllegalArgumentException("Humedad fuera de 0-100 %");
        }
        if (consumoAguaM3 < 0) {
            throw new IllegalArgumentException("El consumo no puede ser negativo");
        }
    }

    public String parcela() {
        return cultivo.parcela();
    }
}
