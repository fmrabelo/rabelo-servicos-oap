package com.algaworks.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.erp.model.Empresa;
import com.algaworks.erp.repository.Empresas;
import com.algaworks.erp.util.Transacional;

/**
 * Classe de serviço com regras de negócio.
 * @author rabelo
 */
public class CadastroEmpresaService
    implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Transacional
	public void salvar (Empresa empresa)
	{
		// regra de negocios
		empresas.guardar(empresa);
	}

	@Transacional
	public void excluir (Empresa empresa)
	{
		// regra de negocios
		empresas.remover(empresa);
	}

}