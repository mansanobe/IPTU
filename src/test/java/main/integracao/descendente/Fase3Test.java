package main.integracao.descendente;

import main.db.DBConnection;
import main.factory.ImovelFactory;
import main.model.*;
import main.util.Money;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

@SuppressWarnings("SqlNoDataSourceInspection")
public class Fase3Test {
    private static Connection setupConnection;

    @BeforeAll
    public static void setupDatabase() throws Exception {
        String connectionUrl = "jdbc:sqlite:file::memory:?cache=shared";
        DBConnection.set("org.sqlite.JDBC", connectionUrl);
        setupConnection = DriverManager.getConnection(connectionUrl);

        try (Statement stm = setupConnection.createStatement()) {
            stm.execute("CREATE TABLE imovel (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "inscricao INTEGER NOT NULL UNIQUE," +
                    "dt_liberacao TEXT NOT NULL," +
                    "valor REAL NOT NULL," +
                    "area INTEGER NOT NULL," +
                    "categoria TEXT NOT NULL" +
                    ")");
        }
    }

    @BeforeEach
    public void cleanTable() throws Exception {
        try (Statement stm = setupConnection.createStatement()) {
            stm.execute("DELETE FROM imovel");
            stm.execute("DELETE FROM sqlite_sequence WHERE name='imovel'");
        }
    }

    @AfterAll
    public static void closeDatabase() throws SQLException {
        if (setupConnection != null && !setupConnection.isClosed()) {
            setupConnection.close();
        }
        DBConnection.close();
    }

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
    public void testGetByIdComIdQueNaoExiste() {
        Imovel imovel = ImovelFactory.getByID(999);
        assertNull(imovel);
    }

    @Test
    public void testGetByIdComIdQueExiste() throws SQLException {
        Connection conn = DBConnection.get();
        assertNotNull(conn);
        try (Statement stm = conn.createStatement()) {
            stm.execute("INSERT INTO imovel (id, inscricao, dt_liberacao, valor, area, categoria) VALUES (1, 10000000, '20240115', 150000, 70, 'B')");
        }

        Imovel imovel = ImovelFactory.getByID(1);

        assertNotNull(imovel);
        assertEquals(1, imovel.getID());
        assertEquals(10000000, imovel.getInscricao());
        assertEquals(150000, imovel.getValor());
        assertEquals(LocalDate.of(2024, 1, 15), imovel.getDataLiberacao());
    }

    @Test
    public void testGetByIdComInscricaoQueNaoExiste() {
        Imovel imovel = ImovelFactory.getByInscricao(999343);
        assertNull(imovel);
    }

    @Test
    public void testGetByIdComInscricaoQueExiste() throws SQLException {
        Connection conn = DBConnection.get();
        assertNotNull(conn);
        try (Statement stm = conn.createStatement()) {
            stm.execute("INSERT INTO imovel (inscricao, dt_liberacao, valor, area, categoria) VALUES (12345678, '20231020', 250000, 120, 'C')");
        }

        Imovel imovel = ImovelFactory.getByInscricao(12345678);

        assertNotNull(imovel);
        assertEquals(1, imovel.getID());
        assertEquals(12345678, imovel.getInscricao());
    }

