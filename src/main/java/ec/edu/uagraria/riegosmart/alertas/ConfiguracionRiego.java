package ec.edu.uagraria.riegosmart.alertas;

/**
 * SINGLETON: unica configuracion de umbrales para todo el sistema.
 * Ninguna regla tiene numeros escritos: todas leen de aqui.
 */
public enum ConfiguracionRiego {
    INSTANCIA;

    public static final double HUMEDAD_MINIMA_POR_DEFECTO = 30.0;     // % de humedad del suelo
    public static final int TOLERANCIA_TURNO_POR_DEFECTO = 30;         // minutos
    public static final double PORCENTAJE_CUOTA_POR_DEFECTO = 100.0;  // % de la cuota asignada

    private double umbralHumedadMinima = HUMEDAD_MINIMA_POR_DEFECTO;
    private int toleranciaTurnoMinutos = TOLERANCIA_TURNO_POR_DEFECTO;
    private double porcentajeCuotaMaxima = PORCENTAJE_CUOTA_POR_DEFECTO;

    public double umbralHumedadMinima() { return umbralHumedadMinima; }

    public int toleranciaTurnoMinutos() { return toleranciaTurnoMinutos; }

    public double porcentajeCuotaMaxima() { return porcentajeCuotaMaxima; }

    public void cambiarUmbralHumedadMinima(double nuevo) {
        if (nuevo <= 0 || nuevo >= 100) {
            throw new IllegalArgumentException("Umbral de humedad fuera de rango");
        }
        this.umbralHumedadMinima = nuevo;
    }

    public void cambiarToleranciaTurnoMinutos(int nueva) {
        if (nueva < 0) {
            throw new IllegalArgumentException("La tolerancia no puede ser negativa");
        }
        this.toleranciaTurnoMinutos = nueva;
    }

    public void cambiarPorcentajeCuotaMaxima(double nuevo) {
        if (nuevo <= 0) {
            throw new IllegalArgumentException("El porcentaje de cuota debe ser positivo");
        }
        this.porcentajeCuotaMaxima = nuevo;
    }

    /** Vuelve a los valores por defecto (lo usan las pruebas para no contaminarse). */
    public void restablecer() {
        umbralHumedadMinima = HUMEDAD_MINIMA_POR_DEFECTO;
        toleranciaTurnoMinutos = TOLERANCIA_TURNO_POR_DEFECTO;
        porcentajeCuotaMaxima = PORCENTAJE_CUOTA_POR_DEFECTO;
    }
}
