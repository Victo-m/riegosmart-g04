package ec.edu.uagraria.riegosmart;

import ec.edu.uagraria.riegosmart.alertas.Alerta;
import ec.edu.uagraria.riegosmart.alertas.Canal;
import ec.edu.uagraria.riegosmart.alertas.ConfiguracionRiego;
import ec.edu.uagraria.riegosmart.alertas.FabricaObservadores;
import ec.edu.uagraria.riegosmart.alertas.LecturaParcela;
import ec.edu.uagraria.riegosmart.alertas.MotorAlertas;
import ec.edu.uagraria.riegosmart.alertas.ReglasAlerta;
import ec.edu.uagraria.riegosmart.dominio.Cultivo;
import ec.edu.uagraria.riegosmart.dominio.Especie;
import ec.edu.uagraria.riegosmart.dominio.Inventario;
import ec.edu.uagraria.riegosmart.dominio.TurnoRiego;
import ec.edu.uagraria.riegosmart.fabrica.DatosProducto;
import ec.edu.uagraria.riegosmart.fabrica.FabricaProductos;
import ec.edu.uagraria.riegosmart.reglas.CalculadoraCostos;
import ec.edu.uagraria.riegosmart.reglas.Estrategias;
import ec.edu.uagraria.riegosmart.reglas.ParametrosCosto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Demostracion del Taller 4: fabrica + inventario + motor de alertas + estrategias. */
public class DemoRiegoSmart {

    public static void main(String[] args) {
        FabricaProductos fabrica = new FabricaProductos();
        Inventario inventario = new Inventario();

        // 1. Todos los objetos del dominio salen de la fabrica (ningun new disperso)
        Cultivo banano = fabrica.crearCultivo(DatosProducto.cultivo("P-01", Especie.BANANO, 4.0, 1200.0));
        Cultivo cacao = fabrica.crearCultivo(DatosProducto.cultivo("P-02", Especie.CACAO, 3.0, 900.0));
        Cultivo arroz = fabrica.crearCultivo(DatosProducto.cultivo("P-03", Especie.ARROZ, 5.0, 2500.0));
        LocalDateTime ahora = LocalDateTime.of(2026, 11, 6, 10, 0);
        TurnoRiego turnoP02 = fabrica.crearTurno(DatosProducto.turno("P-02", ahora.minusHours(3), 60));
        inventario.agregar(banano);
        inventario.agregar(cacao);
        inventario.agregar(arroz);
        inventario.agregar(turnoP02);
        inventario.agregar(fabrica.crearInsumo(DatosProducto.insumo("Cinta de goteo", "rollo", 6, 85.00)));
        inventario.agregar(fabrica.crearInsumo(DatosProducto.insumo("Fertilizante soluble", "saco", 10, 42.50)));

        System.out.println("== Inventario creado por la fabrica ==");
        inventario.todos().forEach(p ->
                System.out.println(p.codigo() + "  " + Inventario.tipoDe(p) + "  " + p.descripcion()));

        // 2. Strategy del Taller 3 sobre los insumos del inventario
        CalculadoraCostos calc = new CalculadoraCostos(
                Estrategias.conIva(Estrategias.costoDirecto(), ParametrosCosto.ECUADOR));
        double costo = calc.calcular(banano, inventario.insumos()).orElseThrow();
        System.out.println("Costo de insumos con IVA para " + banano.parcela() + ": $"
                + CalculadoraCostos.redondear(costo));

        // 3. Motor de alertas con tres reglas y tres observadores
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(ReglasAlerta.humedadSueloBaja());
        motor.agregarRegla(ReglasAlerta.turnoVencido());
        motor.agregarRegla(ReglasAlerta.cuotaExcedida());

        List<Alerta> bitacora = new ArrayList<>();
        motor.suscribir(FabricaObservadores.crear(Canal.CONSOLA));
        motor.suscribir(FabricaObservadores.crear(Canal.SMS));
        motor.suscribir(bitacora::add); // observador con referencia a metodo

        System.out.println("\n== Motor de alertas ==");
        motor.procesar(new LecturaParcela(banano, 22.0, 800.0, null, ahora));      // seca
        motor.procesar(new LecturaParcela(cacao, 41.0, 600.0, turnoP02, ahora));   // turno vencido
        motor.procesar(new LecturaParcela(arroz, 55.0, 2730.0, null, ahora));      // cuota excedida
        motor.procesar(new LecturaParcela(banano, 35.0, 900.0, null, ahora));      // normal

        ConfiguracionRiego.INSTANCIA.cambiarUmbralHumedadMinima(40.0);
        System.out.println("-- umbral de humedad cambiado a 40 % en la configuracion --");
        motor.procesar(new LecturaParcela(banano, 35.0, 900.0, null, ahora));      // ahora si alerta

        System.out.println("Alertas en bitacora: " + bitacora.size());
    }
}
