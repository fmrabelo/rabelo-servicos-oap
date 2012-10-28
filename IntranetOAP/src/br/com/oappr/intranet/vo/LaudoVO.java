/**
 * 
 */
package br.com.oappr.intranet.vo;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

/**
 * Classe Pojo contendo atributos para os dados do Laudo.
 * @author Rabelo Serviços Ltda.
 */
public final class LaudoVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10216598875464987L;
	private Long nrrequisicao;
	private Long nrseqresultado;
	private Date dtconsulta;
	private Date hragenda;
	private Long nrusuarioamb;
	private Long cdpessoa;
	private Long cdproced;
	private String dsexamecompl;
	private Long nrlaudo;
	private Long nrusuarioinc;
	private Date dhinclusao;
	private Long nrusuarioalt;
	private Date dhalteracao;
	private Long cdconvenio;
	private Long nrususolic;
	private Blob dsrtf;
	private List<Blob> images;

	/**
	 * 
	 */
	public LaudoVO ()
	{
		super();
	}

	/**
	 * @return the nrrequisicao
	 */
	public Long getNrrequisicao ()
	{
		return nrrequisicao;
	}

	/**
	 * @param nrrequisicao the nrrequisicao to set
	 */
	public void setNrrequisicao (Long nrrequisicao)
	{
		this.nrrequisicao = nrrequisicao;
	}

	/**
	 * @return the nrseqresultado
	 */
	public Long getNrseqresultado ()
	{
		return nrseqresultado;
	}

	/**
	 * @param nrseqresultado the nrseqresultado to set
	 */
	public void setNrseqresultado (Long nrseqresultado)
	{
		this.nrseqresultado = nrseqresultado;
	}

	/**
	 * @return the dtconsulta
	 */
	public Date getDtconsulta ()
	{
		return dtconsulta;
	}

	/**
	 * @param dtconsulta the dtconsulta to set
	 */
	public void setDtconsulta (Date dtconsulta)
	{
		this.dtconsulta = dtconsulta;
	}

	/**
	 * @return the hragenda
	 */
	public Date getHragenda ()
	{
		return hragenda;
	}

	/**
	 * @param hragenda the hragenda to set
	 */
	public void setHragenda (Date hragenda)
	{
		this.hragenda = hragenda;
	}

	/**
	 * @return the nrusuarioamb
	 */
	public Long getNrusuarioamb ()
	{
		return nrusuarioamb;
	}

	/**
	 * @param nrusuarioamb the nrusuarioamb to set
	 */
	public void setNrusuarioamb (Long nrusuarioamb)
	{
		this.nrusuarioamb = nrusuarioamb;
	}

	/**
	 * @return the cdpessoa
	 */
	public Long getCdpessoa ()
	{
		return cdpessoa;
	}

	/**
	 * @param cdpessoa the cdpessoa to set
	 */
	public void setCdpessoa (Long cdpessoa)
	{
		this.cdpessoa = cdpessoa;
	}

	/**
	 * @return the cdproced
	 */
	public Long getCdproced ()
	{
		return cdproced;
	}

	/**
	 * @param cdproced the cdproced to set
	 */
	public void setCdproced (Long cdproced)
	{
		this.cdproced = cdproced;
	}

	/**
	 * @return the dsexamecompl
	 */
	public String getDsexamecompl ()
	{
		return dsexamecompl;
	}

	/**
	 * @param dsexamecompl the dsexamecompl to set
	 */
	public void setDsexamecompl (String dsexamecompl)
	{
		this.dsexamecompl = dsexamecompl;
	}

	/**
	 * @return the nrlaudo
	 */
	public Long getNrlaudo ()
	{
		return nrlaudo;
	}

	/**
	 * @param nrlaudo the nrlaudo to set
	 */
	public void setNrlaudo (Long nrlaudo)
	{
		this.nrlaudo = nrlaudo;
	}

	/**
	 * @return the nrusuarioinc
	 */
	public Long getNrusuarioinc ()
	{
		return nrusuarioinc;
	}

	/**
	 * @param nrusuarioinc the nrusuarioinc to set
	 */
	public void setNrusuarioinc (Long nrusuarioinc)
	{
		this.nrusuarioinc = nrusuarioinc;
	}

	/**
	 * @return the dhinclusao
	 */
	public Date getDhinclusao ()
	{
		return dhinclusao;
	}

	/**
	 * @param dhinclusao the dhinclusao to set
	 */
	public void setDhinclusao (Date dhinclusao)
	{
		this.dhinclusao = dhinclusao;
	}

	/**
	 * @return the nrusuarioalt
	 */
	public Long getNrusuarioalt ()
	{
		return nrusuarioalt;
	}

	/**
	 * @param nrusuarioalt the nrusuarioalt to set
	 */
	public void setNrusuarioalt (Long nrusuarioalt)
	{
		this.nrusuarioalt = nrusuarioalt;
	}

	/**
	 * @return the dhalteracao
	 */
	public Date getDhalteracao ()
	{
		return dhalteracao;
	}

	/**
	 * @param dhalteracao the dhalteracao to set
	 */
	public void setDhalteracao (Date dhalteracao)
	{
		this.dhalteracao = dhalteracao;
	}

	/**
	 * @return the cdconvenio
	 */
	public Long getCdconvenio ()
	{
		return cdconvenio;
	}

	/**
	 * @param cdconvenio the cdconvenio to set
	 */
	public void setCdconvenio (Long cdconvenio)
	{
		this.cdconvenio = cdconvenio;
	}

	/**
	 * @return the nrususolic
	 */
	public Long getNrususolic ()
	{
		return nrususolic;
	}

	/**
	 * @param nrususolic the nrususolic to set
	 */
	public void setNrususolic (Long nrususolic)
	{
		this.nrususolic = nrususolic;
	}

	/**
	 * @return the dsrtf
	 */
	public Blob getDsrtf ()
	{
		return dsrtf;
	}

	/**
	 * @param dsrtf the dsrtf to set
	 */
	public void setDsrtf (Blob dsrtf)
	{
		this.dsrtf = dsrtf;
	}

	/**
	 * @return the list images
	 */
	public List<Blob> getImages ()
	{
		return images;
	}

	/**
	 * @param List the images to set
	 */
	public void setImages (List<Blob> images)
	{
		this.images = images;
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
		result = prime * result + ((cdconvenio == null) ? 0 : cdconvenio.hashCode());
		result = prime * result + ((cdpessoa == null) ? 0 : cdpessoa.hashCode());
		result = prime * result + ((cdproced == null) ? 0 : cdproced.hashCode());
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
		final LaudoVO other = (LaudoVO)obj;
		if (cdconvenio == null)
		{
			if (other.cdconvenio != null)
			{
				return false;
			}
		}
		else if (!cdconvenio.equals(other.cdconvenio))
		{
			return false;
		}
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
		if (cdproced == null)
		{
			if (other.cdproced != null)
			{
				return false;
			}
		}
		else if (!cdproced.equals(other.cdproced))
		{
			return false;
		}
		return true;
	}
}
