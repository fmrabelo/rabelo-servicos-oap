/**
 * 
 */
package br.com.oappr.infra.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
				str.append(" 	T2.NRUSUARIOALT, T2.DHALTERACAO, T1.CDCONVENIO,T2.NRUSUSOLIC, T2.DSRTF, T3.IMAGEM ");
				str.append(" FROM SYSADM.AARESULTADO T1, SYSADM.ACREQUISICAO T2, SYSADM.ACREQUISIMAGEM T3 ");
				str.append("  WHERE T1.NRSEQRESULTADO = T2.NRSEQRESULTADO ");
				str.append(" 	 AND T1.TPPRESENCA = 5 ");
				str.append(" 	 AND T1.CDPESSOA = ").append(nroCadastroPaciente);// paciente
				str.append(" 	 AND T2.TPLAUDO = 2 ");// 2=Laudo tipo RTF.
				str.append(" 	 AND T2.FLLIBERADO = 1 ");// laudo liberado
				str.append(" 	 AND T2.NRSEQRESULTADO IS NOT NULL ");
				str.append(" 	 AND T2.NRREQUISICAO = T3.NRREQUISICAO ");
				str.append(" 	 AND T2.CDPROCED = T3.CDPROCED ");
				if ((codigoLaudo != null) && (codigoLaudo.longValue() > 0))
				{
					// --pesquisar laudo espec�fico
					str.append(" AND T2.NRSEQRESULTADO = ").append(codigoLaudo);
					str.append(" AND T2.NRREQUISICAO = ").append(nroRequisicao);
				}
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
					// blob para o laudo especifico e imagens.
					if ((codigoLaudo != null) && (codigoLaudo.longValue() > 0))
					{
						p.setDsrtf(rs.getBlob("DSRTF"));
						p.setImages(rs.getBlob("IMAGEM"));
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

	/**
	 * Teste lixo IMAGE
	 */
	final List<LaudoVO> getLaudosLixo () throws Exception
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
				str.append(" SELECT T1.ID, T1.IMAGEM ");
				str.append(" FROM SYSADM.LIXOIMAGEM T1 ");
				rs = stm.executeQuery(str.toString());
				lista = new ArrayList<LaudoVO>();
				while ((rs != null) && rs.next())
				{
					p = new LaudoVO();
					p.setCdconvenio(rs.getLong("ID"));
					p.setImages(rs.getBlob("IMAGEM"));
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

	public final void gravaImagem (final File file)
	{
		Connection conn = null;
		try
		{
			conn = DaoFactory.getInstance().getConection();
			// insert
			PreparedStatement ps = conn.prepareStatement(" INSERT INTO SYSADM.LIXOIMAGEM(ID, IMAGEM) VALUES(?, ?) ");
			ps.setInt(1, 1);
			FileInputStream is = new FileInputStream(file);
			ps.setBinaryStream(2, is, (int)file.length());
			ps.execute();
			// ps.executeUpdate();
			ps.close();
			is.close();

			// update
			// ps = conn.prepareStatement(" SELECT IMAGEM FROM SYSADM.LIXOIMAGEM
			// WHERE ID = ? FOR UPDATE ");
			// ps.setInt(1, 1);

			// ResultSet rs = ps.executeQuery();
			// if (rs.next())
			// {
			// Blob blob = rs.getBlob("IMAGEM");
			// OutputStream outputStream = blob.setBinaryStream(0L);
			// InputStream inputStream = new ByteArrayInputStream(file);
			// byte[] buffer = blob.getBytes(1, file.length);
			// int byteread = 0;
			// while ((byteread = inputStream.read(buffer)) != -1)
			// {
			// outputStream.write(buffer, 0, byteread);
			// }
			// outputStream.close();
			// inputStream.close();
			// }
			// rs.close();
			// ps.close();
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
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			conn = null;
		}
	}
}