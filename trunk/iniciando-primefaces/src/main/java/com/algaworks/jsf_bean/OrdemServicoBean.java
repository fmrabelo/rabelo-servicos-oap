package com.algaworks.jsf_bean;

import java.io.Serializable;
import java.util.Date;

public class OrdemServicoBean
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 841628787810881102L;

	private Long id;
	private Date dataEntrada;
	private String cliente;
	private String descricao;
	private Double preco;

	public Date getDataEntrada ()
	{
		return dataEntrada;
	}

	public void setDataEntrada (Date dataEntrada)
	{
		this.dataEntrada = dataEntrada;
	}

	public String getCliente ()
	{
		return cliente;
	}

	public void setCliente (String cliente)
	{
		this.cliente = cliente;
	}

	public String getDescricao ()
	{
		return descricao;
	}

	public void setDescricao (String descricao)
	{
		this.descricao = descricao;
	}

	public Double getPreco ()
	{
		return preco;
	}

	public void setPreco (Double preco)
	{
		this.preco = preco;
	}

	public void setId (Long id)
	{
		this.id = id;
	}

	public Long getId ()
	{
		return id;
	}

}
