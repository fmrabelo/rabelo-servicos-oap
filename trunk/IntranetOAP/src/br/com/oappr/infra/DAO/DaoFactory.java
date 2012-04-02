package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.naming.NamingException;

import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.MedicoVO;
import br.com.oappr.intranet.vo.PessoaVO;

/**
 * Classe Factory que trabalha como interface DAO Beans da camada Core de
 * neg�cio.
 * @author desenvolvimento
 */
public final class DaoFactory
    implements Serializable
{
	private static final long serialVersionUID = 1684584130338296210L;
	private static DaoFactory instance = null;

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
		final ConnectionDAO con = new ConnectionDAO();
		return con.getConection();
	}

	/**
	 * Fecha fluxo e conex�o com base de dados.
	 * @param stm
	 * @param rs
	 * @param conn
	 * @throws Exception
	 */
	public final void closeConection (Statement stm, ResultSet rs, Connection conn)
	    throws Exception
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
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Pesquisa e retorna os dados da Cl�nica como pessoa Jur�dica.
	 * @return
	 * @throws Exception
	 */
	public final PessoaVO getDadosEmpresa () throws Exception
	{
		final PessoaDAO medico = new PessoaDAO();
		return medico.getDadosEmpresa();
	}

	/**
	 * Pesquisa e retorna Medico respons�vel pela execu��o do exame.
	 * @param nroRequisicao
	 * @param codProcedimento
	 * @param nroLaudo
	 * @return MedicoVO
	 * @throws Exception
	 */
	public final MedicoVO getMedicoResponsavelPorExame (final Long nroRequisicao,
	    final Long codProcedimento, final Long nroLaudo) throws Exception
	{
		final PessoaDAO medico = new PessoaDAO();
		return medico.getMedicoResponsavelPorExame(nroRequisicao, codProcedimento, nroLaudo);
	}

	/**
	 * Pesquisa e retorna pessoa do tipo Medico solicitante do exame.
	 * @param codPessoa
	 * @return PessoaVO
	 * @throws Exception
	 */
	public final PessoaVO getMedicoSolicitante (final Long codPessoa) throws Exception
	{
		final PessoaDAO medico = new PessoaDAO();
		return medico.getMedicoSolicitante(codPessoa);
	}

	/**
	 * Pesquisa e retorna pessoa do tipo Paciente caso exista.
	 * @param codPessoa
	 * @return PessoaVO
	 * @throws Exception
	 */
	public final PessoaVO getAcPacienteByMatricula (final Long codPessoa) throws Exception
	{
		final PessoaDAO paciente = new PessoaDAO();
		return paciente.getAcPessoaByMatricula(codPessoa);
	}

	/**
	 * M�todo respons�vel por listar todos Laudos pelo nro da matricula do
	 * paciente ou retornar um laudo especifico pelo parametro codigo do laudo.
	 * @param nroCadastroPaciente
	 * @return List<LaudoVO>
	 * @throws Exception
	 */
	public final List<LaudoVO> getLaudos (final Long nroCadastroPaciente, final Long codigoLaudo)
	    throws Exception
	{
		final LaudoDAO laudo = new LaudoDAO();
		return laudo.getLaudos(nroCadastroPaciente, codigoLaudo);
	}
}
