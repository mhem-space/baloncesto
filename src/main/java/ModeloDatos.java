import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class ModeloDatos {

    private Connection con;
    private Statement set;
    private ResultSet rs;
    Logger logger = Logger.getLogger(getClass().getName());

    public void abrirConexion() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Con variables de entorno
            String dbHost = System.getenv().get("DATABASE_HOST");
            String dbPort = System.getenv().get("DATABASE_PORT");
            String dbName = System.getenv().get("DATABASE_NAME");
            String dbUser = System.getenv().get("DATABASE_USER");
            String dbPass = System.getenv().get("DATABASE_PASS");

            String url = dbHost + ":" + dbPort + "/" + dbName;
            con = DriverManager.getConnection(url, dbUser, dbPass);

        } catch (Exception e) {
            // No se ha conectado
            logger.info("No se ha podido conectar: " + e.getMessage());
        }
    }

    public void setConexion(Connection con) {
        this.con = con;
    }

    public boolean existeJugador(String nombre) {
        boolean existe = false;
        String cad;
        try {
            set = con.createStatement();
            rs = set.executeQuery("SELECT * FROM Jugadores");
            while (rs.next()) {
                cad = rs.getString("Nombre");
                cad = cad.trim();
                if (cad.compareTo(nombre.trim()) == 0) {
                    existe = true;
                }
            }
            rs.close();
            set.close();
        } catch (Exception e) {
            // No lee de la tabla
            logger.info("No lee de la tabla: " + e.getMessage());
        }
        return (existe);
    }

    public void actualizarJugador(String nombre) {
        try {
            set = con.createStatement();
            set.executeUpdate("UPDATE Jugadores SET votos=votos+1 WHERE nombre " + " LIKE '%" + nombre + "%'");
            rs.close();
            set.close();
        } catch (Exception e) {
            // No modifica la tabla
            logger.info("No modifica la tabla: " + e.getMessage());
        }
    }

    public void insertarJugador(String nombre) {
        try {
            set = con.createStatement();
            set.executeUpdate("INSERT INTO Jugadores " + " (nombre,votos) VALUES ('" + nombre + "',1)");
            rs.close();
            set.close();
        } catch (Exception e) {
            // No inserta en la tabla
            logger.info("No inserta en la tabla: " + e.getMessage());
        }
    }

    public void resetearVotos() {
        try {
            set = con.createStatement();
            set.executeUpdate("UPDATE Jugadores SET votos=0");
            rs.close();
            set.close();
        } catch (Exception e) {
            // No modifica la tabla
            logger.info("Error al resetear votos: " + e.getMessage());
        }
    }

    public List<Jugador> getJugadores() {
        List<Jugador> jugadores = new ArrayList<>();
        try {
            set = con.createStatement();
            rs = set.executeQuery("SELECT * FROM Jugadores");
            while (rs.next()) {
                jugadores.add(new Jugador(rs.getInt("id"),rs.getString("nombre"), rs.getInt("votos")));
            }
        } catch (Exception e) {
            logger.info("Error al obtener jugadores: " + e.getMessage());
        }
        return jugadores;
    }

    public void cerrarConexion() {
        try {
            con.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

}
