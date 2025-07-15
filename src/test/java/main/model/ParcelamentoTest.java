package main.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParcelamentoTest {
    Parcelamento parcelamento = new Parcelamento(0f, 0f);

    @Test
    void testGetValorTotal() {
        float result = parcelamento.getValorTotal();
        Assertions.assertEquals(0f, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme