package br.com.laserviewpr.infra.action.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.laserviewpr.infra.report.ReportParameters;

/**
 * Servlet responsável por carregar um arquivo local e apresentá-lo na web.
 * Servlet implementation class for Servlet: CarregarArquivoLocal
 */

public final class CarregarArquivoLocal extends HttpServlet implements
		ReportParameters, Serializable {
	static final long serialVersionUID = 1974646587465464L;
	public static final String DIRETORIO_IMAGENS = "C:\\Documentos\\Sistemas\\Risc\\Imagens\\";
	byte[] arquivo = null;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public CarregarArquivoLocal() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String nomearquivo = request.getParameter("nomearquivo");
		//final String codconvenio = request.getParameter("codconvenio");

		File file = new File(DIRETORIO_IMAGENS + nomearquivo);
		try {
			arquivo = fileToByte(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String extensao =
		// nomearquivo.substring(nomearquivo.lastIndexOf(".")+1,
		// nomearquivo.length());

		response.setContentType(getContentType(nomearquivo));
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Expires", "0");
		response.setHeader("Content-Encoding:", " gzip");
		response.setHeader("Content-Disposition", "inline; filename="
				+ nomearquivo); // Content-Disposition",
		// attachment ou inline;
		// filename=
		// response.setHeader("Accept-Charset","ISO-8859-5");
		response.setContentLength(arquivo.length);

		// Grava o array de bytes do documento no response
		response.getOutputStream().write(arquivo, 0, arquivo.length);
		response.getOutputStream().flush();
		response.getOutputStream().close();
		response.flushBuffer();
	}

	public static byte[] fileToByte(File imagem) throws Exception {
		final FileInputStream fis = new FileInputStream(imagem);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		if (fis != null) {
			fis.close();
		}
		return baos.toByteArray();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	String getContentType(String fileName) {
		String extension[] = { // File Extensions
		"txt", // 0 - plain text
				"htm", // 1 - hypertext
				"jpg", // 2 - JPEG image
				"png", // 2 - JPEG image
				"gif", // 3 - gif image
				"pdf", // 4 - adobe pdf
				"doc", // 5 - Microsoft Word
				"docx", }; // you can add more
		String mimeType[] = { // mime types
		"text/plain", // 0 - plain text
				"text/html", // 1 - hypertext
				"image/jpg", // 2 - image
				"image/jpg", // 2 - image
				"image/gif", // 3 - image
				"application/pdf", // 4 - Adobe pdf
				"application/msword", // 5 - Microsoft Word
				"application/msword", // 5 - Microsoft Word
		}, // you can add more
		contentType = "text/html"; // default type
		// dot + file extension
		int dotPosition = fileName.lastIndexOf('.');
		// get file extension
		String fileExtension = fileName.substring(dotPosition + 1);
		// match mime type to extension
		for (int index = 0; index < mimeType.length; index++) {
			if (fileExtension.equalsIgnoreCase(extension[index])) {
				contentType = mimeType[index];
				break;
			}
		}
		return contentType;
	}
}