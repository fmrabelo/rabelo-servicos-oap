/**
 * 
 */
package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.oappr.intranet.vo.PessoaVO;

/**
 * Classe Login DAO responsável pela comunicação com base de dados para
 * processos de Autenticação.
 * @author desenvolvimento
 */
final class AutenticacaoDAO
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2804143925142572007L;

	/**
	 * 
	 */
	public AutenticacaoDAO ()
	{
		super();
	}

	/**
	 * Pesquisa e retorna pessoa caso exista.
	 * @param codPessoa
	 * @return
	 * @throws Exception
	 */
	final PessoaVO getAcPessoaByMatricula (final Long codPessoa) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		PessoaVO p = null;
		try
		{
			conn = DaoFactory.getInstance().getConection();
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
}
