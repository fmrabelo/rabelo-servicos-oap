/**
 * 
 */
package br.com.laserviewpr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.laserviewpr.infra.exceptions.InternalException;
import br.com.laserviewpr.infra.util.GenericUtils;
import br.com.laserviewpr.infra.util.Validator;
import br.com.laserviewpr.intranet.vo.FoneVO;
import br.com.laserviewpr.intranet.vo.MedicoVO;
import br.com.laserviewpr.intranet.vo.PessoaVO;
import br.com.laserviewpr.intranet.vo.UsuarioInternoVO;

/**
 * Classe Login DAO responsável pela comunicação com base de dados para
 * processos de Autenticação e recuperar dados de pessoa física
 * (médico/paciente) e Juridica (empresa).
 * @author Rabelo Serviços.
 */
final class PessoaDAO
    implements Serializable, DaoIntranet
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
	 * Autenticação de usuário interno.
	 * @param UsuarioInternoVO user
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	final UsuarioInternoVO autenticarUsuarioWeb (UsuarioInternoVO user)
	    throws InternalException, Exception
	{
		if (user != null)
		{
			final String inPwd = GenericUtils.cryptMD5(user.getSenhaweb());
			user = this.findUsuarioWebById(user.getNrUsuario());
			if (user != null)
			{
				if (inPwd.equals(user.getSenhaweb()))
				{
					// TODO: preencher dados do usuario logado.
					return this.getDadosPessoaUsuarioByNrUsuario(user.getNrUsuario());
				}
			}
			else
			{
				throw new InternalException("Usuário inválido ou não cadastrado.");
			}
			user = null;
		}
		return null;
	}

	/**
	 * Autenticação de usuário interno.
	 * @param UsuarioInternoVO user
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	final UsuarioInternoVO findUsuarioWebById (final Long nroUsuario) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			if ((DaoFactory.getInstance().getConection() != null) && (nroUsuario != null))
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
				final String query = "SELECT T1.IDWEB, T1.NRUSUARIO, T1.SENHAWEB, T1.EMAILWEB FROM SYSADM.acwebacesso T1 WHERE T1.NRUSUARIO = ?1";
				rs = stm.executeQuery(query.replace("?1", nroUsuario.toString()));
				UsuarioInternoVO user = null;
				while ((rs != null) && rs.next())
				{
					user = new UsuarioInternoVO();
					user.setIdweb(rs.getLong("IDWEB"));
					user.setNrUsuario(nroUsuario);
					user.setSenhaweb(rs.getString("SENHAWEB"));
					user.setEmailweb(rs.getString("EMAILWEB"));
				}
				return user;
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
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
		}
		return null;
	}

	/**
	 * Cadastro de usuário interno.
	 * @param UsuarioInternoVO user
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	final UsuarioInternoVO insertUsuarioWeb (final UsuarioInternoVO user)
	    throws InternalException, Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			if ((DaoFactory.getInstance().getConection() != null) && (user != null))
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
				// verificar se é um código válido de colaborador.
				rs = stm.executeQuery("select nrusuario from sysadm.ACUSUARI where nrusuario = "
				    + user.getNrUsuario());
				if ((rs != null) && rs.next())
				{
					// verficar se já existe cadastro web para o colaborador.
					final UsuarioInternoVO userExist = this.findUsuarioWebById(user.getNrUsuario());
					if (userExist != null)
					{
						throw new InternalException(
						    "Atenção: O usuário Interno com código " + user.getNrUsuario()
						        + " já existe no sistema web.");
					}
					final String query = "INSERT INTO SYSADM.acwebacesso (idweb, nrusuario, senhaweb, emailweb) VALUES ( SYSADM.ID_WEB.nextval, ?1, '?2', '?3')";
					user.setSenhaweb(GenericUtils.cryptMD5(user.getSenhaweb()));
					stm.executeUpdate(query.replace("?1", user.getNrUsuario().toString()).replace(
					    "?2", user.getSenhaweb().toString()).replace("?3", user.getEmailweb()));
				}
				else
				{
					throw new InternalException("Atenção: O código " + user.getNrUsuario()
					    + " não pertence a nenhum colaborador Interno.");
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
			throw ex;
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			throw new Exception(th);
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
		}
		return user;
	}

	/**
	 * Alterar Senha de usuário WEB interno.
	 * @param UsuarioInternoVO user
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	final UsuarioInternoVO alterarSenhaUsuarioWeb (final UsuarioInternoVO user)
	    throws InternalException, Exception
	{
		Statement stm = null;
		try
		{
			if ((DaoFactory.getInstance().getConection() != null) && (user != null))
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
				final String query = "UPDATE SYSADM.acwebacesso set SENHAWEB = '?1' where NRUSUARIO = '?2' ";
				user.setSenhaweb(GenericUtils.cryptMD5(user.getSenhaweb()));
				stm.executeUpdate(query.replace("?1", user.getSenhaweb().toString()).replace("?2",
				    user.getNrUsuario().toString()));
			}
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new Exception(sqle);
		}
		catch (Exception ex)
		{
			throw ex;
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			throw new Exception(th);
		}
		finally
		{
			if (stm != null)
			{
				stm.close();
			}
			stm = null;
		}
		return user;
	}

	/**
	 * Pesquisa Pacientes pelo nome.
	 * @param codPessoa
	 * @return List<PessoaVO>
	 * @throws Exception
	 */
	final List<PessoaVO> getPessoaByName (final String nomePessoa) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		List<PessoaVO> list = null;
		PessoaVO p = null;
		try
		{
			if ((DaoFactory.getInstance().getConection() != null)
			    && !Validator.isBlankOrNull(nomePessoa))
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
				final String query = "SELECT T1.CDPESSOA, T1.NMPESSOA, T1.DTNASC FROM SYSADM.ACPESSOA T1 WHERE UPPER(T1.NMPESSOA) LIKE UPPER('?1%') ORDER BY T1.CDPESSOA ASC ".replace(
				    "?1", nomePessoa);
				rs = stm.executeQuery(query);
				list = new ArrayList<PessoaVO>();
				while ((rs != null) && rs.next())
				{
					p = new PessoaVO();
					p.setCdPessoa(rs.getLong("CDPESSOA"));
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setDataNascimento(rs.getDate("DTNASC"));
					list.add(p);
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
			p = null;
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
		}
		return list;
	}

	/**
	 * Retorna os dados da pessoa e dados do usuário usando o código de usuario
	 * (nrusuario).
	 * @param nrusuario
	 * @return
	 * @throws Exception
	 */
	final UsuarioInternoVO getDadosPessoaUsuarioByNrUsuario (final Long nrusuario)
	    throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		UsuarioInternoVO p = null;
		try
		{
			if ((DaoFactory.getInstance().getConection() != null) && (nrusuario != null))
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
				final String qry = "SELECT USU.CDPESSOA, USU.NMUSUARIO, USU.CDUSUARIO, USU.FLMEDICO, PES.NMPESSOA, PES.DSFORMTRAT, PES.DTNASC FROM SYSADM.ACPESSOA PES, SYSADM.ACUSUARI USU WHERE PES.CDPESSOA=USU.CDPESSOA AND USU.NRUSUARIO = "
				    + nrusuario;
				rs = stm.executeQuery(qry);
				p = new UsuarioInternoVO();
				while ((rs != null) && rs.next())
				{
					p.setNrUsuario(nrusuario);
					p.setCdPessoa(rs.getLong("CDPESSOA"));
					p.setNmUsuario(rs.getString("NMUSUARIO"));
					p.setCdUsuario(rs.getString("CDUSUARIO"));
					p.setFlMedico(rs.getInt("FLMEDICO"));
					p.setNomePessoa(rs.getString("NMPESSOA"));
					p.setSiglaTratamento(rs.getString("DSFORMTRAT"));
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
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
		}
		return p;
	}

	/**
	 * Pesquisa Paciente pelo Código de matricula da Pessoa.
	 * @param codPessoa
	 * @return
	 * @throws Exception
	 */
	final PessoaVO getPessoaByCodMatricula (final Long codPessoa) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		PessoaVO p = null;
		try
		{
			if ((DaoFactory.getInstance().getConection() != null) && (codPessoa != null))
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
//				rs = stm.executeQuery("SELECT T1.CDPESSOA, T1.NMPESSOA, T1.DTNASC FROM SYSADM.ACPESSOA T1 WHERE T1.CDPESSOA = "
//				    + codPessoa);
				rs = stm.executeQuery("SELECT T1.tbCodigo, T1.tbNome, T1.tbDtNasc FROM clinic.dbo.clid060 T1 WHERE T1.tbCodigo = "
					    + codPessoa);
				p = new PessoaVO();
				while ((rs != null) && rs.next())
				{
					p.setCdPessoa(rs.getLong("tbCodigo"));
					p.setNomePessoa(rs.getString("tbNome"));
					p.setDataNascimento(rs.getDate("tbDtNasc"));
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
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
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
		try
		{
			if (nroResultado != null)
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
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
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
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
				str.append(" 	         T3.DSFORMTRAT, T3.TPSEXO ,T2.IMAGEMBIN	 ");
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
					p.setAssinaturaDigital(rs.getBlob("IMAGEMBIN"));
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
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
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
		try
		{
			if (DaoFactory.getInstance().getConection() != null)
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
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
					p.setEmail(this.getEmailEmpresa());

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
			if (rs != null)
			{
				rs.close();
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
		}
		return null;
	}

	/**
	 * Pesquisa e retorna o Email da Empresa.
	 * @param codPessoa
	 * @return String Email
	 * @throws Exception
	 */
	private final String getEmailEmpresa () throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			if (DaoFactory.getInstance().getConection() != null)
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
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
			}
			if (stm != null)
			{
				stm.close();
			}
			rs = null;
			stm = null;
		}
		return null;
	}
}
