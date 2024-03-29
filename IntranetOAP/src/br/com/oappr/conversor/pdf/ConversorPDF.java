package br.com.oappr.conversor.pdf;

import java.io.Serializable;

import br.com.oappr.conversor.ConvertDocumentException;

/**
 * Interface para convers�o rtf to pdf.
 * @author Rabelo Servi�os.
 */
public interface ConversorPDF
    extends Serializable
{
	public byte[] converterDocumento (byte[] document) throws ConvertDocumentException;
}