
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author damia
 */
public class Modelo {

    private class Trayecto {

        String nombre;
        List<Trayecto> vecinos;

        public Trayecto(String nombre) {
            this.nombre = nombre;
            this.vecinos = new ArrayList<>();
        }

        public void agregarVecino(Trayecto vecino) {
            vecinos.add(vecino);
        }
    }

    private String usr, psw, tabla, bd, driver, ip, prefijoConexion, colUno, colDos;
    private Connection conexion;
    private ActionListener listener;
    private Map<String, Trayecto> grafo;

    public Modelo() {
        driver = "com.mysql.cj.jdbc.Driver";
        prefijoConexion = "jdbc:mysql://";
        ip = "127.0.0.1";
        bd = "gps";
        tabla = "sentido";
        usr = "";
        psw = "";
        colUno = "desde";
        colDos = "hasta";
        conexion = obtenerConexion();
    }

    private void construirGrafo() {

        conexion = obtenerConexion();
        String q = "SELECT * FROM " + tabla;

        try {

            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(q);

            while (resultSet.next()) {
                agregarRuta(resultSet.getString(1), resultSet.getString(2));
            }

            resultSet.close();
            statement.close();

        } catch (SQLException ex) {
            reportException(ex.getMessage());
        }

    }

    private void agregarRuta(String desde, String hasta) {
        grafo.putIfAbsent(desde, new Trayecto(desde));
        grafo.putIfAbsent(hasta, new Trayecto(hasta));
        grafo.get(desde).agregarVecino(grafo.get(hasta));
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
