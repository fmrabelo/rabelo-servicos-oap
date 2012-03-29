/**
 * 
 */
package br.com.oappr.conversor;

import java.io.Serializable;

import br.com.oappr.conversor.office.ConversorFromTextOffice;
import br.com.oappr.conversor.pdf.ConversorPDF;

/**
 * Classe responsável por converter conteudo RTF em PDF.
 * @author rabelo
 */
public final class ConvertPDF
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -98110012358647101L;

	/**
	 * @param arquivoRTF
	 * @return byte[]
	 */
	public final byte[] convertPDF (final byte[] arquivoRTF)
	{
		try
		{
			final ConversorPDF algoritmo = new ConversorFromTextOffice();
			// byte[] arquivo = null;//
			// obterArquivosEmBytes("/br/com/jm/conversor/pdf/laudo_375688.rtf");
			byte[] pdf = algoritmo.converterDocumento(arquivoRTF);
			// escreverArquivosEmBytes(pdf, "laudo_pdf_office.pdf");
			return pdf;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
}
