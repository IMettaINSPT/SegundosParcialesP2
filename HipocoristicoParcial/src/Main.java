public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Vista v = new Vista();
        Modelo m =new Modelo();
        Controlador c = new Controlador(v,m);
        c.iniciar();
    }
    
}
