package br.com.laserviewpr.infra.action.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet responsável por carregar um arquivo local e apresentar na web.
 * Servlet implementation class for Servlet: CarregarArquivoLocal
 */
public class CarregarArquivoLocal
    extends javax.servlet.http.HttpServlet
    implements javax.servlet.Servlet, Serializable
{
	static final long serialVersionUID = 1974646587465464L;
	public static final String DIRETORIO_IMAGENS = "C:\\Documentos\\Sistemas\\Risc\\Images\\";
	byte[] arquivo = null;

	/*
	 * (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public CarregarArquivoLocal ()
	{
		super();
	}

	/*
	 * (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
		final String nomearquivo = request.getParameter("nomearquivo");
		final String codconvenio = request.getParameter("codconvenio");

		File file = new File(DIRETORIO_IMAGENS + nomearquivo);
		try
		{
			arquivo = fileToByte(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// String extensao = nomearquivo.substring(nomearquivo.lastIndexOf("."),
		// nomearquivo.length());

		response.setContentType("application/octet-stream");
		response.setHeader("Pragma", "");
		response.setHeader("Cache-Control", "");
		response.setHeader("Expires", "");
		response.setHeader("Content-Disposition", "attachment; filename=" + nomearquivo); // Content-Disposition",
		// "inline;
		// filename=
		// response.setHeader("Accept-Charset","ISO-8859-5");
		response.setContentLength(arquivo.length);

		// Grava o array de bytes do documento no response
		response.getOutputStream().write(arquivo, 0, arquivo.length);
		response.getOutputStream().flush();
		response.getOutputStream().close();
		response.flushBuffer();
	}

	public static byte[] fileToByte (File imagem) throws Exception
	{
		FileInputStream fis = new FileInputStream(imagem);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1)
		{
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}

	/*
	 * (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}
}