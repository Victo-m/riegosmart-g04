package ec.edu.uagraria.riegosmart.alertas;

import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.Especie;
import ec.edu.uagraria.riegosmart.dominio.TurnoRiego;
import ec.edu.uagraria.riegosmart.fabrica.DatosProducto;
import ec.edu.uagraria.riegosmart.fabrica.FabricaProductos;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas 11 y 12: las tres alertas (disparo y caso limite) y el motor con sus observadores. */
class MotorAlertasTest {

    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 6, 10, 0);
    private static final ConfiguracionRiego CONFIG = ConfiguracionRiego.INSTANCIA;

    private FabricaProductos fabrica;
    private Cultivo banano; // cuota 1200 m3

    @BeforeEach
    void setUp() {
        CONFIG.restablecer();
        fabrica = new FabricaProductos();
        banano = fabrica.crearCultivo(DatosProducto.cultivo("P-01", Especie.BANANO, 4.0, 1200.0));
    }

    @AfterEach
    void tearDown() {
        CONFIG.restablecer(); // el Singleton es compartido: no dejar cambios a la siguiente prueba
    }

    private LecturaParcela lectura(double humedad, double consumo, TurnoRiego turno, LocalDateTime momento) {
        return new LecturaParcela(banano, humedad, consumo, turno, momento);
    }

    private TurnoRiego turnoDeUnaHora(LocalDateTime inicio) {
        return fabrica.crearTurno(DatosProducto.turno("P-01", inicio, 60));
    }

    // ---------- HUMEDAD_SUELO_BAJA ----------

    @Test
    @DisplayName("Humedad baja se dispara por debajo del umbral")
    void humedadBajaDispara() {
        var alerta = ReglasAlerta.humedadSueloBaja().evaluar(lectura(29.9, 0, null, AHORA));
        assertTrue(alerta.isPresent());
        assertEquals(TipoAlerta.HUMEDAD_SUELO_BAJA, alerta.get().tipo());
    }

    @Test
    @DisplayName("Humedad baja no se dispara en el umbral exacto")
    void humedadBajaNoDisparaEnElUmbral() {
        assertTrue(ReglasAlerta.humedadSueloBaja().evaluar(lectura(30.0, 0, null, AHORA)).isEmpty());
    }

    @Test
    @DisplayName("Cambiar el umbral en el Singleton cambia la regla sin tocarla")
    void humedadBajaRespetaUmbralConfigurable() {
        CONFIG.cambiarUmbralHumedadMinima(40.0);
        assertTrue(ReglasAlerta.humedadSueloBaja().evaluar(lectura(35.0, 0, null, AHORA)).isPresent());
    }

    // ---------- TURNO_VENCIDO ----------

    @Test
    @DisplayName("Turno vencido se dispara pasada la tolerancia")
    void turnoVencidoDispara() {
        // turno 07:00-08:00, tolerancia 30 min -> limite 08:30; lectura 08:31
        TurnoRiego turno = turnoDeUnaHora(LocalDateTime.of(2026, 11, 6, 7, 0));
        var alerta = ReglasAlerta.turnoVencido()
                .evaluar(lectura(50, 0, turno, LocalDateTime.of(2026, 11, 6, 8, 31)));
        assertEquals(TipoAlerta.TURNO_VENCIDO, alerta.orElseThrow().tipo());
    }

    @Test
    @DisplayName("Turno vencido no se dispara en el limite exacto")
    void turnoVencidoNoDisparaEnElLimite() {
        TurnoRiego turno = turnoDeUnaHora(LocalDateTime.of(2026, 11, 6, 7, 0));
        assertTrue(ReglasAlerta.turnoVencido()
                .evaluar(lectura(50, 0, turno, LocalDateTime.of(2026, 11, 6, 8, 30))).isEmpty());
    }

    @Test
    @DisplayName("Un turno ya ejecutado o inexistente no genera alerta")
    void turnoEjecutadoNoDispara() {
        TurnoRiego turno = turnoDeUnaHora(LocalDateTime.of(2026, 11, 6, 7, 0)).marcarEjecutado();
        assertTrue(ReglasAlerta.turnoVencido().evaluar(lectura(50, 0, turno, AHORA)).isEmpty());
        assertTrue(ReglasAlerta.turnoVencido().evaluar(lectura(50, 0, null, AHORA)).isEmpty());
    }

    // ---------- CUOTA_EXCEDIDA ----------

    @Test
    @DisplayName("Cuota excedida se dispara cuando el consumo supera la cuota")
    void cuotaExcedidaDispara() {
        var alerta = ReglasAlerta.cuotaExcedida().evaluar(lectura(50, 1200.5, null, AHORA));
        assertEquals(TipoAlerta.CUOTA_EXCEDIDA, alerta.orElseThrow().tipo());
    }

    @Test
    @DisplayName("Cuota excedida no se dispara en la cuota exacta")
    void cuotaExcedidaNoDisparaEnElUmbral() {
        assertTrue(ReglasAlerta.cuotaExcedida().evaluar(lectura(50, 1200.0, null, AHORA)).isEmpty());
    }

    // ---------- Motor y observadores ----------

    @Test
    @DisplayName("El motor notifica a todos los observadores")
    void motorNotificaATodos() {
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(ReglasAlerta.humedadSueloBaja());
        List<Alerta> a = new ArrayList<>(), b = new ArrayList<>();
        motor.suscribir(a::add);
        motor.suscribir(b::add);
        assertEquals(1, motor.procesar(lectura(10.0, 0, null, AHORA)));
        assertEquals(1, a.size());
        assertEquals(1, b.size());
    }

    @Test
    @DisplayName("Una lectura puede disparar las tres alertas a la vez")
    void motorConTresReglas() {
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(ReglasAlerta.humedadSueloBaja());
        motor.agregarRegla(ReglasAlerta.turnoVencido());
        motor.agregarRegla(ReglasAlerta.cuotaExcedida());
        List<Alerta> bitacora = new ArrayList<>();
        motor.suscribir(bitacora::add);
        TurnoRiego turno = turnoDeUnaHora(LocalDateTime.of(2026, 11, 6, 7, 0));
        assertEquals(3, motor.procesar(lectura(10.0, 1500.0, turno, AHORA)));
        assertEquals(List.of(TipoAlerta.HUMEDAD_SUELO_BAJA, TipoAlerta.TURNO_VENCIDO,
                TipoAlerta.CUOTA_EXCEDIDA), bitacora.stream().map(Alerta::tipo).toList());
    }

    @Test
    @DisplayName("Sin alertas el motor no notifica")
    void motorSinAlertas() {
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(ReglasAlerta.humedadSueloBaja());
        List<Alerta> bitacora = new ArrayList<>();
        motor.suscribir(bitacora::add);
        assertEquals(0, motor.procesar(lectura(45.0, 0, null, AHORA)));
        assertTrue(bitacora.isEmpty());
    }

    @Test
    @DisplayName("La configuracion rechaza umbrales fuera de rango")
    void configuracionValidaRangos() {
        assertThrows(IllegalArgumentException.class, () -> CONFIG.cambiarUmbralHumedadMinima(0));
        assertThrows(IllegalArgumentException.class, () -> CONFIG.cambiarUmbralHumedadMinima(100));
        assertThrows(IllegalArgumentException.class, () -> CONFIG.cambiarToleranciaTurnoMinutos(-1));
        assertThrows(IllegalArgumentException.class, () -> CONFIG.cambiarPorcentajeCuotaMaxima(0));
    }

    @Test
    @DisplayName("La fabrica de observadores crea los canales de consola y SMS")
    void fabricaObservadores() {
        for (Canal canal : Canal.values()) {
            assertNotNull(FabricaObservadores.crear(canal));
        }
    }
}
