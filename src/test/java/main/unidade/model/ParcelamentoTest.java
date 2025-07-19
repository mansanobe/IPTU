package main.unidade.model;

import main.model.Parcelamento;
import main.util.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParcelamentoTest {

    @Test
    void testValorIPTUNegativo() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Parcelamento(-1f, 0f));
    }

    @Test
    void testPercJurosNegativo() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Parcelamento(100f, -0.1f));
    }

    @Test
    void testNumParcelasZero() {
        Parcelamento p = new Parcelamento(0f, 0f);
        Assertions.assertEquals(0, p.getNumParcelas());
        Assertions.assertEquals(0f, p.getValorParcela());
        Assertions.assertEquals(0f, p.getValorTotal());
    }

    @Test
    void testNumParcelasTres() {
        Parcelamento p = new Parcelamento(200f, 0f);
        Assertions.assertEquals(3, p.getNumParcelas());
        Assertions.assertEquals(Money.round(200f / 3), p.getValorParcela());
        Assertions.assertEquals(Money.round(200f / 3) * 3, p.getValorTotal());
    }

    @Test
    void testNumParcelasCinco() {
        Parcelamento p = new Parcelamento(500f, 0f);
        Assertions.assertEquals(5, p.getNumParcelas());
        Assertions.assertEquals(Money.round(500f / 5), p.getValorParcela());
        Assertions.assertEquals(Money.round(500f / 5) * 5, p.getValorTotal());
    }

    @Test
    void testNumParcelasDez() {
        Parcelamento p = new Parcelamento(2000f, 0f);
        Assertions.assertEquals(10, p.getNumParcelas());
        Assertions.assertEquals(Money.round(2000f / 10), p.getValorParcela());
        Assertions.assertEquals(Money.round(2000f / 10) * 10, p.getValorTotal());
    }

    @Test
    void testValorParcelaComJuros() {
        Parcelamento p = new Parcelamento(1200f, 12f); // juros de 12% ao ano
        Assertions.assertEquals(10, p.getNumParcelas());
        float esperado = (float) ((1200f * Math.pow(1 + 12f / 100, 10 / 12f)) / 10);
        Assertions.assertEquals(Money.round(esperado), p.getValorParcela());
    }

    @Test
    void testGetters() {
        Parcelamento p = new Parcelamento(1000f, 5f);
        Assertions.assertEquals(p.getNumParcelas() * p.getValorParcela(), p.getValorTotal());
    }
}