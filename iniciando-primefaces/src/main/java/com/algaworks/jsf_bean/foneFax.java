package com.algaworks.jsf_bean;

import java.io.Serializable;

public final class foneFax
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8416281484410881362L;

	private Long id;
	private String descricao;
	private Long pais;
	private Long ddd;
	private String fone;

	public String getDescricao ()
	{
		return descricao;
	}

	public void setDescricao (String descricao)
	{
		this.descricao = descricao;
	}

	public Long getPais ()
	{
		return pais;
	}

	public void setPais (Long pais)
	{
		this.pais = pais;
	}

	public Long getDdd ()
	{
		return ddd;
	}

	public void setDdd (Long ddd)
	{
		this.ddd = ddd;
	}

	public String getFone ()
	{
		return fone;
	}

	public void setFone (String fone)
	{
		this.fone = fone;
	}

	public Long getId ()
	{
		return id;
	}

}
