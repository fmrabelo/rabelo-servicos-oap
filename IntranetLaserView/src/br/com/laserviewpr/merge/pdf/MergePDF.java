/**
 * 
 */
package br.com.laserviewpr.merge.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.laserviewpr.infra.util.GenericUtils;
import br.com.laserviewpr.infra.util.Validator;
import br.com.laserviewpr.intranet.vo.FoneVO;
import br.com.laserviewpr.intranet.vo.PessoaVO;

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
 * Classe respons�vel por fazer merge entre arquivos PDF.
 * @author Rabelo Servi�os.
 */
public final class MergePDF
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1986565212142125521L;

	/**
	 * M�todo responsavel por concatenar os InputStream pdf, gerando apenas 1
	 * �nico array de bytes contendo todo conte�do dos pdf�s iniciais.
	 * @param streamOfPDFFiles
	 * @param empresa
	 * @return byte[]
	 * @throws Exception
	 */
	public final byte[] concatPDFs (final List<InputStream> streamOfPDFFiles, final PessoaVO empresa)
	    throws Exception
	{
		Document document = null;
		PdfImportedPage page = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		boolean paginate = true;
		byte[] mergedByte = null;
		try
		{
			final BaseFont bf_courier = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
			final List<InputStream> pdfs = streamOfPDFFiles;
			final List<PdfReader> readers = new ArrayList<PdfReader>();
			// descontar a primeira p�gina que cont�m o cabe�alho.
			int totalPages = -1;
			final Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for pdfs.
			while (iteratorPDFs.hasNext())
			{
				final InputStream pdf = iteratorPDFs.next();
				final PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create document itext.
			document = new Document();
			// Create a writer for the outputstream
			final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			// montar dados do endere�o da empresa.
			final String TR = " - ";
			final String SPC = "          ";
			final StringBuilder endereco = new StringBuilder(
			    GenericUtils.nullToBlank(empresa.getEndereco()));
			endereco.append(TR).append(GenericUtils.nullToBlank(empresa.getComplEndereco()));
			endereco.append(TR).append(GenericUtils.nullToBlank(empresa.getBairro()));
			endereco.append(TR).append(GenericUtils.nullToBlank(empresa.getCidade()));
			endereco.append(TR).append(GenericUtils.nullToBlank(empresa.getUf()));
			endereco.append(TR).append(" CEP: ").append(GenericUtils.nullToBlank(empresa.getCep()));
			endereco.append("\n");
			endereco.append(this.formatFone(empresa.getListaFone()));
			endereco.append(SPC).append(GenericUtils.nullToBlank(empresa.getEmail()));
			endereco.append(SPC).append(GenericUtils.nullToBlank(empresa.getUrlSite()));
			// headers and footers must be added before the document is opened
			final HeaderFooter footer = new HeaderFooter(new Phrase(endereco.toString(), new Font(
			    bf_courier, 7, 0, new Color(204, 204, 204))), false);

			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);

			// open document.
			document.open();
			// fonte
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
					// new page for content
					if (currentPageNumber > 1)
					{
						document.newPage();
						// document.setMargins(380, 108, 172, 36);
					}
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					// diminuir a largura esquerda e aumentar a largura superior
					// para segunda p�gina em diante.
					if (currentPageNumber >= 2)
					{
						// teste para 2 p�ginas por folha.
						// cb.addTemplate(page, 1, 0, 0, .5f, 0, 0);

						// controle de espa�amento superior e esquerdo.
						/**
						 * parametros: 1:pagina,<br>
						 * 2:Texto nao aparece se usar zero 0.<br>
						 * 3:escreve na diagonal usando 1.<br>
						 * 4:diagonal meio da pagina<br>
						 * 5:Texto nao aparece se usar zero 0.<br>
						 * 6: Margem Esquerda<br>
						 * 7: Margem Direita<br>
						 */
						// usar esta configura��o para imagens via Export
						cb.addTemplate(page, 1, 0, 0, 1, -30, -50);
						// usar esta configura��o para imagens via Driver
						// cb.addTemplate(page, 1, 0, 0, 1, -50, -50);

						// Inserve conteudo de ponta cabe�a.
						// cb.addTemplate(page, -0.5f, 0f, 0f, -0.5f,
						// PageSize.A4.width() / 2,
						// PageSize.A4.height());
					}
					else
					{
						cb.addTemplate(page, 0, 0);
					}
					// Code for pagination.
					if (paginate && (currentPageNumber > 1))
					{
						cb.beginText();
						cb.setFontAndSize(bf, 7);
						cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "P�g. "
						    + (currentPageNumber - 1) + " de " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			document.close();
			outputStream.flush();
			mergedByte = outputStream.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
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
			outputStream = null;
		}
		return mergedByte;
	}

	/**
	 * Formata lista de telefone/fax
	 * @param listaFone
	 * @return String
	 */
	private final String formatFone (final List<FoneVO> listaFone) throws Exception
	{
		if (Validator.notEmptyCollection(listaFone))
		{
			final StringBuilder b = new StringBuilder("Tel.: ");
			for (final FoneVO f : listaFone)
			{
				if (!Validator.isBlankOrNull(f.getDdd()))
				{
					b.append(GenericUtils.nullToBlank(f.getDdd())).append("  ");
				}
				if (!Validator.isBlankOrNull(f.getNro()))
				{
					b.append(GenericUtils.nullToBlank(f.getNro()));
				}
			}
			return b.toString();
		}
		return null;
	}
}
