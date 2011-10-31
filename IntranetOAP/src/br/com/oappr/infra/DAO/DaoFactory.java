package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.PessoaVO;

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

	/**
	 * Pesquisa e retorna pessoa caso exista.
	 * @param codPessoa
	 * @return
	 * @throws Exception
	 */
	public final PessoaVO getAcPessoaByMatricula (final Long codPessoa) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		PessoaVO p = null;
		try
		{
			conn = this.getConection();
			if (conn != null)
			{
				p = new PessoaVO();
				stm = conn.createStatement();
				rs = stm.executeQuery("SELECT T1.CDPESSOA, T1.NMPESSOA, T1.DTNASC FROM SYSADM.ACPESSOA T1 WHERE T1.CDPESSOA = "
				    + codPessoa);
				System.out.println("Query executada com sucesso... impressao de resultado: ");
				while ((rs != null) && rs.next())
				{
					p.setCdPessoa(rs.getLong("CDPESSOA"));
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setDataNascimento(rs.getDate("DTNASC"));
					// System.out.println(rs.getObject(2));
				}
			}
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
		return p;
	}

	/**
	 * lista de Laudos pelo nro da matricula.
	 * @param nroCadastroPaciente
	 * @return
	 * @throws Exception
	 */
	public final List<LaudoVO> getLaudos (final Long nroCadastroPaciente) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		LaudoVO p = null;
		List<LaudoVO> lista = null;
		StringBuilder str = new StringBuilder();
		try
		{
			conn = this.getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				str.append(" SELECT T2.NRREQUISICAO, T1.NRSEQRESULTADO, T1.DTCONSULTA, T1.HRAGENDA,T1.NRUSUARIOAMB, ");
				str.append(" T1.CDPESSOA, T2.CDPROCED, T2.DSEXAMECOMPL, T2.NRLAUDO, T2.NRUSUARIOINC, T2.DHINCLUSAO, ");
				str.append(" T2.NRUSUARIOALT, T2.DHALTERACAO, T1.CDCONVENIO,T2.NRUSUSOLIC, T2.DSRTF ");
				str.append(" FROM SYSADM.AARESULTADO T1, SYSADM.ACREQUISICAO T2 ");
				str.append(" WHERE T1.NRSEQRESULTADO = T2.NRSEQRESULTADO ");
				str.append(" AND T1.TPPRESENCA = 5 ");
				str.append(" AND T2.NRSEQRESULTADO IS NOT NULL ");
				// str.append(" AND T2.TPLAUDO = 2 ");
				str.append(" AND T2.FLLIBERADO = 1 ");
				str.append(" AND T2.CDPESSOA IS NULL ");
				str.append(" AND T1.CDPESSOA = ").append(nroCadastroPaciente);

				System.out.println();
				System.out.println("Query: ");
				System.out.println(str.toString());
				System.out.println();

				rs = stm.executeQuery(str.toString());

				System.out.println("Query executada com sucesso... impressao de resultado: ");
				lista = new ArrayList<LaudoVO>();
				while ((rs != null) && rs.next())
				{
					p = new LaudoVO();
					p.setCdpessoa(rs.getLong("CDPESSOA"));
					p.setDtconsulta(rs.getDate("DTCONSULTA"));
					p.setDsexamecompl(rs.getString("DSEXAMECOMPL"));
					p.setCdproced(rs.getLong("CDPROCED"));
					p.setNrlaudo(rs.getLong("NRLAUDO"));
					lista.add(p);
				}
			}
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
		return lista;
	}
}
