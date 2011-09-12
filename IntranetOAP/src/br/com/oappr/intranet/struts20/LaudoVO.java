/**
 * 
 */
package br.com.oappr.intranet.struts20;

import java.util.Date;

/**
 * @author rabelo
 */
public final class LaudoVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10216598875464987L;

	private Long id;
	private String descricao;
	private boolean finalizado;
	private Date dataFinalizacao;

	/**
	 * 
	 */
	public LaudoVO ()
	{
		super();
	}

	/**
	 * @param id
	 * @param descricao
	 */
	public LaudoVO (Long id, String descricao)
	{
		super();
		this.id = id;
		this.descricao = descricao;
	}

	/**
	 * @return the id
	 */
	public Long getId ()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao ()
	{
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao (String descricao)
	{
		this.descricao = descricao;
	}

	/**
	 * @return the finalizado
	 */
	public boolean isFinalizado ()
	{
		return finalizado;
	}

	/**
	 * @param finalizado the finalizado to set
	 */
	public void setFinalizado (boolean finalizado)
	{
		this.finalizado = finalizado;
	}

	/**
	 * @return the dataFinalizacao
	 */
	public Date getDataFinalizacao ()
	{
		return dataFinalizacao;
	}

	/**
	 * @param dataFinalizacao the dataFinalizacao to set
	 */
	public void setDataFinalizacao (Date dataFinalizacao)
	{
		this.dataFinalizacao = dataFinalizacao;
	}

}
