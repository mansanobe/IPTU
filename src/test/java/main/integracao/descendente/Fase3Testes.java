package main.integracao.descendente;

import main.dao.ImovelDAO;
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

public class Fase3Testes {

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
    public void testGetByIdComIdQueExiste() {
        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 10000000;
        dto.dataLiberacao = LocalDate.of(2025, 1, 1);
        dto.valor = 100000;
        dto.area = 60;
        dto.categoria = 'A';


        Imovel imovel = ImovelFactory.getByID(1);

        assertEquals(1, imovel.getID());
        assertEquals(10000000, imovel.getInscricao());
        assertEquals(LocalDate.of(2025, 1, 1), imovel.getDataLiberacao());
        assertEquals(100000, imovel.getValor());
        assertEquals(60, imovel.getArea());
        assertEquals('A', imovel.getCategoria());
    }

    @Test
    public void testGetByIdComInscricaoQueNaoExiste() {
        assertThrows(IllegalArgumentException.class, () -> ImovelFactory.getByInscricao(1));
    }

    @Test
    public void testGetByIdComInscricaoQueExiste() throws SQLException {

        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 10000000;
        dto.dataLiberacao = LocalDate.of(2025, 1, 1);
        dto.valor = 100000;
        dto.area = 60;
        dto.categoria = 'A';


        Imovel imovel = ImovelFactory.getByInscricao(10000000);

        assertEquals(1, imovel.getID());
        assertEquals(10000000, imovel.getInscricao());
        assertEquals(LocalDate.of(2025, 1, 1), imovel.getDataLiberacao());
        assertEquals(100000, imovel.getValor());
        assertEquals(60, imovel.getArea());
        assertEquals('A', imovel.getCategoria());

    }

    @Test
    public void testDeleteByIdComIdInvalido() throws Exception {
        try (MockedStatic<ImovelDAO> imovelDAOMock = mockStatic(ImovelDAO.class)) {
            Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
            imovel.delete();

            imovelDAOMock.verify(() -> ImovelDAO.delete(any(Imovel.class)), never());
        }
    }

    @Test
    public void testDeleteByIdComIdValido() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        imovel.setID(1);
        imovel.delete();

        assertEquals(0, imovel.getID());

    }

    @Test
    public void testSaveComImovelNovo() throws Exception {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        try (MockedStatic<ImovelDAO> imovelDAOMock = mockStatic(ImovelDAO.class)) {
            imovel.save();

            imovelDAOMock.verify(() -> ImovelDAO.insert(any(Imovel.class)), times(1));
        }
    }

    @Test
    public void testSaveComImovelExistente() throws Exception {
        try (MockedStatic<ImovelDAO> imovelDAOMock = mockStatic(ImovelDAO.class)) {

            Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
            imovel.setID(1);
            imovel.save();
            assertEquals(1, imovel.getID());
            imovelDAOMock.verify(() -> ImovelDAO.update(any(Imovel.class)), times(1));
        }
    }

}
