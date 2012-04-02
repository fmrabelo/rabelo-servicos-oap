/**
 * 
 */
package br.com.oappr.intranet.vo;

/**
 * @author desenvolvimento
 */
public final class FoneVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10210032875432821L;

	private String ddd;
	private String nro;

	/**
	 * 
	 */
	public FoneVO ()
	{
		super();
	}

	/**
	 * @return the ddd
	 */
	public String getDdd ()
	{
		return ddd;
	}

	/**
	 * @param ddd the ddd to set
	 */
	public void setDdd (String ddd)
	{
		this.ddd = ddd;
	}

	/**
	 * @return the nro
	 */
	public String getNro ()
	{
		return nro;
	}

	/**
	 * @param nro the nro to set
	 */
	public void setNro (String nro)
	{
		this.nro = nro;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result + ((nro == null) ? 0 : nro.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final FoneVO other = (FoneVO)obj;
		if (ddd == null)
		{
			if (other.ddd != null)
			{
				return false;
			}
		}
		else if (!ddd.equals(other.ddd))
		{
			return false;
		}
		if (nro == null)
		{
			if (other.nro != null)
			{
				return false;
			}
		}
		else if (!nro.equals(other.nro))
		{
			return false;
		}
		return true;
	}

}
