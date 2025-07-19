package main.unidade.db;

import main.dao.ImovelDAO;
import main.db.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;

class DBConnectionTest {

    @BeforeEach
    void resetDBConnection() throws Exception {
        var driverField = DBConnection.class.getDeclaredField("driver");
        var connStrField = DBConnection.class.getDeclaredField("connectionString");
        var connField = DBConnection.class.getDeclaredField("conn");
        driverField.setAccessible(true);
        connStrField.setAccessible(true);
        connField.setAccessible(true);
        driverField.set(null, null);
        connStrField.set(null, null);
        connField.set(null, null);
    }

    @Test
    void testSetAndGetSuccess() throws Exception {
        try (MockedStatic<DriverManager> dmMock = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConn = Mockito.mock(Connection.class);
            dmMock.when(() -> DriverManager.getConnection("jdbc:h2:mem:testdb", "", "")).thenReturn(mockConn);

            DBConnection.set("org.h2.Driver", "jdbc:h2:mem:testdb");
            Mockito.when(mockConn.isClosed()).thenReturn(false);
            Connection conn = DBConnection.get();
            Assertions.assertNotNull(conn);
        }
    }

    @Test
    void testGetDriverNotSet() throws Exception {
        DBConnection.set(null, "jdbc:h2:mem:testdb");
        var connField = DBConnection.class.getDeclaredField("conn");
        connField.setAccessible(true);
        connField.set(null, null);
        Assertions.assertThrows(IllegalArgumentException.class, DBConnection::get);
    }

    @Test
    void testGetConnectionStringNotSet() throws Exception {
        DBConnection.set("org.h2.Driver", null);
        var connField = DBConnection.class.getDeclaredField("conn");
        connField.setAccessible(true);
        connField.set(null, null);
        Assertions.assertThrows(IllegalArgumentException.class, DBConnection::get);
    }

    @Test
    void testGetInvalidDriver() throws Exception {
        DBConnection.set("invalid.Driver", "jdbc:h2:mem:testdb");
        var connField = DBConnection.class.getDeclaredField("conn");
        connField.setAccessible(true);
        connField.set(null, null);
        Assertions.assertThrows(IllegalArgumentException.class, DBConnection::get);
    }

    @Test
    void testIsAliveTrue() throws Exception {
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenReturn(false);
        Mockito.when(mockConn.isValid(500)).thenReturn(true);

        var field = DBConnection.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(null, mockConn);

        Assertions.assertTrue(DBConnection.isAlive());
    }

    @Test
    void testIsAliveFalse() throws Exception {
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenReturn(true);

        var field = DBConnection.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(null, mockConn);

        Assertions.assertFalse(DBConnection.isAlive());
    }

    @Test
    void testCloseSuccess() throws Exception {
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenReturn(false);

        var field = DBConnection.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(null, mockConn);

        DBConnection.close();
        Mockito.verify(mockConn).close();
    }

    @Test
    void testCloseNoConnection() throws Exception {
        var field = DBConnection.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(null, null);
        Assertions.assertThrows(IllegalArgumentException.class, DBConnection::close);
    }
    @Test
    void testGetByIDCatch() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenThrow(new RuntimeException("DB error"));

            Assertions.assertThrows(IllegalArgumentException.class, () -> ImovelDAO.getByID(12345678));
        }
    }

    @Test
    void testIsAliveThrowsException() throws Exception {
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenThrow(new RuntimeException("Erro inesperado"));

        var field = DBConnection.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(null, mockConn);

        Assertions.assertFalse(DBConnection.isAlive());
    }

    @Test
    void testCloseThrowsException() throws Exception {
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenReturn(false);
        Mockito.doThrow(new RuntimeException("Erro ao fechar")).when(mockConn).close();

        var field = DBConnection.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(null, mockConn);

        Assertions.assertThrows(IllegalArgumentException.class, DBConnection::close);
    }

    @Test
    void testGetConnectionThrowsException() throws Exception {
        DBConnection.set("org.h2.Driver", "jdbc:h2:mem:testdb");
        var connField = DBConnection.class.getDeclaredField("conn");
        connField.setAccessible(true);
        connField.set(null, null);

        // Mocka DriverManager.getConnection para lançar exceção
        try (MockedStatic<DriverManager> dmMock = Mockito.mockStatic(DriverManager.class)) {
            dmMock.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenThrow(new RuntimeException("Erro de conexão"));
            Connection conn = DBConnection.get();
            Assertions.assertNull(conn); // Espera retorno null
        }
    }
}