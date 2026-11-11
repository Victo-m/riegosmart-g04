package ec.edu.uagraria.riegosmart.dominio;

/** Jerarquia sellada: el inventario de RiegoSmart solo tiene estos tres tipos. */
public sealed interface ProductoRiego extends Trazable permits Cultivo, TurnoRiego, InsumoRiego {

    /** Nombre corto para mostrar en reportes. */
    String descripcion();
}
