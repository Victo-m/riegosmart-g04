package ec.edu.uagraria.riegosmart.fabrica;

import ec.edu.uagraria.riegosmart.dominio.Especie;

import java.time.LocalDateTime;

/**
 * Datos de entrada para la fabrica. Se construyen con los metodos estaticos,
 * uno por tipo, para que quien llama no tenga que recordar el orden de los campos.
 */
public record DatosProducto(String nombre, Especie especie, String unidad,
                            double cantidad, double valor, LocalDateTime inicio) {

    /** nombre = parcela, cantidad = hectareas, valor = cuota de agua en m3. */
    public static DatosProducto cultivo(String parcela, Especie especie,
                                        double hectareas, double cuotaAguaM3) {
        return new DatosProducto(parcela, especie, null, hectareas, cuotaAguaM3, null);
    }

    /** nombre = parcela, cantidad = duracion en minutos. */
    public static DatosProducto turno(String parcela, LocalDateTime inicio, int duracionMinutos) {
        return new DatosProducto(parcela, null, null, duracionMinutos, 0, inicio);
    }

    /** valor = precio unitario. */
    public static DatosProducto insumo(String nombre, String unidad,
                                       double cantidad, double precioUnitario) {
        return new DatosProducto(nombre, null, unidad, cantidad, precioUnitario, null);
    }
}
