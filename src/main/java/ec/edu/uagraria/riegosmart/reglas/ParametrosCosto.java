package ec.edu.uagraria.riegosmart.reglas;

/**
 * Valores de referencia de los calculos de costo, fuera de las formulas.
 * Antes estaban escritos como constantes dentro de Estrategias.
 */
public record ParametrosCosto(double tasaIva) {

    /** IVA vigente en Ecuador (15 %). */
    public static final ParametrosCosto ECUADOR = new ParametrosCosto(0.15);

    public ParametrosCosto {
        if (tasaIva < 0 || tasaIva >= 1) {
            throw new IllegalArgumentException("Tasa de IVA fuera de rango");
        }
    }
}
