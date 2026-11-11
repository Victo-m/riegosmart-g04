package ec.edu.uagraria.riegosmart.reglas;

/** Estrategias de costo escritas como lambdas (adaptadas del Taller 3). */
public final class Estrategias {

    private Estrategias() { }

    /** Suma el costo de todos los insumos. */
    public static EstrategiaCosto costoDirecto() {
        return (cultivo, insumos) -> insumos.stream()
                .mapToDouble(i -> i.costo())
                .sum();
    }

    /** Costo directo dividido para la superficie del cultivo. */
    public static EstrategiaCosto costoPorHectarea() {
        return (cultivo, insumos) -> costoDirecto().calcular(cultivo, insumos) / cultivo.hectareas();
    }

    /** Envuelve otra estrategia y le suma el IVA recibido por parametro. */
    public static EstrategiaCosto conIva(EstrategiaCosto base, ParametrosCosto parametros) {
        return (cultivo, insumos) -> base.calcular(cultivo, insumos) * (1 + parametros.tasaIva());
    }
}
