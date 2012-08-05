/**
 * 
 */
package br.com.oappr.conversor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import br.com.oappr.conversor.office.ConversorFromTextOffice;
import br.com.oappr.conversor.pdf.ConversorPDF;

import com.lowagie.text.Document;

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
	public final byte[] convertRTF_To_PDF (final byte[] arquivoRTF) throws Exception
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

	/**
	 * @param jpg
	 * @return byte[]
	 */
	public final byte[] convertJPG_To_PDF (final byte[] jpg) throws Exception
	{
		final Document document = new Document();
		// String input = "c://temp//MOB.jpg"; // .gif and .jpg are ok too!
		final String output = "c://temp//vicenteDestro.jpg";
		try
		{
			final FileOutputStream fos = new FileOutputStream(new File(output));
			fos.write(jpg);
			fos.flush();
			fos.close();

			// final PdfWriter writer = PdfWriter.getInstance(document, fos);
			// writer.open();
			// document.open();
			// // document.add(Image.getInstance(jpg));
			// final Image instance = Image.getInstance(input);
			// instance.scalePercent(30);
			// document.add(instance);
			// document.close();
			// writer.close();

			// ------------------------------------------
			/** se precisar tratar tamanho da imagem */
			// img.scalePercent(50);
			// document.add(img);
			// ------------------------------------------
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return jpg;
	}
}
