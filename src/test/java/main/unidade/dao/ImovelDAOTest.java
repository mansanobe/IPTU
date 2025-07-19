package main.unidade.dao;

import main.dao.ImovelDAO;
import main.db.DBConnection;
import main.model.Imovel;
import main.model.ImovelDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

class ImovelDAOTest {

    @Test
    void testGetByIDCatch() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            when(DBConnection.get()).thenThrow(new RuntimeException("DB error"));
//            dbMock.when(DBConnection::get).thenThrow(new RuntimeException("DB error"));

            Assertions.assertThrows(IllegalArgumentException.class, () -> ImovelDAO.getByID(12345678));
        }
    }

    @Test
    void testGetByIDFound() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("inscricao")).thenReturn(12345678);
        when(rs.getString("dt_liberacao")).thenReturn("20200101");
        when(rs.getFloat("valor")).thenReturn(1000f);
        when(rs.getInt("area")).thenReturn(100);
        when(rs.getString("categoria")).thenReturn("A");

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            ImovelDTO dto = ImovelDAO.getByID(1);
            Assertions.assertEquals(1, dto.id);
            Assertions.assertEquals(12345678, dto.inscricao);
            Assertions.assertEquals(LocalDate.of(2020, 1, 1), dto.dataLiberacao);
            Assertions.assertEquals(1000f, dto.valor);
            Assertions.assertEquals(100, dto.area);
            Assertions.assertEquals('A', dto.categoria);
        }
    }

    @Test
    void testGetByIDNotFound() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            ImovelDTO dto = ImovelDAO.getByID(999);
            Assertions.assertEquals(0, dto.id);
        }
    }

    @Test
    void testInsertSuccess() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);
        when(stm.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(42);

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 100, 'A');
            int id = ImovelDAO.insert(imovel);
            Assertions.assertEquals(42, id);
        }
    }

    @Test
    void testInsertFail() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenThrow(new SQLException());

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 100, 'A');
            int id = ImovelDAO.insert(imovel);
            Assertions.assertEquals(0, id);
        }
    }

    @Test
    void testUpdateSuccess() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 100, 'A');
            imovel.setID(1);
            boolean result = ImovelDAO.update(imovel);
            Assertions.assertTrue(result);
        }
    }

    @Test
    void testUpdateFail() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenThrow(new SQLException());

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 100, 'A');
            imovel.setID(1);
            boolean result = ImovelDAO.update(imovel);
            Assertions.assertFalse(result);
        }
    }

    @Test
    void testDeleteSuccess() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 100, 'A');
            imovel.setID(1);
            boolean result = ImovelDAO.delete(imovel);
            Assertions.assertTrue(result);
        }
    }

    @Test
    void testDeleteFail() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenThrow(new SQLException());

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 100, 'A');
            imovel.setID(1);
            boolean result = ImovelDAO.delete(imovel);
            Assertions.assertFalse(result);
        }
    }
    @Test
    void testGetByInscricaoNotFound() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            ImovelDTO dto = ImovelDAO.getByInscricao(99999999);
            Assertions.assertEquals(0, dto.id);
        }
    }

    @Test
    void testGetByInscricaoFound() throws Exception {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stm = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(conn.prepareStatement(Mockito.anyString())).thenReturn(stm);
        when(stm.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(2);
        when(rs.getInt("inscricao")).thenReturn(87654321);
        when(rs.getString("dt_liberacao")).thenReturn("20221231");
        when(rs.getFloat("valor")).thenReturn(2000f);
        when(rs.getInt("area")).thenReturn(80);
        when(rs.getString("categoria")).thenReturn("B");

        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            ImovelDTO dto = ImovelDAO.getByInscricao(87654321);
            Assertions.assertEquals(2, dto.id);
            Assertions.assertEquals(87654321, dto.inscricao);
            Assertions.assertEquals(2000f, dto.valor);
            Assertions.assertEquals(80, dto.area);
            Assertions.assertEquals('B', dto.categoria);
        }
    }

    @Test
    void testGetByInscricaoCatch() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenThrow(new RuntimeException("DB error"));

            Assertions.assertThrows(IllegalArgumentException.class, () -> ImovelDAO.getByInscricao(12345678));
        }
    }
}