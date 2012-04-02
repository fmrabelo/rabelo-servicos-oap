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
	 * @param codPessoa
	 * @return
	 * @throws Exception
	 */
	final PessoaVO getMedicoSolicitante (final Long codPessoa) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		final PessoaVO p = new PessoaVO();
		final StringBuilder str = new StringBuilder();
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				str.append(" 	SELECT DSFORMTRAT || ' ' || NMPESSOA as NOME ");
				str.append(" 		FROM SYSADM.ACPESSOA	 ");
				str.append(" 	WHERE CDPESSOA = ").append(codPessoa);
				str.append(" 	UNION	 ");
				str.append(" 	SELECT DSORIGEM	 ");
				str.append(" 		FROM SYSADM.MBORIGEM	 ");
				str.append(" 	WHERE CDORIGEM = ").append(codPessoa);

				rs = stm.executeQuery(str.toString());
				while ((rs != null) && rs.next())
				{
					p.setCdPessoa(codPessoa);
					p.setNomePessoa(rs.getString("NOME"));
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
	 * Pesquisa e retorna Medico responsável pela execução do exame.
	 * @param nroRequisicao
	 * @param codProcedimento
	 * @param nroLaudo
	 * @return MedicoVO
	 * @throws Exception
	 */
	final MedicoVO getMedicoResponsavelPorExame (final Long nroRequisicao,
	    final Long codProcedimento, final Long nroLaudo) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		final MedicoVO p = new MedicoVO();
		final StringBuilder str = new StringBuilder();
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				str.append(" 	Select T1.NRUSUARIO, T3.NMPESSOA, T2.DSORGCLAS, ");
				str.append(" 			DSFORMTRAT, FLREVISOR, IMAGEMASSINATURA, TPSEXO ");
				str.append(" 		From ACREQUISICAOMEDICO T1, ACUSUARI T2, ACPESSOA T3 ");
				str.append(" 	  Where NRREQUISICAO = 	").append(nroRequisicao);
				str.append(" 			And T1.NRUSUARIO = T2.NRUSUARIO	 ");
				str.append(" 			And T2.CDPESSOA = T3.CDPESSOA	 ");
				str.append(" 			And T1.CDPROCED = ").append(codProcedimento);
				str.append(" 			And T1.NRLAUDO = ").append(nroLaudo);
				str.append(" 			And FLREVISOR = 0 ");
				str.append(" 	ORDER BY NRSEQMEDICO ");

				rs = stm.executeQuery(str.toString());
				while ((rs != null) && rs.next())
				{
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
		return p;
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
		final PessoaVO p = new PessoaVO();
		final StringBuilder str = new StringBuilder();
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if (conn != null)
			{
				stm = conn.createStatement();
				str.append(" 	Select CL.NMPESSOA, CL.DSENDERECO, CL.DSTELEFONE, CL.DSFAX, CL.NRDDD, CL.DSBAIRRO,	 ");
				str.append(" 	        CL.SGESTADO, CL.CDCIDADE, CL.NRCEP, CL.SGPAIS, CD.NMCIDADE	 ");
				str.append(" 		from SYSADM.MBCLIENTE CL, SYSADM.MBCIDADE CD	 ");
				str.append(" 	where CL.CDPESSOA = 0	 ");
				str.append(" 		AND CL.CDCIDADE = CD.CDCIDADE	 ");
				str.append(" 		AND CL.SGPAIS = CL.SGPAIS	 ");
				str.append(" 		AND CL.SGESTADO= CL.SGESTADO	 ");
				rs = stm.executeQuery(str.toString());
				FoneVO fone = null;
				final List<FoneVO> listFone = new ArrayList<FoneVO>();
				while ((rs != null) && rs.next())
				{
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setEndereco(rs.getString("DSENDERECO"));
					p.setBairro(rs.getString("DSBAIRRO"));
					p.setUf(rs.getString("SGESTADO"));
					p.setCidade(rs.getString("NMCIDADE"));
					p.setCep(rs.getString("NRCEP"));
					p.setEndereco(rs.getString("SGPAIS"));

					fone = new FoneVO();
					fone.setDdd(rs.getString("NRDDD"));
					fone.setNro(rs.getString("DSTELEFONE"));
					listFone.add(fone);
					fone = new FoneVO();
					fone.setDdd(rs.getString("NRDDD"));
					fone.setNro(rs.getString("DSFAX"));
					listFone.add(fone);

					p.setListaFone(listFone);
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
}
