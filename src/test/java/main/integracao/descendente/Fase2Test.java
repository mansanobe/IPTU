package main.integracao.descendente;

import main.db.DBConnection;
import main.factory.ImovelFactory;
import main.model.Imovel;
import main.model.ImovelDTO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

public class Fase2Test {

    @Test
    public void testCriarImovelComInscricaoInferior() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.create(9999999, LocalDate.of(2025, 1, 1), 100000, 60, 'A'));
    }

    @Test
    public void testCriarImovelComInscricaoSuperior() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.create(119999999, LocalDate.of(2025, 1, 1), 100000, 60, 'A'));
    }

    @Test
    public void testCriarImovelComDataLiberacaoFutura() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.create(10000000, LocalDate.of(2026, 1, 1), 100000, 60, 'A'));
    }

    @Test
    public void testCriarImovelComValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), -100000, 60, 'A'));
    }

    @Test
    public void testCriarImovelComAreaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, -60, 'A'));
    }

    @Test
    public void testCriarImovelComCategoriaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, '@'));
    }

    @Test
    public void testCriarImovelValido() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        assertEquals(10000000, imovel.getInscricao());
        assertEquals(LocalDate.of(2025, 1, 1), imovel.getDataLiberacao());
        assertEquals(100000, imovel.getValor(), 0.01);
        assertEquals(60, imovel.getArea());
        assertEquals('A', imovel.getCategoria());
    }

    @Test
    public void testGetByIdComIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.getByID(0));
    }

    @Test
    public void testGetByIdComIdQueNaoExiste() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.getByID(999));
    }

    @Test
    public void testGetByIdComIdQueExiste() throws SQLException {
        Connection conn = mock(Connection.class);
        PreparedStatement stm = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);

        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 10000000;
        dto.dataLiberacao = LocalDate.of(2025, 1, 1);
        dto.valor = 100000;
        dto.area = 60;
        dto.categoria = 'A';

        ResultSet rs = mock(ResultSet.class);
        when(stm.getGeneratedKeys()).thenReturn(rs);
        when(stm.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true); // Simula que encontrou resultado
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("inscricao")).thenReturn(10000000);
        when(rs.getString("dt_liberacao")).thenReturn("20250101");
        when(rs.getFloat("valor")).thenReturn(100000f);
        when(rs.getInt("area")).thenReturn(60);
        when(rs.getString("categoria")).thenReturn("A");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = ImovelFactory.getByID(1);

            assertEquals(1, imovel.getID());
            assertEquals(10000000, imovel.getInscricao());
            assertEquals(LocalDate.of(2025, 1, 1), imovel.getDataLiberacao());
            assertEquals(100000, imovel.getValor());
            assertEquals(60, imovel.getArea());
            assertEquals('A', imovel.getCategoria());
        }
    }

    @Test
    public void testGetByIdComInscricaoQueNaoExiste() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.getByInscricao(1));
    }

    @Test
    public void testGetByIdComInscricaoQueExiste() throws SQLException {

        Connection conn = mock(Connection.class);
        PreparedStatement stm = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);

        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 10000000;
        dto.dataLiberacao = LocalDate.of(2025, 1, 1);
        dto.valor = 100000;
        dto.area = 60;
        dto.categoria = 'A';

        ResultSet rs = mock(ResultSet.class);
        when(stm.getGeneratedKeys()).thenReturn(rs);
        when(stm.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true); // Simula que encontrou resultado
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("inscricao")).thenReturn(10000000);
        when(rs.getString("dt_liberacao")).thenReturn("20250101");
        when(rs.getFloat("valor")).thenReturn(100000f);
        when(rs.getInt("area")).thenReturn(60);
        when(rs.getString("categoria")).thenReturn("A");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = ImovelFactory.getByInscricao(10000000);

            assertEquals(1, imovel.getID());
            assertEquals(10000000, imovel.getInscricao());
            assertEquals(LocalDate.of(2025, 1, 1), imovel.getDataLiberacao());
            assertEquals(100000, imovel.getValor());
            assertEquals(60, imovel.getArea());
            assertEquals('A', imovel.getCategoria());
        }
    }

    @Test
    public void testDeleteByIdComIdInvalido() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stm = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(stm);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
            imovel.delete();

            verify(conn, never()).prepareStatement(startsWith("delete from imovel"));
        }
    }

    @Test
    public void testDeleteByIdComIdValido() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stm = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
            imovel.setID(1);
            imovel.delete();

            verify(conn, times(1)).prepareStatement(startsWith("delete from imovel"));
            verify(stm, times(1)).setInt(eq(1), eq(1));
            verify(stm, times(1)).executeUpdate();
            assertEquals(0, imovel.getID());
        }
    }

    @Test
    public void testSaveComImovelNovo() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stm = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);
        when(stm.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
            imovel.save();
            assertEquals(1, imovel.getID());
            verify(stm, times(1)).executeUpdate();
            verify(stm, times(1)).getGeneratedKeys();
        }
    }

    @Test
    public void testSaveComImovelExistente() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stm = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(stm);
        when(stm.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::get).thenReturn(conn);

            Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
            imovel.setID(1);
            imovel.save();
            assertEquals(1, imovel.getID());
            verify(stm, times(1)).executeUpdate();
        }
    }

}
