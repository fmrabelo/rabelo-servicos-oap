/**
 * 
 */
package br.com.oappr.intranet.vo;

import java.util.Date;
import java.util.List;

/**
 * Super Classe Pojo para os atributos dos dados do tipo pessoa.
 * @author Rabelo Serviços.
 */
public class PessoaVO
    implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10216598811338787L;

	private Long cdPessoa;
	private String nomePessoa;
	private Date dataNascimento;
	private String endereco;
	private String bairro;
	private String complEndereco;
	private String cidade;
	private String uf;
	private String cep;
	private String email;
	private String urlSite;
	private String sexo;
	private List<FoneVO> listaFone;

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

	/**
	 * @return the endereco
	 */
	public String getEndereco ()
	{
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco (String endereco)
	{
		this.endereco = endereco;
	}

	/**
	 * @return the complEndereco
	 */
	public String getComplEndereco ()
	{
		return complEndereco;
	}

	/**
	 * @param complEndereco the complEndereco to set
	 */
	public void setComplEndereco (String complEndereco)
	{
		this.complEndereco = complEndereco;
	}

	/**
	 * @return the cidade
	 */
	public String getCidade ()
	{
		return cidade;
	}

	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade (String cidade)
	{
		this.cidade = cidade;
	}

	/**
	 * @return the uf
	 */
	public String getUf ()
	{
		return uf;
	}

	/**
	 * @param uf the uf to set
	 */
	public void setUf (String uf)
	{
		this.uf = uf;
	}

	/**
	 * @return the cep
	 */
	public String getCep ()
	{
		return cep;
	}

	/**
	 * @param cep the cep to set
	 */
	public void setCep (String cep)
	{
		this.cep = cep;
	}

	/**
	 * @return the email
	 */
	public String getEmail ()
	{
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail (String email)
	{
		this.email = email;
	}

	/**
	 * @return the urlSite
	 */
	public String getUrlSite ()
	{
		return urlSite;
	}

	/**
	 * @param urlSite the urlSite to set
	 */
	public void setUrlSite (String urlSite)
	{
		this.urlSite = urlSite;
	}

	/**
	 * @return the listaFone
	 */
	public List<FoneVO> getListaFone ()
	{
		return listaFone;
	}

	/**
	 * @param listaFone the listaFone to set
	 */
	public void setListaFone (List<FoneVO> listaFone)
	{
		this.listaFone = listaFone;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro ()
	{
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro (String bairro)
	{
		this.bairro = bairro;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo ()
	{
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo (String sexo)
	{
		this.sexo = sexo;
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
