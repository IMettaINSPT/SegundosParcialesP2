
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Modelo m = new Modelo();
        Vista v = new Vista();
        Controlador c = new Controlador(v, m);

        c.iniciar();

    }

}
