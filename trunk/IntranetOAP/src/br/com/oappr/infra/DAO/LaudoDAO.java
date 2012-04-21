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

import br.com.oappr.intranet.vo.LaudoVO;

/**
 * Classe Laudo DAO respons�vel pela comunica��o com base de dados para
 * processos do laudo.
 * @author Rabelo Servi�os.
 */
final class LaudoDAO
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3185442711874205527L;

	/**
	 * 
	 */
	public LaudoDAO ()
	{
		super();
	}

	/**
	 * M�todo respons�vel por listar todos Laudos pelo nro da matricula do
	 * paciente ou retornar um laudo especifico adicionando os parametros:
	 * codigo do laudo e n�mero da requisi��o.
	 * @param nroCadastroPaciente
	 * @param codigoLaudo
	 * @param nroRequisicao
	 * @return List<LaudoVO>
	 * @throws Exception
	 */
	final List<LaudoVO> getLaudos (final Long nroCadastroPaciente, final Long codigoLaudo,
	    final Long nroRequisicao) throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		Connection conn = null;
		LaudoVO p = null;
		List<LaudoVO> lista = null;
		StringBuilder str = new StringBuilder();
		try
		{
			conn = DaoFactory.getInstance().getConection();
			if (conn != null)
			{

				stm = conn.createStatement();
				str.append(" SELECT T2.NRREQUISICAO, T1.NRSEQRESULTADO, T1.DTCONSULTA, T1.HRAGENDA,T1.NRUSUARIOAMB, ");
				str.append(" 	T1.CDPESSOA, T2.CDPROCED, T2.DSEXAMECOMPL, T2.NRLAUDO, T2.NRUSUARIOINC, T2.DHINCLUSAO, ");
				str.append(" 	T2.NRUSUARIOALT, T2.DHALTERACAO, T1.CDCONVENIO,T2.NRUSUSOLIC, T2.DSRTF ");
				str.append(" FROM SYSADM.AARESULTADO T1, SYSADM.ACREQUISICAO T2 ");
				str.append("  WHERE T1.NRSEQRESULTADO = T2.NRSEQRESULTADO ");
				str.append(" 	 AND T1.TPPRESENCA = 5 ");
				str.append(" 	 AND T2.NRSEQRESULTADO IS NOT NULL ");
				if ((codigoLaudo != null) && (codigoLaudo.longValue() > 0))
				{
					// --pesquisar laudo espec�fico
					str.append(" AND T2.NRSEQRESULTADO = ").append(codigoLaudo);
					str.append(" AND T2.NRREQUISICAO = ").append(nroRequisicao);
				}
				str.append(" 	 AND T2.TPLAUDO = 2 ");// 2=Laudo tipo RTF.
				str.append(" 	 AND T2.FLLIBERADO = 1 ");// laudo liberado
				str.append(" 	 AND T1.CDPESSOA = ").append(nroCadastroPaciente);// paciente
				str.append(" ORDER BY T1.DTCONSULTA DESC ");

				rs = stm.executeQuery(str.toString());

				lista = new ArrayList<LaudoVO>();
				while ((rs != null) && rs.next())
				{
					p = new LaudoVO();
					p.setCdpessoa(rs.getLong("CDPESSOA"));
					p.setDtconsulta(rs.getDate("DTCONSULTA"));
					p.setDsexamecompl(rs.getString("DSEXAMECOMPL"));
					p.setCdproced(rs.getLong("CDPROCED"));
					p.setNrlaudo(rs.getLong("NRLAUDO"));
					p.setNrrequisicao(rs.getLong("NRREQUISICAO"));
					p.setNrseqresultado(rs.getLong("NRSEQRESULTADO"));
					p.setNrusuarioamb(rs.getLong("NRUSUARIOAMB"));
					p.setCdpessoa(rs.getLong("CDPESSOA"));
					p.setCdproced(rs.getLong("CDPROCED"));
					p.setNrlaudo(rs.getLong("NRLAUDO"));
					p.setNrusuarioinc(rs.getLong("NRUSUARIOINC"));
					p.setNrusuarioalt(rs.getLong("NRUSUARIOALT"));
					p.setCdconvenio(rs.getLong("CDCONVENIO"));
					p.setNrususolic(rs.getLong("NRUSUSOLIC"));
					p.setDsexamecompl(rs.getString("DSEXAMECOMPL"));
					p.setDhinclusao(rs.getDate("DHINCLUSAO"));
					p.setDtconsulta(rs.getDate("DTCONSULTA"));
					p.setHragenda(rs.getDate("HRAGENDA"));
					p.setDhalteracao(rs.getDate("DHALTERACAO"));
					// blob para o laudo especifico.
					if ((codigoLaudo != null) && (codigoLaudo.longValue() > 0))
					{
						p.setDsrtf(rs.getBlob("DSRTF"));
					}
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
			DaoFactory.getInstance().closeConection(stm, rs, conn);
		}
		return lista;
	}
}