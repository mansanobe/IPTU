package main.unidade.model;

import main.model.IPTU;
import main.model.Imovel;
import main.model.ValorIPTU;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class IPTUTest {

    @Test
    void testCalculaValorPadrao() {
        // Imóvel válido: inscrição, data, valor, área, categoria
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(10), 100000f, 100, 'A');
        IPTU iptu = new IPTU(imovel);

        ValorIPTU valor = iptu.calculaValor(0f, 0f);

//        Área: 100/20 = 5, fração +1 = 6, mas como 100%20==0, então 5.
//        Percentual: 5 × 0,05% = 0,25%
//                IPTU base: 100.000 × 0,0025 = 250
//        Categoria A: 250 × 1,1 = 275
//        Desconto idade: 10/5 × 3% = 6%, 275 × 0,94 = 258,5
        Assertions.assertEquals(258.5f, valor.valor, 0.01f);
        Assertions.assertEquals(258.5f, valor.valorAVista, 0.01f);
        Assertions.assertNotNull(valor.parcelamento);
    }

    @Test
    void testCalculaValorComDesconto() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(5), 50000f, 40, 'B');
        IPTU iptu = new IPTU(imovel);

        ValorIPTU valor = iptu.calculaValor(10f, 0f);

//        Área: 40/20 = 2, fração +1 = 3, mas como 40%20==0, então 2.
//        Percentual: 2 × 0,05% = 0,10%
//                IPTU base: 50.000 × 0,001 = 50
//        Categoria B: 50 × 1,07 = 53,5
//        Desconto idade: 5/5 × 3% = 3%, 53,5 × 0,97 = 51,9
//        À vista (10% desconto): 51,9 × 0,9 = 46,71
        Assertions.assertEquals(51.9f, valor.valor, 0.01f);
        Assertions.assertEquals(46.71f, valor.valorAVista, 0.01f);
    }

    @Test
    void testCalculaValorCategoriaZ() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 10000f, 20, 'Z');
        IPTU iptu = new IPTU(imovel);

        ValorIPTU valor = iptu.calculaValor(0f, 0f);

        // Categoria Z não calcula IPTU
        Assertions.assertEquals(0f, valor.valor, 0.01f);
        Assertions.assertEquals(0f, valor.valorAVista, 0.01f);
    }

    @Test
    void testCalculaValorIdadeMuitoAlta() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(200), 10000f, 20, 'A');
        IPTU iptu = new IPTU(imovel);

        ValorIPTU valor = iptu.calculaValor(0f, 0f);

        // Idade >= 170 não calcula IPTU
        Assertions.assertEquals(0f, valor.valor, 0.01f);
        Assertions.assertEquals(0f, valor.valorAVista, 0.01f);
    }

    @Test
    void testDescontoInvalido() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 10000f, 20, 'A');
        IPTU iptu = new IPTU(imovel);

        Assertions.assertThrows(IllegalArgumentException.class, () -> iptu.calculaValor(-1f, 0f));
        Assertions.assertThrows(IllegalArgumentException.class, () -> iptu.calculaValor(101f, 0f));
    }

    @Test
    void testJurosInvalido() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(1), 10000f, 20, 'A');
        IPTU iptu = new IPTU(imovel);

        Assertions.assertThrows(IllegalArgumentException.class, () -> iptu.calculaValor(0f, -0.1f));
    }

    @Test
    void testCalculaValorCategoriaC() {
        Imovel imovel = new Imovel(12345678, LocalDate.now().minusYears(3), 80000f, 60, 'C');
        IPTU iptu = new IPTU(imovel);

        ValorIPTU valor = iptu.calculaValor(0f, 0f);

        // Área: 60/20 = 3, fração +1 = 4, mas como 60%20==0, então 3.
        // Percentual: 3 × 0,05% = 0,15%
        // IPTU base: 80.000 × 0,0015 = 120
        // Categoria C: 120 × 1,05 = 126
        // Desconto idade: 3/5 × 3% = 0%, 126 × 1 = 126
        Assertions.assertEquals(126f, valor.valor, 0.01f);
        Assertions.assertEquals(126f, valor.valorAVista, 0.01f);
        Assertions.assertNotNull(valor.parcelamento);
    }
}