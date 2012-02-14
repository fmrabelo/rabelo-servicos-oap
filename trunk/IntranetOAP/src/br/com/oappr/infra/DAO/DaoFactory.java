package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.naming.NamingException;

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
	 * Pesquisa e retorna pessoa caso exista.
	 * @param codPessoa
	 * @return PessoaVO
	 * @throws Exception
	 */
	public final PessoaVO getAcPessoaByMatricula (final Long codPessoa) throws Exception
	{
		final AutenticacaoDAO aut = new AutenticacaoDAO();
		return aut.getAcPessoaByMatricula(codPessoa);
	}

	/**
	 * Método responsável por listar todos Laudos pelo nro da matricula do
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
