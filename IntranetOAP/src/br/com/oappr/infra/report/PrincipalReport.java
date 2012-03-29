package br.com.oappr.infra.report;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import br.com.oappr.infra.util.Validator;

/**
 * Classe abstrata que encapsula detalhes de lógica para geração dos relatórios
 * no jasper report. Esta classe deve ser extendida pelas classes especialistas
 * que terão a lógica dos relatórios.
 * @author Desenvolvimento
 */
public abstract class PrincipalReport<V extends Object>
    extends Object
    implements GenericReport<V>, Serializable
{

	public PrincipalReport ()
	{
	}

	/**
	 * Método principal responsável por gerar o relatório de um tipo
	 * especificado pelo parametro reportType.
	 */
	public void report (HttpServletResponse response, String fileName, String reportType,
	    byte[] relatorio) throws Exception
	{
		if ((relatorio != null) && (relatorio.length > 0))
		{
			response.setContentType(new StringBuilder("application/").append(reportType).toString());
			response.setHeader("Content-disposition",
			    new StringBuilder("inline; filename=").append(fileName).append(".").append(
			        reportType).toString());

			this.setResponseHeader(response);
			response.setContentLength(relatorio.length);
			response.getOutputStream().write(relatorio);
			response.flushBuffer();
		}
		relatorio = null;
	}

	/**
	 * @param response
	 * @param fileName
	 * @param relatorio
	 * @throws Exception
	 */
	public void viewHtlmReport (HttpServletResponse response, String fileName, String relatorio)
	    throws Exception
	{
		if (!Validator.isBlankOrNull(relatorio))
		{
			response.setContentType(new StringBuilder("application/").append(HTML_TYPE).toString());
			response.setHeader("Content-disposition",
			    new StringBuilder("inline; filename=").append(fileName).append(".").append(
			        HTML_TYPE).toString());

			this.setResponseHeader(response);
			response.setContentLength(relatorio.length());
			response.getOutputStream().write(relatorio.getBytes());
			response.flushBuffer();
		}
		relatorio = null;
	}

	/**
	 * Cria relatorio Jaspar em formato do arquivo XLS (Excel) e seta o conteudo
	 * no response
	 * @param response
	 * @param fileName
	 * @param parameters
	 * @throws Exception
	 */
	public void viewExcelReport (HttpServletResponse response, String fileName, File jasperFile,
	    Map<String, String> parameters, JRDataSource dataSource) throws Exception
	{
		response.setContentType(new StringBuilder("application/vnd.ms-excel").toString());
		response.setHeader("Content-disposition",
		    new StringBuilder("attachment; filename=").append(fileName).append(".xls").toString());
		this.setResponseHeader(response);
		try
		{
			response.setContentLength(this.getXls(response, jasperFile, parameters, dataSource));
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * Seta parametros comuns no header do response
	 * @param response
	 */
	private void setResponseHeader (HttpServletResponse response)
	{
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Encoding:", " gzip");
	}

	/**
	 * Cria o buffer de bytes do arquivo pdf gerado pelo JasperReports para o
	 * formato em que o acrobat possa gerar o documento.
	 * @return byte[]
	 * @throws JRException
	 */
	public byte[] getBufferPdf (File jasperFile, Map<String, String> parameters,
	    JRDataSource dataSource) throws JRException
	{
		byte[] buffer = null;
		if ((jasperFile != null) && (parameters != null))
		{
			if (dataSource != null)
			{
				buffer = JasperRunManager.runReportToPdf(jasperFile.getPath(), parameters,
				    dataSource);
			}
			else
			{
				buffer = JasperRunManager.runReportToPdf(jasperFile.getPath(), parameters,
				    new JREmptyDataSource());
			}
		}
		return buffer;
	}

	/**
	 * Cria o arquivo xls gerado pelo JasperReports para o formato em que o
	 * excel possa gerar o documento.
	 * @param response
	 * @return
	 * @throws BusinessException
	 * @throws JRException
	 */
	@SuppressWarnings("deprecation")
	protected int getXls (HttpServletResponse response, File jasperFile,
	    Map<String, String> parameters, JRDataSource dataSource) throws Exception
	{
		try
		{
			if ((jasperFile != null) && (parameters != null) && (dataSource != null))
			{
				JasperReport report = JasperManager.loadReport(jasperFile.getAbsolutePath());

				Field field = JRBaseReport.class.getDeclaredField("leftMargin");
				field.setAccessible(true);
				field.setInt(report, 0);

				field = JRBaseReport.class.getDeclaredField("ignorePagination");
				field.setAccessible(true);
				field.setBoolean(report, true);

				// Imprime o cabeçalho apenas 1 vez
				if (null != report.getGroups())
				{
					for (JRGroup base : report.getGroups())
					{
						base.setReprintHeaderOnEachPage(false);
						base.setStartNewPage(false);
					}
				}

				JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters,
				    dataSource);

				JRXlsExporter xlsExporter = new JRXlsExporter();
				xlsExporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
				    Boolean.FALSE);
				xlsExporter.setParameter(
				    JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				xlsExporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE,
				    Boolean.TRUE);
				xlsExporter.setParameter(JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED,
				    Boolean.TRUE);
				xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				    response.getOutputStream());

				// Em virtude de resticao de tamanho da planilha do excel
				// Sheet name cannot be blank, greater than 31 chars, or contain
				// any of /\*?[]
				String sheetName = jasperFile.getName();
				if (!Validator.isBlankOrNull(sheetName) && (sheetName.length() > 30))
				{
					sheetName = sheetName.substring(0, 30);
				}
				xlsExporter.setParameter(JRXlsAbstractExporterParameter.SHEET_NAMES,
				    new String[]{sheetName});
				xlsExporter.exportReport();
				return response.getOutputStream().toString().length();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new Exception("Não foi possível gerar o XLS.", e);
		}
		return 0;
	}

	/**
	 * retorna path para arquivo jasper. *
	 * @param request
	 * @param fileName
	 * @param modulo
	 * @return
	 * @throws Exception
	 */
	public String jasperFilePath (HttpServletRequest request, String fileName) throws Exception
	{
		return request.getSession().getServletContext().getRealPath(
		    new StringBuilder(File.separator).append("reports").append(File.separator).append(
		        fileName).append(".jasper").toString());
	}

	/**
	 * retorna path para arquivo jasper jrxml.
	 * @param request
	 * @param fileName *
	 * @param modulo
	 * @return
	 * @throws Exception
	 */
	public String jrxmlFilePath (HttpServletRequest request, String fileName) throws Exception
	{
		return request.getSession().getServletContext().getRealPath(
		    new StringBuilder(File.separator).append("reports").append(File.separator).append(
		        fileName).append(".jrxml").toString());
	}

	/**
	 * retorna o path da logomarca de acordo com a filial. Caso não seja
	 * definida uma filial, será usada a filial Default.
	 * @param request
	 * @param codFilial
	 * @return String
	 * @throws Exception
	 */
	public String logoImagePath (HttpServletRequest request, final Long codFilial) throws Exception
	{
		if (request != null)
		{
			// if ((codFilial == null) || (codFilial.longValue() <= 0))
			// {
			// codFilial = null;// getCodFilialDefault(request);
			// }
			return request.getSession().getServletContext().getRealPath(
			    new StringBuilder(File.separator).append("images").append(File.separator).append(
			        "logo").append(File.separator).append("logo2.jpg").toString());
		}
		return null;
	}

	/**
	 * Retorna o path para alguma imagem gif especifica do sistema.
	 * @param request
	 * @param imageName
	 * @return
	 * @throws Exception
	 */
	public String imagePath (HttpServletRequest request, String imageName) throws Exception
	{
		if ((imageName == null) || (imageName.trim().length() == 0))
		{
			imageName = "fundoInicial.gif";
		}
		return request.getSession().getServletContext().getRealPath(
		    new StringBuilder(File.separator).append("images").append(File.separator).append(
		        imageName).toString());
	}

	/**
	 * @param request
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> setDefaultParams (HttpServletRequest request,
	    HashMap<String, String> parameters) throws Exception
	{
		if (parameters == null)
		{
			parameters = new HashMap<String, String>();
		}
		// seta a logo da filial
		parameters.put("LOGO_PATH", this.logoImagePath(request, null));
		// set usuario logado
		if (!parameters.containsKey("USUARIO"))
		{
			parameters.put("USUARIO", null);
		}
		return parameters;
	}

	/**
	 * Método que faz a chamada da versão de impressão do relatório em PDF, HTML
	 * ou EXCEL.
	 * @param response HTTP Response
	 * @param fileName Nome do Arquivo
	 * @param parameters Parametros do Relatorio
	 * @param reportType Tipo do Relatorio
	 * @param jasperFile Arquivo Jasper
	 * @param dataSource Fonte dos dados
	 * @throws Exception
	 */
	public void showReport (HttpServletResponse response, String fileName,
	    HashMap<String, String> parameters, String reportType, File jasperFile,
	    JRDataSource dataSource) throws Exception
	{
		if (PDF_TYPE.equals(reportType))
		{
			byte[] buffer = this.getBufferPdf(jasperFile, parameters, dataSource);
			this.report(response, fileName, PDF_TYPE, buffer);
		}
		else if (XLS_TYPE.equals(reportType))
		{
			this.viewExcelReport(response, fileName, jasperFile, parameters, dataSource);
		}
		else if (HTML_TYPE.equalsIgnoreCase(reportType))
		{
			// TODO: implementar quando necessário. Retorna o caminho onde o
			// arquivo html foi gravado.
			// String pathFileHtml =
			// JasperRunManager.runReportToHtmlFile(jasperFile.getPath(),
			// parameters,
			// dataSource);
		}
	}

	/**
	 * Método que faz a chamada da versão de impressão do relatório em PDF, HTML
	 * ou EXCEL sem uso de fonte de dados DS.
	 * @param response HTTP Response
	 * @param fileName Nome do Arquivo
	 * @param parameters Parametros do Relatorio
	 * @param reportType Tipo do Relatorio
	 * @param jasperFile Arquivo Jasper
	 * @throws Exception
	 */
	public void showReport (HttpServletResponse response, String fileName,
	    HashMap<String, String> parameters, String reportType, File jasperFile) throws Exception
	{
		this.showReport(response, fileName, parameters, reportType, jasperFile, null);
	}
}
