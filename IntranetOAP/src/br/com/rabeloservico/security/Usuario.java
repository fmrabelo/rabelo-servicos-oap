package br.com.rabeloservico.security;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 * @author desenvolvimento
 */
public class Usuario
    implements UserDetails
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4571027308527241712L;
	private String login;
	private String senha;
	private String email;
	private boolean ativo;
	private Integer tentativasLogin;
	private GrantedAuthority[] authorities;

	public String getUsername ()
	{
		return login;
	}

	public String getPassword ()
	{
		return senha;
	}

	public GrantedAuthority[] getAuthorities ()
	{
		return authorities;
	}

	public boolean isEnabled ()
	{
		return ativo;
	}

	public boolean isAccountNonLocked ()
	{
		return tentativasLogin < 3;
	}

	public boolean isAccountNonExpired ()
	{
		return true;
	}

	public boolean isCredentialsNonExpired ()
	{
		return true;
	}

	// --- getters and setters ---

	public String getLogin ()
	{
		return login;
	}

	public void setLogin (String login)
	{
		this.login = login;
	}

	public String getSenha ()
	{
		return senha;
	}

	public void setSenha (String senha)
	{
		this.senha = senha;
	}

	public String getEmail ()
	{
		return email;
	}

	public void setEmail (String email)
	{
		this.email = email;
	}

	public boolean isAtivo ()
	{
		return ativo;
	}

	public void setAtivo (boolean ativo)
	{
		this.ativo = ativo;
	}

	public Integer getTentativasLogin ()
	{
		return tentativasLogin;
	}

	public void setTentativasLogin (Integer tentativasLogin)
	{
		this.tentativasLogin = tentativasLogin;
	}

	public void setAuthorities (GrantedAuthority[] authorities)
	{
		this.authorities = authorities;
	}

}
