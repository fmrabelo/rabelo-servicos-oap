/**
 * 
 */
package br.com.laserviewpr.infra.exceptions;

/**
 * @author rabelo
 */
public final class InternalException
    extends RuntimeException
    implements IntranetException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 976464445649316871L;

	/**
	 * 
	 */
	public InternalException ()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InternalException (String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public InternalException (String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public InternalException (Throwable arg0)
	{
		super(arg0);
	}

}
