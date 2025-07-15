package main.model;

import java.time.LocalDate;

public class ImovelDTO {

	public int id = 0;
	public int inscricao = 0;
	public LocalDate dataLiberacao = LocalDate.MIN;
	public float valor = 0;
	public int area = 0;
	public char categoria = ' ';
}
