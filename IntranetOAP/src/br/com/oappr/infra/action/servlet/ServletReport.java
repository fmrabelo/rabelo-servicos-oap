/**
 * 
 */
package br.com.oappr.infra.action.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.util.GenericUtils;
import br.com.oappr.intranet.vo.LaudoVO;

public final class ServletReport
    extends HttpServlet
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13206501245454L;

	@Override
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException,
	    ServletException
	{
		final String str = req.getParameter("Text1");
		final String nrseqresultado = req.getParameter("nrseqresultado");
		final String nroCadastroPaciente = req.getParameter("nroCadastroPaciente");
		// final Document document = new Document();
		try
		{
			System.out.println("nrseqresultado: " + nrseqresultado);
			System.out.println("nroCadastroPaciente: " + nroCadastroPaciente);
			// if ("pdf".equals(str))
			// {
			// res.setContentType("application/pdf");
			// PdfWriter.getInstance(document, res.getOutputStream());
			// }
			if ("rtf".equals(str))
			{
				// final String fileIn = "C:\\downloads\\text_349747.rtf";
				// final File f = new File(fileIn);
				// final FileInputStream fi = new FileInputStream(f);
				// byte[] blobBuffer = new byte[(int)f.length()];
				// while ((fi.read(blobBuffer)) != -1)
				// {
				// continue;
				// }
				// fi.close();

				final List<LaudoVO> listaLaudos = DaoFactory.getInstance().getLaudos(
				    GenericUtils.toLong(nroCadastroPaciente), GenericUtils.toLong(nrseqresultado));

				final byte[] blobBuffer = listaLaudos.get(0).getDsrtf().getBytes(1,
				    (int)listaLaudos.get(0).getDsrtf().length());

				final String fileName = "relatorio.rtf";
				res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
				res.setContentLength(blobBuffer.length);
				res.setContentType("text/rtf");
				res.getOutputStream().write(blobBuffer, 0, blobBuffer.length);
				res.getOutputStream().flush();
				res.getOutputStream().close();

				// res.setContentType("text/rtf");
				// RtfWriter2.getInstance(document, res.getOutputStream());
			}
			// if ("html".equals(str))
			// {
			// res.setContentType("text/html");
			// HtmlWriter.getInstance(document, res.getOutputStream());
			// }
			// document.open();
			// document.add(new Paragraph("Hello World"));
		}
		// catch (DocumentException de)
		// {
		// de.printStackTrace();
		// System.err.println("document: " + de.getMessage());
		// }
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		// document.close();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}
