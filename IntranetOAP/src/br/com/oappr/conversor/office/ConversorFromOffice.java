package br.com.oappr.conversor.office;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.ConnectException;

import br.com.oappr.conversor.ConvertDocumentException;
import br.com.oappr.conversor.pdf.ConversorPDF;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * Classe que cont�m m�todos com as regras de acesso aos servi�os openOffice e
 * para convers�o de RTF para PDF.
 * @author Rabelo Servi�os.
 */
/*
 * Para deixar o openoffice aceitando tcp/ip: soffice -headless
 * -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
 */
public abstract class ConversorFromOffice
    implements ConversorPDF, Serializable
{

	/*
	 * O host e a porta do open office est�o fixos no c�digo por simplicidade,
	 * mas essa configura��o pode ser externalizada
	 */
	private final String host = "localhost";
	private final int porta = 8100;
	private static final String MSG = "Servi�o do open office fora do ar! Convers�es para PDF a partir de documentos n�o ir�o funcionar!";

	/**
	 * 
	 */
	public byte[] converterDocumento (byte[] documento)
	{
		try
		{
			return this.converterInternamente(documento);
		}
		catch (ConnectException e)
		{
			throw new ConvertDocumentException(MSG);
		}
	}

	/**
	 * @param documento
	 * @return
	 * @throws ConnectException
	 */
	public byte[] converterInternamente (byte[] documento) throws ConnectException
	{
		final OpenOfficeConnection connection = new SocketOpenOfficeConnection(host, porta);
		connection.connect();

		try
		{
			DocumentFormat doc = this.getTipoDeDocumentoParaConverter();
			DocumentFormat pdf = new DocumentFormat("Portable Document Format", "application/pdf",
			    "pdf");
			pdf.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");
			pdf.setExportFilter(DocumentFamily.SPREADSHEET, "calc_pdf_Export");

			ByteArrayInputStream input = new ByteArrayInputStream(documento);
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			DocumentConverter documentConverter = new OpenOfficeDocumentConverter(connection);
			documentConverter.convert(input, doc, output, pdf);

			return output.toByteArray();
		}
		finally
		{
			connection.disconnect();
		}
	}

	// para estender nas subclasses - define qual tipo de documento do office
	// ir� converter
	protected abstract DocumentFormat getTipoDeDocumentoParaConverter ();

}
