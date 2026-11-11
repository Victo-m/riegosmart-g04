package ec.edu.uagraria.riegosmart.dominio;

/**
 * Cultivo sembrado en una parcela, con la cuota de agua que tiene asignada.
 *
 * @param codigo      codigo generado por la fabrica (CUL-0001)
 * @param parcela     identificador de la parcela (P-01)
 * @param especie     banano, cacao o arroz
 * @param hectareas   superficie sembrada
 * @param cuotaAguaM3 volumen de agua asignado para el periodo, en m3
 */
public record Cultivo(String codigo, String parcela, Especie especie,
                      double hectareas, double cuotaAguaM3) implements ProductoRiego {

    public Cultivo {
        if (hectareas <= 0) {
            throw new IllegalArgumentException("Las hectareas deben ser mayores a cero");
        }
        if (cuotaAguaM3 < 0) {
            throw new IllegalArgumentException("La cuota de agua no puede ser negativa");
        }
    }

    @Override
    public String descripcion() {
        return especie + " en " + parcela + " (" + hectareas + " ha)";
    }
}
