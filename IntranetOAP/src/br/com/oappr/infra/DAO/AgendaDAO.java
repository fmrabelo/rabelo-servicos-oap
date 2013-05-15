/**
 * 
 */
package br.com.oappr.infra.DAO;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.infra.util.Validator;
import br.com.oappr.intranet.vo.AgendaMedicaVO;

/**
 * Classe Laudo DAO responsável pela comunicação com base de dados para
 * processos da Agenda Médica.
 * @author Rabelo Serviços.
 */
final class AgendaDAO
    implements Serializable, DaoOap
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3197332054578745698L;

	/**
	 * 
	 */
	public AgendaDAO ()
	{
		super();
	}

	/**
	 * Método responsável pela pesquisa de Agenda usando como parametros o nro
	 * do usuário (médico) e a data da agenda.
	 * @param nroUsuario do médico
	 * @param data da agenda
	 * @return List<AgendaMedicaVO>
	 * @throws Exception
	 */

	final List<AgendaMedicaVO> getAgendaMedica (final Long nroUsuario, final String data)
	    throws Exception
	{
		Statement stm = null;
		ResultSet rs = null;
		AgendaMedicaVO p = null;
		List<AgendaMedicaVO> lista = null;
		StringBuilder str = new StringBuilder();
		try
		{
			if (DaoFactory.getInstance().getConection() != null)
			{
				stm = DaoFactory.getInstance().getConection().createStatement();
				// pesquisa agenda por data e nro usuario (médico).
				str = new StringBuilder();
				str.append("	SELECT T1.DTCONSULTA,	");
				str.append("	      TO_CHAR(T1.HRAGENDA, 'HH24:MI') HRAGENDA,	");
				str.append("	       T1.NRUSUARIO,	");
				str.append("	       T1.CDPESSOA,	");
				str.append("	       TO_CHAR(T1.DSOBSAGENDA) DSOBSAGENDA,	");
				str.append("	       T1.TPVAGA,	");
				str.append("	       T1.FLCONFIRMADO,	");
				str.append("	       T2.NMUSUARIO,	");
				str.append("	       T3.NMPESSOA,      	");
				str.append("	       T5.DSCONVENIO	");
				str.append("	FROM    SYSADM.ACAGENDA T1,	");
				str.append("	        SYSADM.ACUSUARI T2,	");
				str.append("	        SYSADM.ACPESSOA T3,	");
				str.append("	        SYSADM.ACCONVENIO T5	");
				str.append("	WHERE T1.NRUSUARIO = ").append(nroUsuario);
				str.append("	      AND T1.DTCONSULTA = TO_DATE('").append(data).append("') ");
				str.append("	      AND T1.NRUSUARIO = T2.NRUSUARIO ");
				str.append("	      AND T1.CDPESSOA = T3.CDPESSOA ");
				str.append("	      AND T1.CDCONVENIO = T5.CDCONVENIO	");
				str.append("	      ORDER BY T1.HRAGENDA	");

				rs = stm.executeQuery(str.toString());
				lista = new ArrayList<AgendaMedicaVO>();
				while ((rs != null) && rs.next())
				{
					p = new AgendaMedicaVO();
					p.setDtconsulta(rs.getDate("DTCONSULTA"));
					p.setHragenda(rs.getString("HRAGENDA"));
					p.setNrusuario(rs.getLong("NRUSUARIO"));
					p.setCdpessoa(rs.getLong("CDPESSOA"));
					p.setDsosbagenda(rs.getString("DSOBSAGENDA"));
					p.setTpvaga(rs.getString("TPVAGA"));
					p.setFlconfirmado(rs.getInt("FLCONFIRMADO"));
					p.setNmusuario(rs.getString("NMUSUARIO"));
					p.setNmpessoa(rs.getString("NMPESSOA"));
					p.setDsconvenio(rs.getString("DSCONVENIO"));
					lista.add(p);
				}
				// pesquisa telefones
				if (Validator.notEmptyCollection(lista))
				{
					getFone(lista);
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
			str = null;
			p = null;
			DaoFactory.getInstance().closeConection(stm, rs);
		}
		return lista;
	}

	/**
	 * Retorna Fone de usuário especifico.
	 * @param data
	 * @param nroUsuario
	 * @param cdpessoa
	 * @return String fone.
	 * @throws Exception
	 */
	private final static void getFone (final List<AgendaMedicaVO> lista) throws Exception
	{
		PreparedStatement stm = null;
		ResultSet rs = null;
		StringBuilder qry = new StringBuilder();
		StringBuilder fones = null;
		try
		{
			if (DaoFactory.getInstance().getConection() != null)
			{
				// pesquisa fones de usuário especifico.
				qry = new StringBuilder();
				qry.append("	SELECT DISTINCT  (T4.NRDDD || ' ' || T4.DSTELEFONE) FONE ");
				qry.append("	FROM    SYSADM.ACAGENDA T1, SYSADM.ACTELEFONE T4 ");
				qry.append("	WHERE T1.NRUSUARIO = ? ");
				qry.append("	AND T1.CDPESSOA = ? ");
				qry.append("	AND T1.DTCONSULTA = TO_DATE( ? ) ");
				qry.append("	AND T1.CDPESSOA = T4.CDPESSOA(+) ");
				stm = DaoFactory.getInstance().getConection().prepareStatement(qry.toString());
				for (AgendaMedicaVO vo : lista)
				{
					fones = new StringBuilder();
					stm.setLong(1, vo.getNrusuario());
					stm.setLong(2, vo.getCdpessoa());
					stm.setString(3, DateUtils.formatDateDDMMYYYY(vo.getDtconsulta()));
					rs = stm.executeQuery();
					while ((rs != null) && rs.next())
					{
						fones.append(rs.getString("FONE")).append(";  ");
					}
					if (fones.toString().trim().endsWith(";"))
					{
						vo.setDstelefone(fones.toString().trim().substring(0,
						    fones.toString().trim().length() - 1));
					}
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
			qry = fones = null;
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
	}

}
