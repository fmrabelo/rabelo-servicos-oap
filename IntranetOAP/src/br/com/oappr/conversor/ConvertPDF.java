/**
 * 
 */
package br.com.oappr.conversor;

import java.io.Serializable;

import br.com.oappr.conversor.office.ConversorFromTextOffice;
import br.com.oappr.conversor.pdf.ConversorPDF;

/**
 * Classe responsável por converter conteudo RTF em PDF.
 * @author Rabelo Serviços.
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
	public final byte[] convertPDF (final byte[] arquivoRTF) throws Exception
	{
		try
		{
			final ConversorPDF algoritmo = new ConversorFromTextOffice();
			final byte[] pdf = algoritmo.converterDocumento(arquivoRTF);
			return pdf;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}
}
