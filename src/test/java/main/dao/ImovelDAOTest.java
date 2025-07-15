package main.dao;

import main.model.Imovel;
import main.model.ImovelDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class ImovelDAOTest {

    @Test
    void testGetByID() {
        ImovelDTO result = ImovelDAO.getByID(0);
        Assertions.assertEquals(new ImovelDTO(), result);
    }

    @Test
    void testGetByInscricao() {
        ImovelDTO result = ImovelDAO.getByInscricao(0);
        Assertions.assertEquals(new ImovelDTO(), result);
    }

    @Test
    void testInsert() {
        int result = ImovelDAO.insert(new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a'));
        Assertions.assertEquals(0, result);
    }

    @Test
    void testUpdate() {
        boolean result = ImovelDAO.update(new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a'));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testDelete() {
        boolean result = ImovelDAO.delete(new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a'));
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme