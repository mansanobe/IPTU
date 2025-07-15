package main.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class ImovelTest {
    //Field dataLiberacao of type LocalDate - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    Imovel imovel = new Imovel(0, LocalDate.of(2025, Month.JULY, 11), 0f, 0, 'a');

    @Test
    void testSave() {
        imovel.save();
    }

    @Test
    void testDelete() {
        imovel.delete();
    }

    @Test
    void testToString() {
        String result = imovel.toString();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme