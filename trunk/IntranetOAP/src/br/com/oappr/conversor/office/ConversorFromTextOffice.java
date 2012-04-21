package br.com.oappr.conversor.office;

import java.io.Serializable;

import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;

/**
 * Classe de configura��o para convers�es de arquivos RTF para PDF.
 * @author Rabelo Servi�os.
 */
public final class ConversorFromTextOffice
    extends ConversorFromOffice
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8754578778775421L;

	@Override
	protected DocumentFormat getTipoDeDocumentoParaConverter ()
	{
		DocumentFormat doc = new DocumentFormat("Microsoft Word", DocumentFamily.TEXT,
		    "application/msword", "doc");
		doc.setExportFilter(DocumentFamily.TEXT, "MS Word 97");
		return doc;
	}
}
