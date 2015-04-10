package com.algaworks.jsf_bean;

import javax.faces.bean.ManagedBean;

@ManagedBean
// usando bean gerenciado por JSF padrão.
public class Ola_JSF_Bean
{
	private String nome;
	private String sobrenome;
	private String nomeCompleto;

	public void dizerOla ()
	{
		this.nomeCompleto = this.nome.toUpperCase() + " " + this.sobrenome;
	}

	public String getNome ()
	{
		return nome;
	}

	public void setNome (String nome)
	{
		this.nome = nome;
	}

	public String getSobrenome ()
	{
		return sobrenome;
	}

	public void setSobrenome (String sobrenome)
	{
		this.sobrenome = sobrenome;
	}

	public String getNomeCompleto ()
	{
		return nomeCompleto;
	}

	public void setNomeCompleto (String nomeCompleto)
	{
		this.nomeCompleto = nomeCompleto;
	}

}
