import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Statement;

public class ModeloDatosTest {

    @Test
    public void testExisteJugador() {
        System.out.println("Prueba de existeJugador");
        String nombre = "";
        ModeloDatos instance = new ModeloDatos();
        boolean expResult = false;
        boolean result = instance.existeJugador(nombre);
        assertEquals(expResult, result);
        //fail("Fallo forzado.");
    }

    @Test
    public void testActualizarJugador() throws Exception {
        System.out.println("Prueba de actualizarJugador");

        // Simular las dependencias de la base de datos
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        // Configurar el comportamiento simulado
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // Inyectar las dependencias simuladas en la instancia de ModeloDatos
        ModeloDatos instance = new ModeloDatos();

        // Le pasamos la conexión simulada
        instance.setConexion(mockConnection);

        instance.abrirConexion(); // Se llama al método sobreescrito que establece la conexión simulada

        String nombreJugador = "Rudy";
        // Ejecutar el método a probar
        instance.actualizarJugador(nombreJugador);

        // Verificar que el método executeUpdate fue llamado con la consulta SQL esperada
        verify(mockStatement, times(1)).executeUpdate("UPDATE Jugadores SET votos=votos+1 WHERE nombre " + " LIKE '%" + nombreJugador + "%'");

        // Limpiar el estado de la conexión simulada
        instance.cerrarConexion();
    }
}
