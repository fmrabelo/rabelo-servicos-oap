/**
 * 
 */
package br.com.oappr.merge.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Classe responsável por fazer merge entre arquivos PDF.
 * @author rabelo
 */
public final class MergePDF
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1986565212142125521L;

	/**
	 * @param streamOfPDFFiles
	 * @param outputStream
	 * @param paginate
	 */
	public final void concatPDFs (final List<InputStream> streamOfPDFFiles, byte[] byteArrayMerge)
	{
		Document document = null;
		PdfImportedPage page = null;
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteArrayMerge.length);
		boolean paginate = true;
		try
		{
			final BaseFont bf_courier = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
			final List<InputStream> pdfs = streamOfPDFFiles;
			final List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = -1; // descontar o cabeçalho.
			final Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext())
			{
				final InputStream pdf = iteratorPDFs.next();
				final PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}

			// calculo para altura.
			// Rectangle psize = readers.get(1).getPageSize(1);
			// float width = psize.getHeight();
			// float height = psize.getWidth();
			// document = new Document(new Rectangle(width, height));
			document = new Document();
			// Create a writer for the outputstream
			final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			// headers and footers must be added before the document is opened
			final HeaderFooter footer = new HeaderFooter(
			    new Phrase(
			        "Rua Emiliano Perneta, 297  2º andar  cj 22 / 24 - Centro - Curitiba - PR - CEP: 80010-050 \n Fone: (41) 3225-7303          oap@oappr.com.br           www.oappr.com.br",
			        new Font(bf_courier, 10)), false);

			// Rua Emiliano Perneta, 297 2º andar cj 22 / 24 - Centro - Curitiba
			// - PR - CEP:
			// 80010-050
			// Fone: (41) 3225-7303 oap@oappr.com.br www.oappr.com.br

			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);
			// open document.
			document.open();
			final BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
			    BaseFont.NOT_EMBEDDED);
			// Holds the PDF data
			final PdfContentByte cb = writer.getDirectContent();

			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			final Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext())
			{
				final PdfReader pdfReader = iteratorPDFReader.next();
				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages())
				{
					if (currentPageNumber > 1)
					{
						document.newPage();
					}
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					if (currentPageNumber >= 2)
					{
						// cb.addTemplate(page, 1, 0, 0, .5f, 0, 0);
						cb.addTemplate(page, 1, 0, 0, 1, -40, -50);
					}
					else
					{
						cb.addTemplate(page, 0, 0);
					}
					// Code for pagination.
					if (paginate && (currentPageNumber > 1))
					{
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Pág. "
						    + (currentPageNumber - 1) + " de " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			outputStream.write(byteArrayMerge, 0, outputStream.size());
			document.close();
			outputStream.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if ((document != null) && document.isOpen())
			{
				document.close();
			}
			try
			{
				if (outputStream != null)
				{
					outputStream.close();
				}
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
			document = null;
			page = null;
		}
	}
}
