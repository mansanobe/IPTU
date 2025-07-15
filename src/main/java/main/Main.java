package main;

import java.time.LocalDate;

import main.db.DBConnection;
import main.factory.ImovelFactory;
import main.model.IPTU;
import main.model.Imovel;
import main.model.ValorIPTU;

public class Main {
	
	// Constantes para o DRIVER e STRING DE CONEXÃO do BD
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String CONEXAO = "jdbc:sqlite:c:/Projetos/TVVT/IPTU/iptu.db";
    
	public static void main(String[] args) {
		DBConnection.set(DRIVER, CONEXAO);
		
		// Recupera do BD por ID e por inscrição
		//Imovel i1 = ImovelFactory.getByID(1);
		//Imovel i2 = ImovelFactory.getByInscricao(23456789);
		
		//System.out.println(i1);
		//System.out.println(i2);
		
		// Atualiza os dados de i1
		//i1.setValor(1600000);
		//i1.save();
		//System.out.println(i1);
		
		// Cria dois imóveis que não estão no BD
		Imovel i3 = ImovelFactory.create(78901234, LocalDate.now(), 250000, 45, 'D');
		Imovel i4 = ImovelFactory.create(89012345, LocalDate.of(1800, 1, 1), 700000, 77, 'E');
		
		System.out.println(i3);
		System.out.println(i4);
		
		// Insere os imóveis novos no BD
		// Se tentar inserir duas vezes vai dar erro, porque inscrição é chave única
		//i3.save();
		//i4.save();
		
		//System.out.println(i3);
		//System.out.println(i4);
		
		// Exclui o imóvel i3 do BD
		//i3.delete();
		
		//System.out.println(i3);
		//System.out.println(i4);
		
		// Calcula IPTU de i4
		IPTU iptu = new IPTU(i4);
		
		ValorIPTU valorIPTU = iptu.calculaValor(10, 0.7f);
		
		System.out.println(valorIPTU.valor);
		System.out.println(valorIPTU.valorAVista);
		System.out.println(valorIPTU.parcelamento.getNumParcelas());
		System.out.println(valorIPTU.parcelamento.getValorParcela());
	}

}
