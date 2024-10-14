
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Modelo {

    private String usr, psw, tabla, bd, driver, ip, prefijoConexion;
    private Connection conexion;
    private ArrayList<Codigo> alfabeto;
    private ActionListener listener;

    public Modelo() {
        driver = "com.mysql.cj.jdbc.Driver";
        prefijoConexion = "jdbc:mysql://";
        ip = "127.0.0.1";
        bd = "codigo";
        tabla = "representacion";
        usr = "";
        psw = "";
        conexion = obtenerConexion();
        System.out.println("Me conecteee");
        alfabeto = new ArrayList();

    }

    public String traducirTexto(String frase) {
        obtenerAlfabeto();
        String[] desarmador = frase.split("");
        String aux;
        ArrayList<String> fraseFinal = new ArrayList();
        for (String palabra : desarmador) {
            aux = buscarYTraducir(palabra);
            if (!aux.equals("")) {
                fraseFinal.add(aux);
            } else {
                System.out.println("La palabra no pertenece al alfabeto dado");
                System.exit(1);
            }
        }
        return String.join(" ", fraseFinal);

    }

    private String buscarYTraducir(String palabra) {
        for (Codigo cod : alfabeto) {
            if (cod.equals(palabra)) {
                if (cod.getTraduccion().length() >= 5) {
                    return cod.getTraduccion();
                } else {
                    //traduzco cada uno de los caracteres de la palabra
                   return decodificarNumero(cod.getTraduccion());
                }
            }
        }
        return "";
    }

    private String decodificarNumero(String s) {

        char[] desarmador = s.toCharArray();
        String nFinal = null;
        ArrayList<String> numeroFinal = new ArrayList();

        for (int i = 0; i < desarmador.length; i++) {
            for (int j = 0; j < alfabeto.size(); j++) {
                if (alfabeto.get(j).getCaracter().equals(String.valueOf(desarmador[i]))) {
                    numeroFinal.add(alfabeto.get(j).getTraduccion());
                }
            }
        }
        nFinal = String.join("", numeroFinal);
        return nFinal;
    }

    private void obtenerAlfabeto() {

        conexion = obtenerConexion();
        alfabeto.clear();
        String q = "SELECT * FROM " + tabla;

        try {

            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(q);

            while (resultSet.next()) {
                alfabeto.add(new Codigo(resultSet.getString(1), resultSet.getString(2)));
            }

            resultSet.close();
            statement.close();

        } catch (SQLException ex) {
            reportException(ex.getMessage());
        }

    }

    private Connection obtenerConexion() {
        if (conexion == null) {
            try {
                Class.forName(driver); // driver = "com.mysql.cj.jdbc.Driver";
            } catch (ClassNotFoundException ex) {
                reportException(ex.getMessage());
            }
            try { // prefijoConexion = "jdbc:mysql://";
                conexion
                        = DriverManager.getConnection(prefijoConexion + ip + "/" + bd, usr, psw);
            } catch (Exception ex) {
                reportException(ex.getMessage());
            }
            Runtime.getRuntime().addShutdownHook(new ShutDownHook());
        }
        return conexion;
    }

    private class ShutDownHook extends Thread {

        public void run() {
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                reportException(ex.getMessage());
            }
        }
    }

    public void addExceptionListener(ActionListener listener) {
        this.listener = listener;
    }

    private void reportException(String exception) {
        if (listener != null) {
            ActionEvent evt = new ActionEvent(this, 0, exception);
            listener.actionPerformed(evt);
        }
    }

}
