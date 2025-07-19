package main.unidade.factory;

import main.dao.ImovelDAO;
import main.factory.ImovelFactory;
import main.model.Imovel;
import main.model.ImovelDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;

class ImovelFactoryTest {

    @Test
    void testCreate() {
        Imovel result = ImovelFactory.create(12345678, LocalDate.now(), 1000f, 80, 'A');
        Imovel expected = new Imovel(12345678, LocalDate.now(), 1000f, 80, 'A');
        Assertions.assertEquals(expected.getInscricao(), result.getInscricao());
        Assertions.assertEquals(expected.getDataLiberacao(), result.getDataLiberacao());
        Assertions.assertEquals(expected.getValor(), result.getValor());
        Assertions.assertEquals(expected.getArea(), result.getArea());
        Assertions.assertEquals(expected.getCategoria(), result.getCategoria());
    }

    @Test
    void testGetByIDFound() {
        ImovelDTO dto = new ImovelDTO();
        dto.id = 1;
        dto.inscricao = 12345678;
        dto.dataLiberacao = LocalDate.now();
        dto.valor = 1000f;
        dto.area = 80;
        dto.categoria = 'A';

        try (MockedStatic<ImovelDAO> daoMock = Mockito.mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.getByID(1)).thenReturn(dto);
            Imovel result = ImovelFactory.getByID(1);
            Imovel expected = new Imovel(dto);
            Assertions.assertEquals(expected.getInscricao(), result.getInscricao());
            Assertions.assertEquals(expected.getDataLiberacao(), result.getDataLiberacao());
            Assertions.assertEquals(expected.getValor(), result.getValor());
            Assertions.assertEquals(expected.getArea(), result.getArea());
            Assertions.assertEquals(expected.getCategoria(), result.getCategoria());
        }
    }

    @Test
    void testGetByIDNotFound() {
        ImovelDTO dto = new ImovelDTO();
        dto.id = 0; // Não encontrado

        try (MockedStatic<ImovelDAO> daoMock = Mockito.mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.getByID(99)).thenReturn(dto);
            Imovel result = ImovelFactory.getByID(99);
            Assertions.assertNull(result);
        }
    }

    @Test
    void testGetByInscricaoFound() {
        ImovelDTO dto = new ImovelDTO();
        dto.id = 2;
        dto.inscricao = 87654321;
        dto.dataLiberacao = LocalDate.now();
        dto.valor = 2000f;
        dto.area = 120;
        dto.categoria = 'B';

        try (MockedStatic<ImovelDAO> daoMock = Mockito.mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.getByInscricao(87654321)).thenReturn(dto);
            Imovel result = ImovelFactory.getByInscricao(87654321);
            Imovel expected = new Imovel(dto);
            Assertions.assertEquals(expected.getInscricao(), result.getInscricao());
            Assertions.assertEquals(expected.getDataLiberacao(), result.getDataLiberacao());
            Assertions.assertEquals(expected.getValor(), result.getValor());
            Assertions.assertEquals(expected.getArea(), result.getArea());
            Assertions.assertEquals(expected.getCategoria(), result.getCategoria());
        }
    }

    @Test
    void testGetByInscricaoNotFound() {
        ImovelDTO dto = new ImovelDTO();
        dto.id = 0; // Não encontrado

        try (MockedStatic<ImovelDAO> daoMock = Mockito.mockStatic(ImovelDAO.class)) {
            daoMock.when(() -> ImovelDAO.getByInscricao(99999999)).thenReturn(dto);
            Imovel result = ImovelFactory.getByInscricao(99999999);
            Assertions.assertNull(result);
        }
    }
}