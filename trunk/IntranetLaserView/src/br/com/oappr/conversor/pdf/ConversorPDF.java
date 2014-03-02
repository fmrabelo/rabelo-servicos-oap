package br.com.oappr.conversor.pdf;

import java.io.Serializable;

import br.com.oappr.conversor.ConvertDocumentException;

/**
 * Interface para conversão rtf to pdf.
 * @author Rabelo Serviços.
 */
public interface ConversorPDF
    extends Serializable
{
	public byte[] converterDocumento (byte[] document) throws ConvertDocumentException;
}