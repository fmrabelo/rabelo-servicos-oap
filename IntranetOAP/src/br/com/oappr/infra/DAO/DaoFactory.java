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
 * Classe Factory que trabalha como interface DAO Beans da camada Core de
 * negócio.
 * @author desenvolvimento
 */
public final class DaoFactory
    implements Serializable
{
	private static final long serialVersionUID = 1684584130338296210L;
	private static DaoFactory instance = null;
	private final static String DATASOURCENAME = "java:/comp/env";

	private DaoFactory ()
	{
	}

	private synchronized static void createInstance () throws NamingException
	{
		instance = new DaoFactory();
	}

	public static final DaoFactory getInstance ()
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
	 */
	public final Connection getConection ()
	{
		Connection conn = null;
		try
		{
			final Context initContext = new InitialContext();
			final Context envContext = (Context)initContext.lookup(DATASOURCENAME);
			final DataSource ds = (DataSource)envContext.lookup("jdbc/myoracle");
			conn = ds.getConnection();
			if (conn == null)
			{
				System.err.println("conexao oracle nao criada...");
			}
		}
		catch (NamingException ne)
		{
			ne.printStackTrace();
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
		}
		return conn;
	}

	/**
	 * 
	 */
	public final void testeDB ()
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
				rs = stm.executeQuery("SELECT * FROM PARAMETROS_TEMPORARIOS");
				while ((rs != null) && rs.next())
				{
					System.out.println(rs.getObject(1));
					System.out.println(rs.getObject(2));
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
