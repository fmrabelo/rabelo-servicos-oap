package br.com.oappr.infra.report;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe generica para gerar relatórios jasper.
 */
public interface GenericReport<V extends Object>
    extends java.io.Serializable
{
	public static final String PDF_TYPE = "pdf";
	public static final String TXT_TYPE = "txt";
	public static final String HTML_TYPE = "html";
	public static final String XML_TYPE = "xml";
	/** Referência para a extensão XLS - Microsoft Excel. */
	public static final String XLS_TYPE = "xls";

	public void report (HttpServletResponse response, String fileName, String reportType,
	    byte[] relatorio) throws Exception;

	public String jasperFilePath (HttpServletRequest request, String fileName) throws Exception;

	/**
	 * Interface de método que deve gerar a vizualização do relatório
	 * @param request HTTPRequest
	 * @param response HTTPResponse
	 * @param fileName Nome do Arquivo Jasper
	 * @param lista Lista de VOs
	 * @param parameters Parametros do Jasper
	 * @param reportType Formato do Relatorio a ser gerado (ex: pdf ou xls)
	 * @throws Exception
	 */
	public void viewReport (HttpServletRequest request, HttpServletResponse response,
	    String fileName, List<V> lista, HashMap<String, String> parameters, String reportType)
	    throws Exception;
}
