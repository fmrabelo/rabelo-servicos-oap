/**
 * 
 */
package br.com.laserviewpr.intranet.vo;

/**
 * Classe Pojo para os atributos dos usuários da Empresa cadastrados para acesso
 * interno via site web.
 * @author Rabelo Serviços.
 */
public class UsuarioInternoVO
    extends MedicoVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10216598811332357L;
	private Long idweb;
	private String senhaweb;
	private String emailweb;

	/**
	 * 
	 */
	public UsuarioInternoVO ()
	{
		super();
	}

	/**
	 * @param idweb
	 * @param nrusuario
	 * @param senhaweb
	 * @param emailweb
	 */
	public UsuarioInternoVO (Long idweb, Long nrusuario, String senhaweb, String emailweb)
	{
		super();
		this.idweb = idweb;
		this.senhaweb = senhaweb;
		this.emailweb = emailweb;
	}

	/**
	 * @return the idweb
	 */
	public Long getIdweb ()
	{
		return idweb;
	}

	/**
	 * @param idweb the idweb to set
	 */
	public void setIdweb (Long idweb)
	{
		this.idweb = idweb;
	}

	/**
	 * @return the senhaweb
	 */
	public String getSenhaweb ()
	{
		return senhaweb;
	}

	/**
	 * @param senhaweb the senhaweb to set
	 */
	public void setSenhaweb (String senhaweb)
	{
		this.senhaweb = senhaweb;
	}

	/**
	 * @return the emailweb
	 */
	public String getEmailweb ()
	{
		return emailweb;
	}

	/**
	 * @param emailweb the emailweb to set
	 */
	public void setEmailweb (String emailweb)
	{
		this.emailweb = emailweb;
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
		result = prime * result + ((idweb == null) ? 0 : idweb.hashCode());
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
		final UsuarioInternoVO other = (UsuarioInternoVO)obj;
		if (idweb == null)
		{
			if (other.idweb != null)
			{
				return false;
			}
		}
		else if (!idweb.equals(other.idweb))
		{
			return false;
		}
		return true;
	}
}
