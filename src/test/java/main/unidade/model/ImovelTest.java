package main.unidade.model;

import main.dao.ImovelDAO;
import main.model.Imovel;
import main.model.ImovelDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;

class ImovelTest {

    @Test
    void testConstrutorValido() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        Assertions.assertEquals(12345678, imovel.getInscricao());
        Assertions.assertEquals(1000f, imovel.getValor());
        Assertions.assertEquals(50, imovel.getArea());
        Assertions.assertEquals('A', imovel.getCategoria());
    }

    @Test
    void testConstrutorDTOValido() {
        ImovelDTO dto = new ImovelDTO();
        dto.id = 5;
        dto.inscricao = 87654321;
        dto.dataLiberacao = LocalDate.now().minusYears(2);
        dto.valor = 2000f;
        dto.area = 80;
        dto.categoria = 'B';

        Imovel imovel = new Imovel(dto);
        Assertions.assertEquals(5, imovel.getID());
        Assertions.assertEquals(87654321, imovel.getInscricao());
        Assertions.assertEquals(2000f, imovel.getValor());
        Assertions.assertEquals(80, imovel.getArea());
        Assertions.assertEquals('B', imovel.getCategoria());
    }

    @Test
    void testSettersAndGetters() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        imovel.setInscricao(87654321);
        imovel.setDataLiberacao(LocalDate.now().minusYears(5));
        imovel.setValor(5000f);
        imovel.setArea(100);
        imovel.setCategoria('C');
        imovel.setID(10);

        Assertions.assertEquals(87654321, imovel.getInscricao());
        Assertions.assertEquals(LocalDate.now().minusYears(5), imovel.getDataLiberacao());
        Assertions.assertEquals(5000f, imovel.getValor());
        Assertions.assertEquals(100, imovel.getArea());
        Assertions.assertEquals('C', imovel.getCategoria());
        Assertions.assertEquals(10, imovel.getID());
    }

    @Test
    void testToString() {
        Imovel imovel = new Imovel(12345678, LocalDate.of(2020, 1, 1), 1000f, 50, 'A');
        imovel.setID(1);
        String expected = "Imovel [id=1, inscricao=12345678, dataLiberacao=2020-01-01, valor=1000.0, area=50, regiao=A]";
        Assertions.assertEquals(expected, imovel.toString());
    }

    @Test
    void testInscricaoInvalida() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(9999999, LocalDate.now().minusYears(1), 1000f, 50, 'A'));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(100000000, LocalDate.now().minusYears(1), 1000f, 50, 'A'));
    }

    @Test
    void testDataLiberacaoFutura() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().plusDays(1), 1000f, 50, 'A'));
    }

    @Test
    void testValorInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().minusYears(1), 0f, 50, 'A'));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().minusYears(1), -10f, 50, 'A'));
    }

    @Test
    void testAreaInvalida() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 0, 'A'));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, -5, 'A'));
    }

    @Test
    void testCategoriaInvalida() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, '@'));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, '['));
    }

    @Test
    void testSaveInsert() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        try (MockedStatic<ImovelDAO> mock = org.mockito.Mockito.mockStatic(ImovelDAO.class)) {
            mock.when(() -> ImovelDAO.insert(imovel)).thenReturn(42);
            imovel.save();
            Assertions.assertEquals(42, imovel.getID());
            mock.verify(() -> ImovelDAO.insert(imovel));
        }
    }

    @Test
    void testSaveUpdate() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        imovel.setID(10);
        try (MockedStatic<ImovelDAO> mock = org.mockito.Mockito.mockStatic(ImovelDAO.class)) {
            mock.when(() -> ImovelDAO.update(imovel)).thenReturn(true);
            imovel.save();
            Assertions.assertEquals(10, imovel.getID());
            mock.verify(() -> ImovelDAO.update(imovel));
        }
    }

    @Test
    void testDeleteWhenIdZero() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        imovel.setID(0);
        try (MockedStatic<ImovelDAO> mock = org.mockito.Mockito.mockStatic(ImovelDAO.class)) {
            imovel.delete();
            mock.verifyNoInteractions();
            Assertions.assertEquals(0, imovel.getID());
        }
    }

    @Test
    void testDeleteWhenDaoReturnsTrue() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        imovel.setID(15);
        try (MockedStatic<ImovelDAO> mock = org.mockito.Mockito.mockStatic(ImovelDAO.class)) {
            mock.when(() -> ImovelDAO.delete(imovel)).thenReturn(true);
            imovel.delete();
            Assertions.assertEquals(0, imovel.getID());
            mock.verify(() -> ImovelDAO.delete(imovel));
        }
    }

    @Test
    void testDeleteWhenDaoReturnsFalse() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 1000f, 50, 'A');
        imovel.setID(20);
        try (MockedStatic<ImovelDAO> mock = org.mockito.Mockito.mockStatic(ImovelDAO.class)) {
            mock.when(() -> ImovelDAO.delete(imovel)).thenReturn(false);
            imovel.delete();
            Assertions.assertEquals(20, imovel.getID());
            mock.verify(() -> ImovelDAO.delete(imovel));
        }
    }
}