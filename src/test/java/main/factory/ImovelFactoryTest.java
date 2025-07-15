package main.factory;

import main.model.Imovel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class ImovelFactoryTest {

    @Test
    void testCreate() {
        Imovel result = ImovelFactory.create(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a');
        Assertions.assertEquals(new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a'), result);
    }

    @Test
    void testGetByID() {
        Imovel result = ImovelFactory.getByID(0);
        Assertions.assertEquals(new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a'), result);
    }

    @Test
    void testGetByInscricao() {
        Imovel result = ImovelFactory.getByInscricao(0);
        Assertions.assertEquals(new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a'), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme