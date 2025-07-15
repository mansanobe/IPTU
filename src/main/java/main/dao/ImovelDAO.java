package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import main.db.DBConnection;
import main.model.Imovel;
import main.model.ImovelDTO;

/*
 * Classe responsável pelas operações de SELECT INSERT, DELET e UPDATE na tabela do Imovel
 */
public class ImovelDAO {
	
	/*
	 * Recupera um imóvel com base no ID
	 * Retorna um DTO com os dados do objeto recuperado ou
	 * um DTO com ID = 0 se o objeto nãofoi recuperado do BD
	 */
	public static ImovelDTO getByID(int id) {
		try (Connection conn = DBConnection.get();
			PreparedStatement stm = conn.prepareStatement("select * from imovel where id=?")) {
			
			stm.setInt(1, id);
			
			try (ResultSet rs = stm.executeQuery()) {
			
				ImovelDTO dto = new ImovelDTO();
				
				if (rs.next()) {
				
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
					
					dto.id = rs.getInt("id");
					dto.inscricao = rs.getInt("inscricao");
					dto.dataLiberacao = LocalDate.from(df.parse(rs.getString("dt_liberacao")));
					dto.valor = rs.getFloat("valor");
					dto.area = rs.getInt("area");
					dto.categoria = rs.getString("categoria").charAt(0);
				}
				
				return dto;
			}
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/*
	 * Recupera um imóvel com base na inscricao
	 * Retorna um DTO com os dados do objeto recuperado ou
	 * um DTO com ID = 0 se o objeto não foi recuperado do BD
	 */
	public static ImovelDTO getByInscricao(int inscricao) {
		try (Connection conn = DBConnection.get();
			PreparedStatement stm = conn.prepareStatement("select * from imovel where inscricao=?")) {
			
			stm.setInt(1, inscricao);
			
			try (ResultSet rs = stm.executeQuery()) {
			
				ImovelDTO dto = new ImovelDTO();
				
				if (rs.next()) {
				
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
					
					dto.id = rs.getInt("id");
					dto.inscricao = rs.getInt("inscricao");
					dto.dataLiberacao = LocalDate.from(df.parse(rs.getString("dt_liberacao")));
					dto.valor = rs.getFloat("valor");
					dto.area = rs.getInt("area");
					dto.categoria = rs.getString("categoria").charAt(0);
				}
				
				return dto;
			}
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	/*
	 * Insere um novo Imovel na tabela e retorna o ID gerado pelo SGBD
	 * Retorna ZERO, caso a inserção não seja bem sucedida
	 */
	public static int insert(Imovel imovel) {
		int id = 0;
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		try (Connection conn = DBConnection.get();
			 PreparedStatement stm = conn.prepareStatement("insert into imovel (inscricao, dt_liberacao, valor, area, categoria) values (?,?,?,?,?)")) {		
			
			stm.setInt(1, imovel.getInscricao());
			stm.setString(2, imovel.getDataLiberacao().format(df));
			stm.setFloat(3, imovel.getValor());
			stm.setInt(4, imovel.getArea());
			stm.setString(5, Character.toString(imovel.getCategoria()));
			
			stm.executeUpdate();
			
			// Recupera o ID gerado pelo SGBD
			try (ResultSet rs = stm.getGeneratedKeys()) {
	            if (rs.next())
	                id = rs.getInt(1);
			}

			return id;
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/*
	 * Atualiza os dados do Imovel na tabela e retorna true
	 * Retorna false, caso a atualização não seja bem sucedida
	 */
	public static boolean update(Imovel imovel) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		try (Connection conn = DBConnection.get();
			 PreparedStatement stm = conn.prepareStatement("update imovel set inscricao=?, dt_liberacao=?, valor=?, area=?, categoria=? where id=?")) {		
			
			stm.setInt(1, imovel.getInscricao());
			stm.setString(2, imovel.getDataLiberacao().format(df));
			stm.setFloat(3, imovel.getValor());
			stm.setInt(4, imovel.getArea());
			stm.setString(5, Character.toString(imovel.getCategoria()));
			stm.setInt(6, imovel.getID());
			
			stm.executeUpdate();

			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	/*
	 * Exclui o Imovel da tabela e retorna true
	 * Retorna false, caso a exclusão não seja bem sucedida
	 */
	public static boolean delete(Imovel imovel) {
		try (Connection conn = DBConnection.get();
			 PreparedStatement stm = conn.prepareStatement("delete from imovel where id=?")) {		
			
			stm.setInt(1, imovel.getID());
			
			stm.executeUpdate();

			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

}
