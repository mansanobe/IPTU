package main.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.mockito.Mockito.*;

class DBConnectionTest {
    @Mock
    Connection conn;
    @InjectMocks
    DBConnection dBConnection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGet() {
        when(conn.isClosed()).thenReturn(true);

        Connection result = DBConnection.get();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testSet() {
        DBConnection.set("driver", "connectionString");
    }

    @Test
    void testIsAlive() {
        when(conn.isClosed()).thenReturn(true);
        when(conn.isValid(anyInt())).thenReturn(true);

        boolean result = DBConnection.isAlive();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testClose() {
        when(conn.isClosed()).thenReturn(true);

        DBConnection.close();
        verify(conn).close();
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme