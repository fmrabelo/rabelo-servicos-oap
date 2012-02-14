/**
 * 
 */
package br.com.oappr.intranet.vo;

import java.util.Date;
import java.util.List;

/**
 * @author desenvolvimento
 */
public final class PessoaVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10216598811338787L;

	private Long cdPessoa;
	private String nomePessoa;
	private Date dataNascimento;

	private List<LaudoVO> listaLaudos;

	/**
	 * @return the listaLaudos
	 */
	public List<LaudoVO> getListaLaudos ()
	{
		return listaLaudos;
	}

	/**
	 * @param listaLaudos the listaLaudos to set
	 */
	public void setListaLaudos (List<LaudoVO> listaLaudos)
	{
		this.listaLaudos = listaLaudos;
	}

	/**
	 * 
	 */
	public PessoaVO ()
	{
		super();
	}

	/**
	 * @param id
	 * @param descricao
	 */
	public PessoaVO (Long id, String descricao)
	{
		super();
	}

	/**
	 * @return the cdPessoa
	 */
	public Long getCdPessoa ()
	{
		return cdPessoa;
	}

	/**
	 * @param cdPessoa the cdPessoa to set
	 */
	public void setCdPessoa (Long cdPessoa)
	{
		this.cdPessoa = cdPessoa;
	}

	/**
	 * @return the nomePessoa
	 */
	public String getNomePessoa ()
	{
		return nomePessoa;
	}

	/**
	 * @param nomePessoa the nomePessoa to set
	 */
	public void setNomePessoa (String nomePessoa)
	{
		this.nomePessoa = nomePessoa;
	}

	/**
	 * @return the dataNascimento
	 */
	public Date getDataNascimento ()
	{
		return dataNascimento;
	}

	/**
	 * @param dataNascimento the dataNascimento to set
	 */
	public void setDataNascimento (Date dataNascimento)
	{
		this.dataNascimento = dataNascimento;
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
		result = prime * result + ((cdPessoa == null) ? 0 : cdPessoa.hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
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
		final PessoaVO other = (PessoaVO)obj;
		if (cdPessoa == null)
		{
			if (other.cdPessoa != null)
			{
				return false;
			}
		}
		else if (!cdPessoa.equals(other.cdPessoa))
		{
			return false;
		}
		if (dataNascimento == null)
		{
			if (other.dataNascimento != null)
			{
				return false;
			}
		}
		else if (!dataNascimento.equals(other.dataNascimento))
		{
			return false;
		}
		return true;
	}

}
