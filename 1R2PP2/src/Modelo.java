
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Integer.parseInt;
import java.sql.*;
import java.util.*;

public class Modelo {

    private String driver, prefijoConexion, ip, usr, psw, bd, tabla, trad, cara;
    private Connection conexion;
    private ActionListener listener;

    public Modelo() {

        driver = "com.mysql.cj.jdbc.Driver";
        prefijoConexion = "jdbc:mysql://";
        ip = "127.0.0.1";
        bd = "programacion";
        tabla = "palabras";
        usr = "";
        psw = "";
        conexion = obtenerConexion();
    }

    public HashMap<String, String> consulta() {

        conexion = obtenerConexion();
        HashMap<String, String> alfabeto = new HashMap();

        String q = "SELECT original, traduccion FROM " + tabla;

        try {

            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(q);

            while (resultSet.next()) {
                alfabeto.put(resultSet.getString(1), resultSet.getString(2));
            }

            resultSet.close();
            statement.close();

        } catch (SQLException ex) {
            reportException(ex.getMessage());
        }

        return alfabeto;
    }

    public String traducir(String texto) {

        HashMap<String, String> palabrasDB = consulta();
        String[] palabrasTextoATraducir = texto.split("\n");
        ArrayList<String> palabrasTextoATraducirFinal = new ArrayList<>();
        String[] aux;
        ArrayList<String> fraseFinal = new ArrayList();

        for (String palabra : palabrasTextoATraducir) {
            for (String palabrita : palabra.split(" ")) {
                 if (!palabrita.contains("(") || palabrita.isEmpty()) {
                    if (palabrita.contains(",")) {
                        aux = palabrita.split(",");
                        for (int i = 0; i < aux.length; i++) {
                            palabrasTextoATraducirFinal.add(aux[i]);
                            palabrasTextoATraducirFinal.add(",");
                        }
                        continue;
                    }

                    if (palabrita.contains(";")) {
                        aux = palabrita.split(";");
                        for (int i = 0; i < aux.length; i++) {
                            palabrasTextoATraducirFinal.add(aux[i]);
                        }
                        palabrasTextoATraducirFinal.add(";");
                        continue;
                    }
                }
                palabrasTextoATraducirFinal.add(palabrita);
            }
            palabrasTextoATraducirFinal.add("\n");
        }

        //recorro cada string (for each)
        for (String palabra : palabrasTextoATraducirFinal) {

            // el string palabra esta ADENTRO de la coleccion DB
            if (palabrasDB.containsKey(palabra)) {
                //existe
                String traduccion = palabrasDB.get(palabra);
                fraseFinal.add(traduccion);

            } // no existe, lo agrego tal cual esta 
            else {
                fraseFinal.add(palabra);

            }
        }

        return String.join(" ", fraseFinal);

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
