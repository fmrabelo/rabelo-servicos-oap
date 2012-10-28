/**
 * 
 */
package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Classe Singleton responsável pela conexão com base de dados para processos do
 * laudo online.
 * @author Rabelo Serviços.
 */
final class ConnectionDAO
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3187540011874205887L;
	private final static String DATASOURCENAME = "java:/comp/env";
	private static ConnectionDAO instance = null;

	/**
	 * construtor privado garante que não será instanciado externamente a não
	 * ser via reflection.
	 */
	private ConnectionDAO ()
	{
		super();
	}

	/**
	 * @throws NamingException
	 */
	private synchronized static void createInstance () throws NamingException
	{
		instance = new ConnectionDAO();
	}

	/**
	 * @return ConnectionDAO
	 */
	public static final ConnectionDAO getInstance ()
	{
		if (instance == null)
		{
			try
			{
				createInstance();
			}
			catch (NamingException ne)
			{
				ne.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	final Connection getConection () throws Exception
	{
		Connection conn = null;
		System.out.printf("%n> Executando Classe ConnectionDAO.getConection() %n");
		DatabaseMetaData db = null;
		final String erroJDBC = "> ERRO: Conexao oracle (jdbc/ADCON) nao criada...";
		try
		{
			final Context initContext = new InitialContext();
			final Context envContext = (Context)initContext.lookup(DATASOURCENAME);
			final DataSource ds = (DataSource)envContext.lookup("jdbc/ADCON");
			conn = ds.getConnection();
			if (conn == null)
			{
				System.err.println(erroJDBC);
			}
			else
			{
				System.err.println("[ OK ] Conexao oracle (jdbc/ADCON) criada com sucesso...");
				db = conn.getMetaData();
				if (db != null)
				{
					System.err.printf("  URL: %s%n  USER: %s%n", db.getURL(), db.getUserName());
				}
			}
		}
		catch (NamingException ne)
		{
			ne.printStackTrace();
			throw new Exception(erroJDBC);
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new Exception(erroJDBC);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new Exception(erroJDBC);
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			throw new Exception(erroJDBC);
		}
		finally
		{
			db = null;
		}
		return conn;
	}

	/**
	 * Fecha fluxo e conexão com base de dados.
	 * @param stm
	 * @param rs
	 * @param conn
	 * @throws Exception
	 */
	final void closeConection (Statement stm, ResultSet rs, Connection conn) throws Exception
	{
		try
		{
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
			if (stm != null)
			{
				stm.close();
				stm = null;
			}
			if (conn != null)
			{
				conn.close();
				conn = null;
			}
			System.err.println("[ OK ] Conexao fechada com sucesso...");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Método para teste de conexão com a base de dados.
	 */
	public final void testeConnectionDB ()
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		try
		{
			conn = this.getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				rs = stm.executeQuery("SELECT count(T1.*) FROM SYSADM.ACPESSOA T1");
				System.out.println("Query executada com sucesso... impressao de resultado: ");
				while ((rs != null) && rs.next())
				{
					System.out.println(rs.getObject(1));
					// System.out.println(rs.getObject(2));
				}
			}
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		catch (Throwable th)
		{
			th.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (stm != null)
				{
					stm.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			rs = null;
			stm = null;
		}
	}
}
