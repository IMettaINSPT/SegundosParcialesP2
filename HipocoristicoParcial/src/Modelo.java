
import java.sql.*;
import java.util.ArrayList;

public class Modelo {

    private class Equivalencias {

        private String equivalencia1;
        private String equivalencia2;

        public Equivalencias(String eq1, String eq2) {
            this.equivalencia1 = eq1;
            this.equivalencia2 = eq2;
        }

        public String getEquivalencia1() {
            return equivalencia1;
        }

        public String getEquivalencia2() {
            return equivalencia2;
        }

    }

    private String usr, psw, tabla, bd, driver, ip, prefijoConexion;
    private Connection conexion;
    private ArrayList<Equivalencias> equivalenciasList = new ArrayList<>();

    public Modelo() {
        driver = "com.mysql.cj.jdbc.Driver";
        prefijoConexion = "jdbc:mysql://";
        ip = "127.0.0.1";
        bd = "hipocoristicos";
        tabla = "equivalencias";
        usr = "";
        psw = "";
        conexion = obtenerConexion();
        System.out.println("Me conecteee");

    }

    public boolean CompararPersonas(String persona1, String persona2) {
        boolean sonEquivalentes = true;
        this.getEquivalenciasList();
        String[] personas1v = persona1.split(" ");
        String[] personas2v = persona2.split(" ");
        if (personas1v.length != personas2v.length) {
            System.out.println("La cantidad de nombres de las persona 1 es distinta a la de las persona 2");
            return false;
        }

        for (int i = 0; i < personas1v.length; i++) {
            
            sonEquivalentes &= buscarEquivalencias(personas1v[i],personas2v[i]);
        }
        return sonEquivalentes;

    }

    public void getEquivalenciasList() {
        try {
            String q = "SELECT * FROM equivalencias";

            Statement statement = this.conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(q);

            while (resultSet.next()) {
                equivalenciasList.add(new Equivalencias(resultSet.getString(1), resultSet.getString(2)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private boolean buscarEquivalencias(String nombre, String nombreABuscar) {
        ArrayList<String> lista = new ArrayList<>();
        consultarEquivalenciasBag(nombre, lista);
        return lista.contains(nombreABuscar);
    }

    private void consultarEquivalenciasBag(String nombre, ArrayList<String> lista) {
        if (!lista.contains(nombre)) {
            lista.add(nombre);

            for (Equivalencias eq : equivalenciasList) {
                if (eq.equivalencia1.equals(nombre)) {
                    consultarEquivalenciasBag(eq.equivalencia2, lista);
                } else if (eq.equivalencia2.equals(nombre)) {
                    consultarEquivalenciasBag(eq.equivalencia1, lista);
                }
            }
        }
    }

    private Connection obtenerConexion() {
        if (conexion == null) {
            try {
                Class.forName(driver); // driver = "com.mysql.cj.jdbc.Driver";
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            try { // prefijoConexion = "jdbc:mysql://";
                conexion = DriverManager.getConnection(prefijoConexion + ip + "/" + bd, usr, psw);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return conexion;

    }

}
