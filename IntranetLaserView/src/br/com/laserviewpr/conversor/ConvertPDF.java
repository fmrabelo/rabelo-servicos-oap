/**
 * 
 */
package br.com.laserviewpr.conversor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.io.IOUtils;

import br.com.laserviewpr.conversor.office.ConversorFromTextOffice;
import br.com.laserviewpr.conversor.pdf.ConversorPDF;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

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
		finally
		{
		}
	}

	/**
	 * @param jpg
	 * @return byte[]
	 */
	public final byte[] convertJPG_To_PDF (final Blob imageBlob, final String nomeExame)
	    throws Exception
	{
		final Document document = new Document();
		// String input = "c://temp//MOB.jpg"; // .gif and .jpg are ok too!
		// final String output = "c://temp//IMG_PDF_" +
		// System.currentTimeMillis() + ".pdf";
		try
		{
			// final File fileOut = new File(output);
			final OutputStream fos = new ByteArrayOutputStream();
			// fos.write(jpg);
			// fos.flush();
			// fos.close();

			final PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);
			pdfWriter.open();
			document.open();
			// document.add(Image.getInstance(jpg));
			final byte[] byteArrayImg = imageBlob.getBytes(1, (int)imageBlob.length());
			final Image instanceImg = Image.getInstance(byteArrayImg);

			// ------------------------------------------

			/** Usar configurações abaixo, se caso precisar tratar zoom da imagem */
			// instanceImg.scalePercent(50);
			/**
			 * Melhoria: 19/09/2013 - Comentar regra de zoom em 30% para alguns
			 * exames e 57% para outros, deixando todos com 30%.
			 */
			instanceImg.scalePercent(30);
			// if (GenericUtils.verificarNomeExames(nomeExame,
			// ServletReport.EXAMES_MENOR_ZOOM))
			// {
			// // usar zoom 30 com imagem exportada com o Driver
			// instanceImg.scalePercent(30);
			// }
			// else
			// {
			// // usar zoom 57 com imagem exportada com clinitolls EXPORT.
			// instanceImg.scalePercent(57);
			// }
			// ------------------------------------------

			document.add(instanceImg);
			// document.setMargins(180, 108, 72, 36);
			document.close();
			pdfWriter.close();
			fos.close();

			final ByteArrayInputStream in = new ByteArrayInputStream(
			    ((ByteArrayOutputStream)fos).toByteArray());
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(in, out);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
			return out.toByteArray();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
		}
	}
}
