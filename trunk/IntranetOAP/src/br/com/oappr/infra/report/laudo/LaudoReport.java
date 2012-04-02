package br.com.oappr.infra.report.laudo;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import br.com.oappr.infra.report.PrincipalReport;

/**
 * Relat�rio de Laudos.
 * @author Fertipar
 */
public final class LaudoReport
    extends PrincipalReport<Object>
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6699965917166824783L;

	public LaudoReport ()
	{
		super();
	}

	/**
	 * Monta e gera o relat�rio e todo conte�do � passado via parametros.
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public void viewReport (HttpServletRequest request, HttpServletResponse response,
	    String fileName, List<Object> lista, HashMap<String, String> parameters, String reportType)
	    throws Exception
	{
		File reportFile = null;
		try
		{
			this.setDefaultParams(request, parameters);
			reportFile = new File(this.jasperFilePath(request, fileName));
			final JREmptyDataSource dataSource = new JREmptyDataSource();
			this.showReport(response, fileName, parameters, reportType, reportFile, dataSource);
		}
		catch (Exception je)
		{
			je.printStackTrace();
			throw new Exception(je);
		}
		catch (Throwable e)
		{
			throw new Exception(e);
		}
		finally
		{
			reportFile = null;
			lista = null;
			parameters = null;
			fileName = null;
			request = null;
		}
	}

	/**
	 * Cria o cabe�alho do relat�rio PDF e retorna o array de bytes com
	 * conte�do.
	 */
	@SuppressWarnings("unused")
	public final byte[] getByteArrayCabecalhoPDF (HttpServletRequest request,
	    HttpServletResponse response, String fileName, List<Object> lista,
	    HashMap<String, String> parameters, String reportType) throws Exception
	{
		File reportFile = null;
		try
		{
			this.setDefaultParams(request, parameters);
			reportFile = new File(this.jasperFilePath(request, fileName));
			final JREmptyDataSource dataSource = new JREmptyDataSource();
			return this.getByteArrayReport(response, fileName, parameters, reportType, reportFile,
			    dataSource);
		}
		catch (Exception je)
		{
			je.printStackTrace();
			throw new Exception(je);
		}
		catch (Throwable e)
		{
			throw new Exception(e);
		}
		finally
		{
			reportFile = null;
			lista = null;
			parameters = null;
			fileName = null;
			request = null;
		}
	}
}
