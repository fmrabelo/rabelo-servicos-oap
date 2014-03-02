/**
 * 
 */
package br.com.oappr.intranet.vo;

import java.util.Date;

/**
 * Classe Pojo contendo atributos para os dados da Agenda Médica.
 * @author Rabelo Serviços Ltda.
 */
public final class AgendaMedicaVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10999598475452487L;

	private Date dtconsulta;
	private String hragenda;
	private Long nrusuario;
	private Long cdpessoa;
	private String dsosbagenda;
	private String tpvaga;
	private Integer flconfirmado;
	private String nmusuario;
	private String nmpessoa;
	private String nrddd;
	private String dstelefone;
	private String dsconvenio;

	/**
	 * 
	 */
	public AgendaMedicaVO ()
	{
		super();
	}

	public Date getDtconsulta ()
	{
		return dtconsulta;
	}

	public void setDtconsulta (Date dtconsulta)
	{
		this.dtconsulta = dtconsulta;
	}

	public String getHragenda ()
	{
		return hragenda;
	}

	public void setHragenda (String hragenda)
	{
		this.hragenda = hragenda;
	}

	public Long getNrusuario ()
	{
		return nrusuario;
	}

	public void setNrusuario (Long nrusuario)
	{
		this.nrusuario = nrusuario;
	}

	public Long getCdpessoa ()
	{
		return cdpessoa;
	}

	public void setCdpessoa (Long cdpessoa)
	{
		this.cdpessoa = cdpessoa;
	}

	public String getDsosbagenda ()
	{
		return dsosbagenda;
	}

	public void setDsosbagenda (String dsosbagenda)
	{
		this.dsosbagenda = dsosbagenda;
	}

	public String getTpvaga ()
	{
		return tpvaga;
	}

	public void setTpvaga (String tpvaga)
	{
		this.tpvaga = tpvaga;
	}

	public Integer getFlconfirmado ()
	{
		return flconfirmado;
	}

	public void setFlconfirmado (Integer flconfirmado)
	{
		this.flconfirmado = flconfirmado;
	}

	public String getNmusuario ()
	{
		return nmusuario;
	}

	public void setNmusuario (String nmusuario)
	{
		this.nmusuario = nmusuario;
	}

	public String getNmpessoa ()
	{
		return nmpessoa;
	}

	public void setNmpessoa (String nmpessoa)
	{
		this.nmpessoa = nmpessoa;
	}

	public String getNrddd ()
	{
		return nrddd;
	}

	public void setNrddd (String nrddd)
	{
		this.nrddd = nrddd;
	}

	public String getDstelefone ()
	{
		return dstelefone;
	}

	public void setDstelefone (String dstelefone)
	{
		this.dstelefone = dstelefone;
	}

	public String getDsconvenio ()
	{
		return dsconvenio;
	}

	public void setDsconvenio (String dsconvenio)
	{
		this.dsconvenio = dsconvenio;
	}

	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdpessoa == null) ? 0 : cdpessoa.hashCode());
		result = prime * result + ((dtconsulta == null) ? 0 : dtconsulta.hashCode());
		result = prime * result + ((hragenda == null) ? 0 : hragenda.hashCode());
		result = prime * result + ((nmpessoa == null) ? 0 : nmpessoa.hashCode());
		result = prime * result + ((nmusuario == null) ? 0 : nmusuario.hashCode());
		result = prime * result + ((nrusuario == null) ? 0 : nrusuario.hashCode());
		return result;
	}

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
		final AgendaMedicaVO other = (AgendaMedicaVO)obj;
		if (cdpessoa == null)
		{
			if (other.cdpessoa != null)
			{
				return false;
			}
		}
		else if (!cdpessoa.equals(other.cdpessoa))
		{
			return false;
		}
		if (dtconsulta == null)
		{
			if (other.dtconsulta != null)
			{
				return false;
			}
		}
		else if (!dtconsulta.equals(other.dtconsulta))
		{
			return false;
		}
		if (hragenda == null)
		{
			if (other.hragenda != null)
			{
				return false;
			}
		}
		else if (!hragenda.equals(other.hragenda))
		{
			return false;
		}
		if (nmpessoa == null)
		{
			if (other.nmpessoa != null)
			{
				return false;
			}
		}
		else if (!nmpessoa.equals(other.nmpessoa))
		{
			return false;
		}
		if (nmusuario == null)
		{
			if (other.nmusuario != null)
			{
				return false;
			}
		}
		else if (!nmusuario.equals(other.nmusuario))
		{
			return false;
		}
		if (nrusuario == null)
		{
			if (other.nrusuario != null)
			{
				return false;
			}
		}
		else if (!nrusuario.equals(other.nrusuario))
		{
			return false;
		}
		return true;
	}

}
