/**
 * 
 */
package br.com.laserviewpr.intranet.vo;

import java.io.Serializable;

/**
 * Classe Pojo para dados do Medico.
 * @author Rabelo Serviços.
 */
public class MedicoVO
    extends UsuarioVO
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -986232478754011L;

	private String descrCRM;
	private String siglaTratamento;
	private java.sql.Blob AssinaturaDigital;

	/**
	 * 
	 */
	public MedicoVO ()
	{
		super();
	}

	/**
	 * @return the descrCRM
	 */
	public String getDescrCRM ()
	{
		return descrCRM;
	}

	/**
	 * @param descrCRM the descrCRM to set
	 */
	public void setDescrCRM (String descrCRM)
	{
		this.descrCRM = descrCRM;
	}

	/**
	 * @return the siglaTratamento
	 */
	public String getSiglaTratamento ()
	{
		return siglaTratamento;
	}

	/**
	 * @param siglaTratamento the siglaTratamento to set
	 */
	public void setSiglaTratamento (String siglaTratamento)
	{
		this.siglaTratamento = siglaTratamento;
	}

	/**
	 * @return the assinaturaDigital
	 */
	public java.sql.Blob getAssinaturaDigital ()
	{
		return AssinaturaDigital;
	}

	/**
	 * @param assinaturaDigital the assinaturaDigital to set
	 */
	public void setAssinaturaDigital (java.sql.Blob assinaturaDigital)
	{
		AssinaturaDigital = assinaturaDigital;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descrCRM == null) ? 0 : descrCRM.hashCode());
		result = prime * result + ((siglaTratamento == null) ? 0 : siglaTratamento.hashCode());
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
		if (!super.equals(obj))
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final MedicoVO other = (MedicoVO)obj;
		if (descrCRM == null)
		{
			if (other.descrCRM != null)
			{
				return false;
			}
		}
		else if (!descrCRM.equals(other.descrCRM))
		{
			return false;
		}
		if (siglaTratamento == null)
		{
			if (other.siglaTratamento != null)
			{
				return false;
			}
		}
		else if (!siglaTratamento.equals(other.siglaTratamento))
		{
			return false;
		}
		return true;
	}

}
