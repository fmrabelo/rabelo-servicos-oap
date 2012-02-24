/**
 * 
 */
package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Classe Connection DAO respons�vel pela conex�o com base de dados para
 * processos do laudo online.
 * @author desenvolvimento
 */
final class ConnectionDAO
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3187540011874205887L;
	private final static String DATASOURCENAME = "java:/comp/env";

	/**
	 * 
	 */
	public ConnectionDAO ()
	{
		super();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public final Connection getConection () throws Exception
	{
		Connection conn = null;
		try
		{
			final Context initContext = new InitialContext();
			final Context envContext = (Context)initContext.lookup(DATASOURCENAME);
			final DataSource ds = (DataSource)envContext.lookup("jdbc/ADCON");
			conn = ds.getConnection();
			if (conn == null)
			{
				System.err.println("ERRO: Conexao oracle (jdbc/ADCON) nao criada...");
			}
			else
			{
				System.err.println("[ OK ] Conexao oracle (jdbc/ADCON) criada com sucesso...");
			}
		}
		catch (NamingException ne)
		{
			ne.printStackTrace();
			throw new Exception(ne);
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new Exception(sqle);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new Exception(ex);
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			throw new Exception(th);
		}
		finally
		{
		}
		return conn;
	}

	/**
	 * M�todo para teste de conex�o com a base de dados.
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