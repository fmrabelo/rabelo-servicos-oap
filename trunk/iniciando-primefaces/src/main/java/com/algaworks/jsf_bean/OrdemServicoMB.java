package com.algaworks.jsf_bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
// usando bean gerenciado por JSF padrão.
@RequestScoped
public class OrdemServicoMB
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7803140885454757765L;

	private OrdemServicoBean ordemServico;

	public void salvar ()
	{
		System.out.println("----------------salvar---------------------");
		System.out.println("Cliente:" + ordemServico.getCliente());
		System.out.println("Descricao:" + ordemServico.getDescricao());
		System.out.println("Preco:" + ordemServico.getPreco());
		System.out.println("Dt.Entr.:" + ordemServico.getDataEntrada());
	}

	public OrdemServicoBean getOrdemServico ()
	{
		if (this.ordemServico == null) this.setOrdemServico(new OrdemServicoBean());
		return this.ordemServico;
	}

	public void setOrdemServico (OrdemServicoBean ordemServico)
	{
		this.ordemServico = ordemServico;
	}

}
