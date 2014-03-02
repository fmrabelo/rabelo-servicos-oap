package br.com.laserviewpr.infra.DAO;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.naming.NamingException;

import br.com.laserviewpr.infra.exceptions.InternalException;
import br.com.laserviewpr.intranet.vo.AgendaMedicaVO;
import br.com.laserviewpr.intranet.vo.LaudoVO;
import br.com.laserviewpr.intranet.vo.MedicoVO;
import br.com.laserviewpr.intranet.vo.PessoaVO;
import br.com.laserviewpr.intranet.vo.UsuarioInternoVO;

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
	// public final void closeConection (Statement stm, ResultSet rs, Connection
	// conn)
	// throws Exception
	// {
	// ConnectionDAO.getInstance().closeConection(stm, rs, conn);
	// }
	/**
	 * Fecha fluxo e conexão com base de dados.
	 * @param stm
	 * @param rs
	 * @param conn
	 * @throws Exception
	 */
	public final void closeConection (Statement stm, ResultSet rs) throws Exception
	{
		ConnectionDAO.getInstance().closeConection(stm, rs);
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
	 * Pesquisa pessoa do tipo Paciente pelo código de matrícula.
	 * @param codPessoa
	 * @return PessoaVO
	 * @throws Exception
	 */
	public final PessoaVO getPacienteByCodMatricula (final Long codPessoa) throws Exception
	{
		return new PessoaDAO().getPessoaByCodMatricula(codPessoa);
	}

	/**
	 * Retorna os dados da pessoa e dados do usuário usando o código de usuario
	 * (nrusuario).
	 * @param nrusuario
	 * @return
	 * @throws Exception
	 */
	public final UsuarioInternoVO getDadosPessoaUsuarioByNrUsuario (final Long nrusuario)
	    throws Exception
	{
		return new PessoaDAO().getDadosPessoaUsuarioByNrUsuario(nrusuario);
	}

	/**
	 * Pesquisa pessoa do tipo Paciente pelo nome.
	 * @param nomePessoa
	 * @return List<PessoaVO>
	 * @throws Exception
	 */
	public final List<PessoaVO> getPessoaByName (final String nomePessoa) throws Exception
	{
		return new PessoaDAO().getPessoaByName(nomePessoa);
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

	/**
	 * Autenticar Colaborador da Interno.
	 * @param user UsuarioInternoVO
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	public final UsuarioInternoVO autenticarUsuarioWeb (UsuarioInternoVO user)
	    throws InternalException, Exception
	{
		return new PessoaDAO().autenticarUsuarioWeb(user);
	}

	/**
	 * Pesquisa por usuário WEB.
	 * @param user UsuarioInternoVO
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	public final UsuarioInternoVO findUsuarioWebById (final Long nroUsuario)
	    throws InternalException, Exception
	{
		return new PessoaDAO().findUsuarioWebById(nroUsuario);
	}

	/**
	 * Autenticar Colaborador.
	 * @param user UsuarioInternoVO
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	public final UsuarioInternoVO insertUsuarioWeb (UsuarioInternoVO user)
	    throws Exception, InternalException
	{
		return new PessoaDAO().insertUsuarioWeb(user);
	}

	/**
	 * Atualizar senha de Colaborador.
	 * @param user UsuarioInternoVO
	 * @return UsuarioInternoVO
	 * @throws Exception
	 */
	public final UsuarioInternoVO alterarSenhaUsuarioWeb (UsuarioInternoVO user)
	    throws Exception, InternalException
	{
		return new PessoaDAO().alterarSenhaUsuarioWeb(user);
	}

	/**
	 * Método responsável pela pesquisa de Agenda usando como parametros o nro
	 * do usuário (médico) e a data da agenda.
	 * @param nroUsuario do médico
	 * @param data da agenda
	 * @return List<AgendaMedicaVO>
	 * @throws Exception
	 */
	public final List<AgendaMedicaVO> getAgendaMedica (final Long nroUsuario, final String data)
	    throws Exception
	{
		final AgendaDAO agenda = new AgendaDAO();
		return agenda.getAgendaMedica(nroUsuario, data);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public final List<LaudoVO> getLaudosLixo () throws Exception
	{
		final LaudoDAO laudo = new LaudoDAO();
		return laudo.getLaudosLixo();
	}

	@Deprecated
	public final void gravaImagem (final File file)
	{
		final LaudoDAO laudo = new LaudoDAO();
		laudo.gravaImagem(file);
	}
}
