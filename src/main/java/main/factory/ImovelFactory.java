package main.factory;

import java.time.LocalDate;

import main.dao.ImovelDAO;
import main.model.Imovel;
import main.model.ImovelDTO;

/*
// * Classe responsável pela criação do objeto Imovel
 */
public class ImovelFactory {
	
	/*
	 * Retorna um objeto não persistete com os dados passado no parâmetro
	 */
	public static Imovel create(int inscricao, LocalDate dataHabilitacao, float valor, int area, char regiao) {
		return new Imovel(inscricao, dataHabilitacao, valor, area, regiao);
	}
	
	/*
	 * Recupera o Imovel do BD usando o ID
	 * Se objeto não existir no BD retorna nulo.
	 */
	public static Imovel getByID(int id) {
		ImovelDTO dto = ImovelDAO.getByID(id);
		
		// Se o DTO retornou com ID = 0, então não recuperou o objeto do BD
		return dto.id != 0 ? new Imovel(dto) : null;
	}

	/*
	 * Recupera o Imovel do BD usando a inscricao
	 * Se objeto não existir no BD retorna nulo.
	 */
	public static Imovel getByInscricao(int inscricao) {
		ImovelDTO dto = ImovelDAO.getByInscricao(inscricao);
		
		// Se o DTO retornou com ID = 0, então não recuperou o objeto do BD
		return dto.id != 0 ? new Imovel(dto) : null;
	}
}