    @Test
    public void testDeleteComImovelNovoNaoDeveFazerNada() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        imovel.delete();
        assertEquals(0, imovel.getID());
    }

    @Test
    public void testDeleteByIdComIdValido() throws Exception {
        Connection conn = DBConnection.get();
        assertNotNull(conn);
        try (Statement stm = conn.createStatement()) {
            stm.execute("INSERT INTO imovel (id, inscricao, dt_liberacao, valor, area, categoria) VALUES (10, 40000000, '20240301', 800000, 300, 'F')");
        }
        Imovel imovel = ImovelFactory.getByID(10);
        assertNotNull(imovel);

        // Acao
        imovel.delete();

        // Verificacao
        assertEquals(0, imovel.getID());
        assertNull(ImovelFactory.getByID(10));
    }

    @Test
    public void testSaveComImovelNovo() throws Exception {
        Imovel imovel = ImovelFactory.create(20000000, LocalDate.of(2024, 1, 1), 300000, 150, 'D');
        assertEquals(0, imovel.getID());

        imovel.save();

        assertNotEquals(0, imovel.getID());
        Connection conn = DBConnection.get();
        assertNotNull(conn);
        try (Statement stm = conn.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM imovel WHERE id=" + imovel.getID());
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    public void testSaveComImovelExistente() throws Exception {
        Connection conn = DBConnection.get();
        assertNotNull(conn);
        try (Statement stm = conn.createStatement()) {
            stm.execute("INSERT INTO imovel (id, inscricao, dt_liberacao, valor, area, categoria) VALUES (5, 30000000, '20240201', 500000, 200, 'E')");
        }
        Imovel imovel = ImovelFactory.getByID(5);
        assertNotNull(imovel);

        imovel.setValor(550000);
        imovel.save();

        Imovel imovelAtualizado = ImovelFactory.getByID(5);
        assertNotNull(imovelAtualizado);
        assertEquals(550000, imovelAtualizado.getValor());
        assertEquals('E', imovelAtualizado.getCategoria());
    }

    //Testes de Lógica de Negócio com Mocks (IPTU)
    // =================================================================

    @Test
    void testCalculaValorIPTUComDependenciasMockadas() {
        Imovel imovel = new Imovel(10000000, LocalDate.of(2020, 7, 19),
                                                    100000f, 100, 'A');

        try (
                // Mock para o metodo estático Money.round()
                MockedStatic<Money> moneyMock = mockStatic(Money.class);

                // Mock para a construção de objetos Parcelamento
                MockedConstruction<Parcelamento> parcelamentoMock = mockConstruction(Parcelamento.class, (mock, context) -> {
                    when(mock.getNumParcelas()).thenReturn(10);
                    when(mock.getValorParcela()).thenReturn(280.50f);
                })
        ) {

            // Money.round() simplesmente retorna o valor que recebeu, o invocation pega o primeiro argumento
            moneyMock.when(() -> Money.round(anyFloat())).thenAnswer(invocation -> invocation.getArgument(0));
            IPTU iptu = new IPTU(imovel);

            // Chame o metodo a ser testado.
            ValorIPTU resultado = iptu.calculaValor(10f, 5f);

            // area = 100/20 = 5 -> percIPTU = 5 * 0.05 = 0.25
            // valorBase = 100000 * (0.25/100) = 250
            // acrescimo 'A' = 250 * 1.1 = 275
            // desconto idade (5 anos) = (5/5) * 0.03 = 0.03 -> 3%
            // valorFinal = 275 * (1 - 0.03) = 266.75
            assertEquals(266.75f, resultado.valor, 0.01);

            // valorAVista = 266.75 * (1 - 10/100) = 240.075
            assertEquals(240.075f, resultado.valorAVista, 0.001);

            // Verifica se o parcelamento mockado foi atribuído corretamente.
            assertNotNull(resultado.parcelamento);
            assertEquals(10, resultado.parcelamento.getNumParcelas());
            assertEquals(280.50f, resultado.parcelamento.getValorParcela());

            // Garante que o metodo Money.round() foi chamado 2 vezes (uma para o valor total, outra para o valor à vista).
            moneyMock.verify(() -> Money.round(anyFloat()), times(2));

            // Garante que o construtor de Parcelamento foi chamado 1 vez.
            assertEquals(1, parcelamentoMock.constructed().size());
        }
    }

    @Test
    void testCalculaValorIPTUComDescontoInvalido() {
        Imovel imovel = new Imovel(10000000, LocalDate.of(2020, 7, 19), 100000f, 100, 'A');
        IPTU iptu = new IPTU(imovel);

        // Verifica se o metodo lança a exceção correta para um valor de desconto inválido.
        assertThrows(IllegalArgumentException.class, () -> iptu.calculaValor(-10f, 5f));
    }

}
