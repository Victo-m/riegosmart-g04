package ec.edu.uagraria.riegosmart.fabrica;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.InsumoRiego;
import ec.edu.uagraria.riegosmart.dominio.ProductoRiego;
import ec.edu.uagraria.riegosmart.dominio.Trazable;
import ec.edu.uagraria.riegosmart.dominio.TurnoRiego;

import java.util.EnumMap;
import java.util.Map;

/**
 * FACTORY: unico lugar del proyecto donde se hace new sobre los records del dominio.
 * Lleva una secuencia por tipo para generar los codigos.
 */
public class FabricaProductos {

    private final Map<TipoProducto, Integer> secuencias = new EnumMap<>(TipoProducto.class);

    public ProductoRiego crear(TipoProducto tipo, DatosProducto datos) {
        if (tipo == null || datos == null) {
            throw new IllegalArgumentException("Tipo y datos son obligatorios");
        }
        String codigo = Trazable.generarCodigo(tipo.prefijo(), siguiente(tipo));
        return switch (tipo) { // exhaustivo: sin default
            case CULTIVO -> new Cultivo(codigo, datos.nombre(), datos.especie(),
                    datos.cantidad(), datos.valor());
            case TURNO_RIEGO -> new TurnoRiego(codigo, datos.nombre(), datos.inicio(),
                    (int) datos.cantidad(), false);
            case INSUMO_RIEGO -> new InsumoRiego(codigo, datos.nombre(), datos.unidad(),
                    datos.cantidad(), datos.valor());
        };
    }

    /** Atajos tipados para no tener que hacer cast en quien llama. */
    public Cultivo crearCultivo(DatosProducto datos) {
        return (Cultivo) crear(TipoProducto.CULTIVO, datos);
    }

    public TurnoRiego crearTurno(DatosProducto datos) {
        return (TurnoRiego) crear(TipoProducto.TURNO_RIEGO, datos);
    }

    public InsumoRiego crearInsumo(DatosProducto datos) {
        return (InsumoRiego) crear(TipoProducto.INSUMO_RIEGO, datos);
    }

    private int siguiente(TipoProducto tipo) {
        return secuencias.merge(tipo, 1, Integer::sum);
    }
}
