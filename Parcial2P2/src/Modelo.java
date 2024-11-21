
import java.sql.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Modelo {

    private String driver, prefijoConexion, ip, usr, psw, bd, tabla;
    private Connection conexion;
    private ActionListener listener;
    private String ejeX, ejeY, resultadoConsulta;

    public Modelo() {

        ejeX = "x";
        ejeY = "y";
        driver = "com.mysql.cj.jdbc.Driver";
        prefijoConexion = "jdbc:mysql://";
        ip = "127.0.0.1";
        bd = "puntos";
        tabla = "coordenadas";
        usr = "";
        psw = "";
        conexion = obtenerConexion();
    }

    // obtengo en un listaa en memoria todas las coordenadasDbList de la db, de esta manera solo consulto una vez la DB
    private ArrayList<CoordenadaPlano> obtenerCoordenadasDB() {

        conexion = obtenerConexion();

        ArrayList<CoordenadaPlano> coorPlano = new ArrayList();
        String q = "SELECT * FROM " + tabla;

        try {
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(q);

            while (resultSet.next()) {
                coorPlano.add(new CoordenadaPlano(resultSet.getInt(1), resultSet.getInt(2)));
            }

            resultSet.close();
            statement.close();

        } catch (SQLException ex) {
            reportException(ex.getMessage());
        }

        return coorPlano;
    }

    // calcula las distancia entre cada coordenada de la db y el punto. 
    private ArrayList<CoordenadaPlano> calcularDistancias(int x, int y) {

        //me obtengo de la db todas las coordenadas
        ArrayList<CoordenadaPlano> coordenadasDbList = obtenerCoordenadasDB();
        // en esta lista voy a guardame la distancias de cada coordenada respecto de los puntos dados
        ArrayList<CoordenadaPlano> distanciasPtosList = new ArrayList();

        //por cada punto de la db le calculo la distancia respecto a los puntos dados
        for (CoordenadaPlano punto : coordenadasDbList) {
            punto.calcularDistanciaEntrePuntos(x, y);
            // lista final con todas las coordenadas y sus distancias respecto al punto 
            distanciasPtosList.add(punto);
        }

// uso un clase comparate custom para ordenar las coordenadas segun la distancia 
// uso metodo comparing para ordenar , por default me lo ordena ascendentemente 
        Collections.sort(distanciasPtosList, Comparator.comparing(CoordenadaPlano::getDistancia));

// saco las coordenadas cuya distancia seria 0, de este modo saco el punto a comparar de la lista.
        for (int i = 0; i < distanciasPtosList.size(); i++) {

            if (distanciasPtosList.get(i).getDistancia() == 0.0) {
                distanciasPtosList.remove(i);
            }

        }

        return distanciasPtosList;
    }

    // Sobrecargo el metodo calcularDistancias para que me me de una  la distancia en base a una coleccion de elementos ya dados
    private ArrayList<CoordenadaPlano> calcularDistancias(int x, int y, ArrayList<CoordenadaPlano> restantes) {

        ArrayList<CoordenadaPlano> distanciasPtosList = new ArrayList();

        for (CoordenadaPlano punto : restantes) {

            punto.calcularDistanciaEntrePuntos(x, y);
            distanciasPtosList.add(punto);

        }
// reordeno lista x distancia 
        Collections.sort(distanciasPtosList, Comparator.comparing(CoordenadaPlano::getDistancia));

        for (int i = 0; i < distanciasPtosList.size(); i++) {

            if (distanciasPtosList.get(i).getDistancia() == 0.0) {
                distanciasPtosList.remove(i);
            }

        }

        return distanciasPtosList;
    }

    public String obtenerPuntosCercanosText(int x, int y) {
        ArrayList<CoordenadaPlano> ptosCercanosList = obtenerPuntosCercanos(x, y);
        ArrayList<String> listFinal = new ArrayList<>();
        for (CoordenadaPlano pto : ptosCercanosList) {
            listFinal.add(pto.toString());
        }

        return   String.join(" ", listFinal);
    }

    private ArrayList<CoordenadaPlano> obtenerPuntosCercanos(int x, int y) {

        ArrayList<CoordenadaPlano> ptosCercanosListFinal = new ArrayList();
        ArrayList<CoordenadaPlano> ptosCercanosAuxiliar = calcularDistancias(x, y);

        int tam = ptosCercanosAuxiliar.size();

        //tomo el primer elemento de la lista auxiliar (ya ordenado) 
        //, a los restante le recalculo la distancia en base a ese punto relativo y asi sucesivamente
        for (int i = 0; i < tam; i++) {

            ptosCercanosListFinal.add(ptosCercanosAuxiliar.get(0));
            ptosCercanosAuxiliar.remove(0);

            x = ptosCercanosListFinal.get(i).getX();
            y = ptosCercanosListFinal.get(i).getY();
// recalculo distancias
            ptosCercanosAuxiliar = calcularDistancias(x, y, ptosCercanosAuxiliar);

        }

        return ptosCercanosListFinal;
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
