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
 * negócio.
 * @author Rabelo Serviços.
 */
public final class DaoFactory
    implements Serializable
{
	private static final long serialVersionUID = 1684584130338296210L;
	private static DaoFactory instance = null;

	/**
	 * construtor privado garante que não será instanciado externamente a não
	 * ser via reflection.
	 */
	private DaoFactory ()
	{
	}

	/**
	 * @throws NamingException
	 */
	private synchronized static void createInstance () throws NamingException
	{
		instance = new DaoFactory();
	}

	/**
	 * @return
	 */
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
		return ConnectionDAO.getInstance().getConection();
	}

	/**
	 * Fecha fluxo e conexão com base de dados.
	 * @param stm
	 * @param rs
	 * @param conn
	 * @throws Exception
	 */
	public final void closeConection (Statement stm, ResultSet rs, Connection conn)
	    throws Exception
	{
		ConnectionDAO.getInstance().closeConection(stm, rs, conn);
	}

	/**
	 * Pesquisa e retorna os dados da Clínica como pessoa Jurídica.
	 * @return
	 * @throws Exception
	 */
	public final PessoaVO getDadosEmpresa () throws Exception
	{
		final PessoaDAO empresa = new PessoaDAO();
		return empresa.getDadosEmpresa();
	}

	/**
	 * Pesquisa e retorna Medico responsável pela execução do exame.
	 * @param nroRequisicao
	 * @param nroResultado
	 * @return MedicoVO
	 * @throws Exception
	 */
	public final MedicoVO getMedicoResponsavelPorExame (final Long nroRequisicao,
	    final Long nroResultado) throws Exception
	{
		final PessoaDAO medico = new PessoaDAO();
		return medico.getMedicoResponsavelPorLaudo(nroRequisicao, nroResultado);
	}

	/**
	 * Pesquisa e retorna pessoa do tipo Medico solicitante do exame.
	 * @param nroResultado
	 * @return PessoaVO
	 * @throws Exception
	 */
	public final PessoaVO getMedicoSolicitante (final Long nroResultado) throws Exception
	{
		final PessoaDAO medico = new PessoaDAO();
		return medico.getMedicoSolicitante(nroResultado);
	}

	/**
	 * Pesquisa e retorna pessoa do tipo Paciente caso exista.
	 * @param codPessoa
	 * @return PessoaVO
	 * @throws Exception
	 */
	public final PessoaVO getPacienteByMatricula (final Long codPessoa) throws Exception
	{
		final PessoaDAO paciente = new PessoaDAO();
		return paciente.getPessoaByMatricula(codPessoa);
	}

	/**
	 * Método responsável por listar todos Laudos pelo nro da matricula do
	 * paciente ou retornar um laudo especifico adicionando os parametros:
	 * codigo do laudo e número da requisição.
	 * @param nroCadastroPaciente
	 * @param codigoLaudo
	 * @param nroRequisicao
	 * @return List<LaudoVO>
	 * @throws Exception
	 */
	public final List<LaudoVO> getLaudos (final Long nroCadastroPaciente, final Long codigoLaudo,
	    final Long nroRequisicao) throws Exception
	{
		final LaudoDAO laudo = new LaudoDAO();
		return laudo.getLaudos(nroCadastroPaciente, codigoLaudo, nroRequisicao);
	}
}
