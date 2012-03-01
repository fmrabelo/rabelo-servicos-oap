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
 * Classe Laudo DAO responsável pela comunicação com base de dados para
 * processos do laudo.
 * @author desenvolvimento
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
	 * Método responsável por listar todos Laudos pelo nro da matricula do
	 * paciente ou retornar um laudo especifico pelo parametro codigo do laudo.
	 * @param nroCadastroPaciente
	 * @return List<LaudoVO>
	 * @throws Exception
	 */
	final List<LaudoVO> getLaudos (final Long nroCadastroPaciente, final Long codigoLaudo)
	    throws Exception
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
					str.append(" AND T2.NRSEQRESULTADO = ").append(codigoLaudo);
				}
				str.append(" 	 AND T2.TPLAUDO = 2 ");// Laudo tipo RTF.
				str.append(" 	 AND T2.FLLIBERADO = 1 ");
				str.append(" 	 AND T2.CDPESSOA IS NULL ");
				str.append(" 	 AND T1.CDPESSOA = ").append(nroCadastroPaciente);
				str.append(" ORDER BY T1.DTCONSULTA DESC ");

				// System.out.println();
				// System.out.println("Query: ");
				// System.out.println(str.toString());
				// System.out.println();

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
					// blob para o laudo.
					p.setDsrtf(rs.getBlob("DSRTF"));
					// p.setDsrtf_str(rs.getString("DSRTF_STR"));
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
