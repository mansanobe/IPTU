package main.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.*;

class IPTUTest {
    @Mock
    Imovel imovel;
    @InjectMocks
    IPTU iPTU;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculaValor() {
        when(imovel.getDataLiberacao()).thenReturn(LocalDate.of(2025, Month.JULY, 11));
        when(imovel.getValor()).thenReturn(0f);
        when(imovel.getArea()).thenReturn(0);
        when(imovel.getCategoria()).thenReturn('a');

        ValorIPTU result = iPTU.calculaValor(0f, 0f);
        Assertions.assertEquals(new ValorIPTU(), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme