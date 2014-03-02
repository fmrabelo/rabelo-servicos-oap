/**
 * 
 */
package br.com.laserviewpr.intranet.vo;

/**
 * Super Classe Pojo para os atributos dos dados do tipo usuario.
 * @author Rabelo Serviços.
 */
public class UsuarioVO
    extends PessoaVO
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2266002916003886747L;
	private Long nrUsuario;
	private Long cdPessoa;
	private String nmUsuario;
	private String cdUsuario;
	private int flMedico;
	private Long cdProced;

	/**
	 * 
	 */
	public UsuarioVO ()
	{
		super();
	}

	/**
	 * @return the nrUsuario
	 */
	public Long getNrUsuario ()
	{
		return nrUsuario;
	}

	/**
	 * @param nrUsuario the nrUsuario to set
	 */
	public void setNrUsuario (Long nrUsuario)
	{
		this.nrUsuario = nrUsuario;
	}

	/**
	 * @return the cdPessoa
	 */
	@Override
	public Long getCdPessoa ()
	{
		return cdPessoa;
	}

	/**
	 * @param cdPessoa the cdPessoa to set
	 */
	@Override
	public void setCdPessoa (Long cdPessoa)
	{
		this.cdPessoa = cdPessoa;
	}

	/**
	 * @return the nmUsuario
	 */
	public String getNmUsuario ()
	{
		return nmUsuario;
	}

	/**
	 * @param nmUsuario the nmUsuario to set
	 */
	public void setNmUsuario (String nmUsuario)
	{
		this.nmUsuario = nmUsuario;
	}

	/**
	 * @return the cdUsuario
	 */
	public String getCdUsuario ()
	{
		return cdUsuario;
	}

	/**
	 * @param cdUsuario the cdUsuario to set
	 */
	public void setCdUsuario (String cdUsuario)
	{
		this.cdUsuario = cdUsuario;
	}

	/**
	 * @return the flMedico
	 */
	public int getFlMedico ()
	{
		return flMedico;
	}

	/**
	 * @param flMedico the flMedico to set
	 */
	public void setFlMedico (int flMedico)
	{
		this.flMedico = flMedico;
	}

	/**
	 * @return the cdProced
	 */
	public Long getCdProced ()
	{
		return cdProced;
	}

	/**
	 * @param cdProced the cdProced to set
	 */
	public void setCdProced (Long cdProced)
	{
		this.cdProced = cdProced;
	}

}
