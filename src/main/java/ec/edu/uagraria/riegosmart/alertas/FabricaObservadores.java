package ec.edu.uagraria.riegosmart.alertas;

/** FACTORY de observadores: el resto del sistema pide un canal, no sabe como se construye. */
public final class FabricaObservadores {

    private FabricaObservadores() { }

    public static ObservadorAlertas crear(Canal canal) {
        return switch (canal) { // exhaustivo: sin default
            case CONSOLA -> a -> System.out.println(
                    "[CONSOLA] " + a.tipo() + " " + a.parcela() + ": " + a.detalle());
            case SMS -> a -> System.out.println(
                    "[SMS] Administrador de riego: revisar " + a.parcela() + " (" + a.tipo() + ")");
        };
    }
}
