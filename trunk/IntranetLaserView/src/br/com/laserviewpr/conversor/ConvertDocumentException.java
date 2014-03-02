package br.com.laserviewpr.conversor;

import java.io.Serializable;

/**
 * Classe de controle de exceções na conversão rtf para pdf.
 * @author Rabelo Serviços.
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
