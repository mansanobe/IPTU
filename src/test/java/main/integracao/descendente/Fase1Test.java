package main.integracao.descendente;

import main.dao.ImovelDAO;
import main.factory.ImovelFactory;
import main.model.Imovel;
import main.model.ImovelDTO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

public class Fase1Test {

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
        // Mock do DAO
        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 10000000;
        dto.dataLiberacao = LocalDate.of(2025, 1, 1);
        dto.valor = 100000;
        dto.area = 60;
        dto.categoria = 'A';

        try (MockedStatic<ImovelDAO> daoMock = mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.getByID(1)).thenReturn(dto);

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
    public void testGetByIdComInscricaoQueExiste() {

        // Mock do DAO
        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 10000000;
        dto.dataLiberacao = LocalDate.of(2025, 1, 1);
        dto.valor = 100000;
        dto.area = 60;
        dto.categoria = 'A';
        try (MockedStatic<ImovelDAO> daoMock = mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.getByInscricao(1)).thenReturn(dto);

            Imovel imovel = ImovelFactory.getByInscricao(1);

            assertEquals(1, imovel.getID());
            assertEquals(10000000, imovel.getInscricao());
            assertEquals(LocalDate.of(2025, 1, 1), imovel.getDataLiberacao());
            assertEquals(100000, imovel.getValor());
            assertEquals(60, imovel.getArea());
            assertEquals('A', imovel.getCategoria());
        }
    }

    @Test
    public void testDeleteByIdComIdInvalido() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        imovel.delete();
        try (MockedStatic<ImovelDAO> daoMock = mockStatic(ImovelDAO.class)) {
            daoMock.verify(() -> ImovelDAO.delete(imovel), never());
        }
    }

    @Test
    public void testDeleteByIdComIdValido() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        imovel.setID(1);
        try (MockedStatic<ImovelDAO> daoMock = mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.delete(any(Imovel.class))).thenReturn(true);
            imovel.delete();
            assertEquals(0, imovel.getID());
            daoMock.verify(() -> ImovelDAO.delete(imovel), times(1));
        }
    }

    @Test
    public void testSaveComImovelNovo() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        try (MockedStatic<ImovelDAO> daoMock = mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.insert(any(Imovel.class))).thenReturn(1);
            imovel.save();
            assertEquals(1, imovel.getID());
            daoMock.verify(() -> ImovelDAO.insert(imovel), times(1));
        }
    }

    @Test
    public void testSaveComImovelExistente() {
        Imovel imovel = ImovelFactory.create(10000000, LocalDate.of(2025, 1, 1), 100000, 60, 'A');
        imovel.setID(1);
        try (MockedStatic<ImovelDAO> daoMock = mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.update(any(Imovel.class))).thenReturn(true);
            imovel.save();
            assertEquals(1, imovel.getID());
            daoMock.verify(() -> ImovelDAO.update(imovel), times(1));
        }
    }


}
