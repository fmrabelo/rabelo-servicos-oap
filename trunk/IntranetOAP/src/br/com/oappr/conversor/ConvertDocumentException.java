package br.com.oappr.conversor;

import java.io.Serializable;

/**
 * Classe de controle de exceções na conversão rtf para pdf.
 * @author rabelo
 */
public class ConvertDocumentException
    extends RuntimeException
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1323587010021L;

	public ConvertDocumentException (String msg)
	{
		super(msg);
	}

	public ConvertDocumentException (Throwable e)
	{
		super(null, e);
	}

}
