package ec.edu.uagraria.riegosmart.fabrica;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.Especie;
import ec.edu.uagraria.riegosmart.dominio.InsumoRiego;
import ec.edu.uagraria.riegosmart.dominio.Inventario;
import ec.edu.uagraria.riegosmart.dominio.ProductoRiego;
import ec.edu.uagraria.riegosmart.dominio.TurnoRiego;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/** Prueba 10: la fabrica crea los tres tipos del dominio con su codigo. */
class FabricaProductosTest {

    private FabricaProductos fabrica;

    @BeforeEach
    void setUp() {
        fabrica = new FabricaProductos();
    }

    @Test
    @DisplayName("La fabrica crea un Cultivo con codigo CUL-0001")
    void creaCultivo() {
        ProductoRiego p = fabrica.crear(TipoProducto.CULTIVO,
                DatosProducto.cultivo("P-01", Especie.BANANO, 4.0, 1200.0));
        Cultivo c = assertInstanceOf(Cultivo.class, p);
        assertEquals("CUL-0001", c.codigo());
        assertEquals(1200.0, c.cuotaAguaM3(), 0.001);
    }

    @Test
    @DisplayName("La fabrica crea un TurnoRiego sin ejecutar")
    void creaTurno() {
        ProductoRiego p = fabrica.crear(TipoProducto.TURNO_RIEGO,
                DatosProducto.turno("P-02", LocalDateTime.of(2026, 11, 6, 7, 0), 60));
        TurnoRiego t = assertInstanceOf(TurnoRiego.class, p);
        assertEquals("TUR-0001", t.codigo());
        assertFalse(t.ejecutado());
        assertEquals(LocalDateTime.of(2026, 11, 6, 8, 0), t.fin());
    }

    @Test
    @DisplayName("La fabrica crea un InsumoRiego y calcula su costo")
    void creaInsumo() {
        ProductoRiego p = fabrica.crear(TipoProducto.INSUMO_RIEGO,
                DatosProducto.insumo("Cinta de goteo", "rollo", 6, 85.0));
        InsumoRiego i = assertInstanceOf(InsumoRiego.class, p);
        assertEquals("INS-0001", i.codigo());
        assertEquals(510.0, i.costo(), 0.001);
    }

    @Test
    @DisplayName("Cada tipo lleva su propia secuencia de codigos")
    void secuenciaPorTipo() {
        fabrica.crearCultivo(DatosProducto.cultivo("P-01", Especie.BANANO, 4.0, 1200.0));
        Cultivo segundo = fabrica.crearCultivo(DatosProducto.cultivo("P-02", Especie.CACAO, 3.0, 900.0));
        InsumoRiego primerInsumo = fabrica.crearInsumo(DatosProducto.insumo("Urea", "saco", 1, 30.0));
        assertEquals("CUL-0002", segundo.codigo());
        assertEquals("INS-0001", primerInsumo.codigo());
    }

    @Test
    @DisplayName("La fabrica rechaza datos invalidos del record")
    void rechazaDatosInvalidos() {
        assertThrows(IllegalArgumentException.class, () ->
                fabrica.crearCultivo(DatosProducto.cultivo("P-01", Especie.ARROZ, 0.0, 100.0)));
        assertThrows(IllegalArgumentException.class, () -> fabrica.crear(null, null));
    }

    @Test
    @DisplayName("El inventario clasifica los productos creados por la fabrica")
    void inventarioClasifica() {
        Inventario inv = new Inventario();
        inv.agregar(fabrica.crearCultivo(DatosProducto.cultivo("P-01", Especie.BANANO, 4.0, 1200.0)));
        inv.agregar(fabrica.crearInsumo(DatosProducto.insumo("Urea", "saco", 2, 30.0)));
        assertEquals(2, inv.tamanio());
        assertEquals(1, inv.cultivos().size());
        assertEquals("Insumo de riego", Inventario.tipoDe(inv.buscar("INS-0001").orElseThrow()));
    }
}
