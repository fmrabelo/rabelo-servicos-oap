package com.algaworks.jsf_bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;

@ManagedBean
// usando bean gerenciado por JSF padrão.
@ViewScoped
public class nomesBean
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3026065396747774260L;
	private String nome;
	private List<String> nomes = new ArrayList<>();

	// ligação com componentes através do backing bean
	private HtmlInputText inputNome;
	private HtmlCommandButton botaoAdicionar;

	public void adicionar ()
	{
		this.nomes.add(nome);
		// desativa campo e botão quando mais que 3 nomes
		// forem adicionados
		if (this.nomes.size() > 3)
		{
			this.inputNome.setDisabled(true);
			this.botaoAdicionar.setDisabled(true);
			this.botaoAdicionar.setValue("Muitos nomes adicionados...");
		}
	}

	public List<String> getNomes ()
	{
		return nomes;
	}

	public void setNomes (List<String> nomes)
	{
		this.nomes = nomes;
	}

	public HtmlInputText getInputNome ()
	{
		return inputNome;
	}

	public void setInputNome (HtmlInputText inputNome)
	{
		this.inputNome = inputNome;
	}

	public HtmlCommandButton getBotaoAdicionar ()
	{
		return botaoAdicionar;
	}

	public void setBotaoAdicionar (HtmlCommandButton botaoAdicionar)
	{
		this.botaoAdicionar = botaoAdicionar;
	}

	public String getNome ()
	{
		return nome;
	}

	public void setNome (String nome)
	{
		this.nome = nome;
	}

}
