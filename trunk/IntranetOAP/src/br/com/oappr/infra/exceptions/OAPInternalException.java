/**
 * 
 */
package br.com.oappr.infra.exceptions;

/**
 * @author rabelo
 */
public final class OAPInternalException
    extends RuntimeException
    implements OAPException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 976464445649316871L;

	/**
	 * 
	 */
	public OAPInternalException ()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public OAPInternalException (String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public OAPInternalException (String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public OAPInternalException (Throwable arg0)
	{
		super(arg0);
	}

}
