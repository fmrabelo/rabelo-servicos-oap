package br.com.laserviewpr.conversor;

import java.io.Serializable;

/**
 * Classe de controle de exce��es na convers�o rtf para pdf.
 * @author Rabelo Servi�os.
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
