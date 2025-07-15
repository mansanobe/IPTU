package main.model;

import java.time.LocalDate;

import main.util.Money;

public class IPTU {

	private Imovel imovel;
	
	public IPTU(Imovel imovel) {
		super();
		this.imovel = imovel;
	}
	
	public ValorIPTU calculaValor(float percDescontoVista, float percJurosParcelamento) {	
		if (percDescontoVista < 0 || percDescontoVista > 100)
			throw new IllegalArgumentException("Desconto para pagamento à vista inválido");
		
		if (percJurosParcelamento < 0)
			throw new IllegalArgumentException("Juros do parcelamento inválido");
		
		float valorIPTU = 0;
		
		// Calcula a idade do imóvel 
		int idade = LocalDate.now().getYear() - imovel.getDataLiberacao().getYear();
		
		if (idade < 170 && imovel.getCategoria() != 'Z') {
			// Calcula o percentual do IPTU
			int area = imovel.getArea() / 20 + 1;
			
			if (imovel.getArea() % 20 == 0)
				area--;
			
			float percIPTU = area * 0.05f;
			
			// Calcula valor do IPTU
			valorIPTU = imovel.getValor() * (percIPTU / 100);
			
			// Calcula acréscimo por região
			if (imovel.getCategoria() == 'A')
				valorIPTU *= 1.1f;
			else if (imovel.getCategoria() == 'B')
				valorIPTU *= 1.07f;
			else if (imovel.getCategoria() == 'C')
				valorIPTU *= 1.05f;
			
			// Calcula desconto por idade
			float percDescontoIdade = idade / 5 * 0.03f;
			
			valorIPTU *= 1 - percDescontoIdade;
			
			valorIPTU = Money.round(valorIPTU);
		}
		
		ValorIPTU iptu = new ValorIPTU();
		
		iptu.valor = valorIPTU;
		
		// Calcula desconto para pagamento à vista
		iptu.valorAVista  = Money.round(valorIPTU * (1 - percDescontoVista / 100));
		
	    // Calcula o parcelamento
		iptu.parcelamento = new Parcelamento(valorIPTU, percJurosParcelamento);
		
		return iptu;
	}
}
