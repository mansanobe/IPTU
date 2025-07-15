package main.model;

import java.time.LocalDate;

import main.dao.ImovelDAO;
import main.db.IPersistent;

public class Imovel implements IPersistent {
	
	/*
	 * ID do objeto: será gerado pelo SGBD quando o objeto for inserido na tabela
	 * ID == 0 -> objeto não está inserido na tabela do BD (objeto não persistente)
	 * ID != 0 -> objeto está inserido na tabela do BD com esse ID (objeto persistente)
	 */
	private int id = 0; 
	
	private int inscricao;
	private LocalDate dataLiberacao;
	private float valor;
	private int area;
	private char categoria;
	
	public Imovel(int inscricao, LocalDate dataLiberacao, float valor, int area, char categoria) {
		super();
		
		this.inscricao = inscricao;
		this.dataLiberacao = dataLiberacao;
		this.valor = valor;
		this.area = area;
		this.categoria = categoria;
		
		verificaValidade();
	}
	
	public Imovel(ImovelDTO dto) {
		super();
		this.id = dto.id;
		this.inscricao = dto.inscricao;
		this.dataLiberacao = dto.dataLiberacao;
		this.valor = dto.valor;
		this.area = dto.area;
		this.categoria = dto.categoria; 
		
		verificaValidade();
	}

	public int getInscricao() {
		return inscricao;
	}
	
	public void setInscricao(int inscricao) {
		this.inscricao = inscricao;
	}
	
	public LocalDate getDataLiberacao() {
		return dataLiberacao;
	}
	
	public void setDataLiberacao(LocalDate dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}
	
	public float getValor() {
		return valor;
	}
	
	public void setValor(float valor) {
		this.valor = valor;
	}
	
	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public char getCategoria() {
		return categoria;
	}
	
	public void setCategoria(char categoria) {
		this.categoria = categoria;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	/*
	 * Salva o imovel no BD
	 * Gera um INSERT ou um UPDATE, dependendo do ID do imovel
	 */
	@Override
	public void save() {
		if (this.id == 0)
			// Se ID == 0 então o objeto ainda não foi inserido no BD -> faz um INSERT
			this.id = ImovelDAO.insert(this);
		else
			// Se ID != 0 então o objeto ainda já foi inserido no BD -> faz um UPDATE
			ImovelDAO.update(this);
	}

	/*
	 * Exclui o objeto do BD
	 * Caso seja bem sucedido, o ID retorna para ZERO
	 */
	@Override
	public void delete() {
		// Só exclui o objeto do BD se ID != 0
		if (this.id != 0)
			if (ImovelDAO.delete(this))
				this.id = 0;
	}

	
	@Override
	public String toString() {
		return "Imovel [id=" + id + ", inscricao=" + inscricao + ", dataLiberacao=" + dataLiberacao + ", valor=" + valor
				+ ", area=" + area + ", regiao=" + categoria + "]";
	}

	private void verificaValidade() {
		if (inscricao < 10000000 || inscricao > 99999999)
			throw new IllegalArgumentException("Inscrição inválida");
		
		if (dataLiberacao.isAfter(LocalDate.now()))
			throw new IllegalArgumentException("Data de liberação inválida");
		
		if (valor <= 0)
			throw new IllegalArgumentException("Valor inválido");
		
		if (area <= 0)
			throw new IllegalArgumentException("Área inválida");
		
		if (categoria < 'A' || categoria > 'Z')
			throw new IllegalArgumentException("Categoria inválida");
	}
}
