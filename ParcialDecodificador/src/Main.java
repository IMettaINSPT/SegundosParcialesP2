
public class Main {

    public static void main(String[] args) {
        // TODO code application logic here
        Vista v = new Vista();
        Modelo m = new Modelo();
        Controlador c =new Controlador(v,m);
        c.iniciar();
        
    }

}
