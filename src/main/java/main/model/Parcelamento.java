package main.model;

import main.util.Money;

public class Parcelamento {

	private int numParcelas;
	private float valorParcela;
	
	public Parcelamento(float valorIPTU, float percJurosParcelameto) {
		
		if (valorIPTU < 0)
			throw new IllegalArgumentException("Valor do IPTU inválido");
		
		if (percJurosParcelameto < 0)
			throw new IllegalArgumentException("Juros do parcelamento inválido");
		
		// Define a quantidade de parcelas
		if (valorIPTU == 0)
			numParcelas = 0;
		else if (valorIPTU <= 300)
			numParcelas = 3;
		else if (valorIPTU <= 1000)
			numParcelas = 5;
		else
			numParcelas = 10;
		
		// Calcula o valor da parcela e arredonda
		valorParcela = (float) ((valorIPTU * Math.pow(1 + percJurosParcelameto / 100, numParcelas / 12f)) / numParcelas);
		
		valorParcela = Money.round(valorParcela);
	}

	public int getNumParcelas() {
		return numParcelas;
	}

	public float getValorParcela() {
		return valorParcela;
	}
	
	public float getValorTotal() {
		return numParcelas * valorParcela;
	}
}
