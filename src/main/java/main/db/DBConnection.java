package main.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe singleton que retorna uma conexão JDBC.
 */
public class DBConnection {

	private static String driver = null;
	private static String connectionString = null;
	private static Connection conn = null;

	/**
     * Cria ou retorna uma conexão criada previamente.
     * @return Conexão JDBC.
     */
    public static synchronized Connection get() {
    	try {
	        if (conn == null || conn.isClosed()) {
	        	if (driver == null || driver.isBlank())
	        		throw new IllegalArgumentException("Driver não foi definido");
	        	
	        	if (connectionString == null || connectionString.isBlank())
	        		throw new IllegalArgumentException("ConnectionString não foi definida");
	        	
	        	try {
	        		Class.forName(driver);
	        	} catch (Exception e) {
	        		throw new IllegalArgumentException("Driver inválido");
	            }
	                
	            try {
	            	conn = DriverManager.getConnection(connectionString, "", "");
		        } catch (Exception e) {
		            return null;
		        }
	        }
	        
	        return conn;
    	}
    	catch (Exception e) {
    		throw new IllegalArgumentException(e);
        }
    }

	/**
	 * Define o driver e a string de conexão.
	 * 
	 * @param driver Nome do driver.
	 * @param connectionString String de conexão.
	 */
	public static void set(String driver, String connectionString) {
		DBConnection.driver = driver;
		DBConnection.connectionString = connectionString;
	}
	
	public static boolean isAlive() {
		try {
			return conn != null && ! conn.isClosed() && conn.isValid(500);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Fecha a conexão JDBC.
	 */
	public static synchronized void close() {
		if (conn == null)
			throw new IllegalArgumentException("Conexão com BD não existe");

		try {
			if (!conn.isClosed())
				conn.close();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
