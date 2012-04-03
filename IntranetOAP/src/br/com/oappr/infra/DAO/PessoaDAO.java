/**
 * 
 */
package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.oappr.intranet.vo.FoneVO;
import br.com.oappr.intranet.vo.MedicoVO;
import br.com.oappr.intranet.vo.PessoaVO;

/**
 * Classe Login DAO responsável pela comunicação com base de dados para
 * processos de Autenticação e recuperar dados de pessoa física
 * (médico/paciente) e Juridica (empresa).
 * @author desenvolvimento
 */
final class PessoaDAO
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2804143925142572007L;

	/**
	 * 
	 */
	public PessoaDAO ()
	{
		super();
	}

	/**
	 * Pesquisa e retorna Paciente caso exista.
	 * @param codPessoa
	 * @return
	 * @throws Exception
	 */
	final PessoaVO getPessoaByMatricula (final Long codPessoa) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		PessoaVO p = null;
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if ((conn != null) && (codPessoa != null))
			{
				stm = conn.createStatement();
				rs = stm.executeQuery("SELECT T1.CDPESSOA, T1.NMPESSOA, T1.DTNASC FROM SYSADM.ACPESSOA T1 WHERE T1.CDPESSOA = "
				    + codPessoa);
				p = new PessoaVO();
				while ((rs != null) && rs.next())
				{
					p.setCdPessoa(rs.getLong("CDPESSOA"));
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setDataNascimento(rs.getDate("DTNASC"));
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
			DaoFactory.getInstance().closeConection(stm, rs, conn);
		}
		return p;
	}

	/**
	 * Pesquisa e retorna Medico solicitante caso exista.
	 * @param nroResultado
	 * @return PessoaVO
	 * @throws Exception
	 */
	final PessoaVO getMedicoSolicitante (final Long nroResultado) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if ((conn != null) && (nroResultado != null))
			{
				stm = conn.createStatement();
				final StringBuilder str = new StringBuilder();
				str.append(" SELECT T2.DSFORMTRAT || ' ' || T2.NMPESSOA NOME ");
				str.append(" 	FROM SYSADM.AARESULTADO T1, SYSADM.ACPESSOA T2 ");
				str.append("   WHERE T1.CDORIGEM = T2.CDPESSOA ");
				str.append("     AND T1.NRSEQRESULTADO = ").append(nroResultado);

				rs = stm.executeQuery(str.toString());
				final PessoaVO p = new PessoaVO();
				while ((rs != null) && rs.next())
				{
					p.setCdPessoa(nroResultado);
					p.setNomePessoa(rs.getString("NOME"));
				}
				return p;
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
			DaoFactory.getInstance().closeConection(stm, rs, conn);
		}
		return null;
	}

	/**
	 * Pesquisa e retorna Medico responsável pela execução do Exame / Laudo.
	 * @param nroRequisicao
	 * @param nroResultado
	 * @return MedicoVO
	 * @throws Exception
	 */
	final MedicoVO getMedicoResponsavelPorLaudo (final Long nroRequisicao, final Long nroResultado)
	    throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				final StringBuilder str = new StringBuilder();
				str.append(" 	Select   T1.NRSEQMEDICO, T4.CDPESSOA, T1.NRUSUARIO, T1.FLREVISOR, T3.NMPESSOA, T2.DSORGCLAS,	 ");
				str.append(" 	         T3.DSFORMTRAT, T3.TPSEXO ,T2.IMAGEMASSINATURA	 ");
				str.append(" 	From    SYSADM.ACREQUISICAOMEDICO T1, SYSADM.ACUSUARI T2,	 ");
				str.append(" 	        SYSADM.ACPESSOA T3, SYSADM.AARESULTADOPR T4	 ");
				str.append(" 	WHERE  T1.NRUSUARIO = T2.NRUSUARIO	 ");
				str.append(" 	And    T2.CDPESSOA = T3.CDPESSOA	 ");
				str.append(" 	And    T3.CDPESSOA = T4.CDPESSOA	 ");
				str.append(" 	AND T1.NRREQUISICAO = ").append(nroRequisicao);
				str.append(" 	AND T4.NRSEQRESULTADO = ").append(nroResultado);
				str.append(" 	ORDER BY NRSEQMEDICO	 ");

				rs = stm.executeQuery(str.toString());
				if ((rs != null) && rs.next())
				{
					final MedicoVO p = new MedicoVO();
					p.setCdPessoa(rs.getLong("NRUSUARIO"));
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setDescrCRM(rs.getString("DSORGCLAS"));
					p.setSiglaTratamento(rs.getString("DSFORMTRAT"));
					p.setAssinaturaDigital(rs.getBlob("IMAGEMASSINATURA"));
					p.setSexo(rs.getString("TPSEXO"));
					return p;
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
			DaoFactory.getInstance().closeConection(stm, rs, conn);
		}
		return null;
	}

	/**
	 * Pesquisa e retorna os dados da Clínica como pessoa Jurídica.
	 * @return
	 * @throws Exception
	 */
	final PessoaVO getDadosEmpresa () throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				final StringBuilder str = new StringBuilder();
				str.append(" 	Select CL.NMPESSOA, CL.DSENDERECO, CL.DSREFEREN, CL.DSTELEFONE, CL.DSFAX, CL.NRDDD, CL.DSBAIRRO,	 ");
				str.append(" 	        CL.SGESTADO, CL.CDCIDADE, CL.NRCEP, CL.SGPAIS, CD.NMCIDADE	 ");
				str.append(" 		from SYSADM.MBCLIENTE CL, SYSADM.MBCIDADE CD	 ");
				str.append(" 	where CL.CDPESSOA = 0	 ");
				str.append(" 		AND CL.CDCIDADE = CD.CDCIDADE	 ");
				str.append(" 		AND CL.SGPAIS = CL.SGPAIS	 ");
				str.append(" 		AND CL.SGESTADO= CL.SGESTADO	 ");
				rs = stm.executeQuery(str.toString());
				if ((rs != null) && rs.next())
				{
					final PessoaVO p = new PessoaVO();
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setEndereco(rs.getString("DSENDERECO"));
					p.setBairro(rs.getString("DSBAIRRO"));
					p.setUf(rs.getString("SGESTADO"));
					p.setCidade(rs.getString("NMCIDADE"));
					p.setCep(rs.getString("NRCEP"));
					p.setComplEndereco(rs.getString("DSREFEREN"));
					p.setEmail(this.getEmailEmpresa(conn));

					FoneVO fone = new FoneVO();
					fone.setDdd(rs.getString("NRDDD"));
					fone.setNro(rs.getString("DSTELEFONE"));

					final List<FoneVO> listFone = new ArrayList<FoneVO>();
					listFone.add(fone);
					// fone = new FoneVO();
					// fone.setDdd(rs.getString("NRDDD"));
					// fone.setNro(rs.getString("DSFAX"));
					// listFone.add(fone);

					p.setListaFone(listFone);
					return p;
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
			DaoFactory.getInstance().closeConection(stm, rs, conn);
		}
		return null;
	}

	/**
	 * Pesquisa e retorna o Email da Empresa.
	 * @param codPessoa
	 * @return String Email
	 * @throws Exception
	 */
	private final String getEmailEmpresa (final Connection conn) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			if (conn != null)
			{
				stm = conn.createStatement();
				rs = stm.executeQuery("SELECT DSEMAIL FROM SYSADM.ACPESSOA T1, SYSADM.ACPREFERENCES T2 WHERE T1.CDPESSOA = T2.CDINSTITUICAO ");
				if ((rs != null) && rs.next())
				{
					return rs.getString("DSEMAIL");
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
		}
		return null;
	}
}
