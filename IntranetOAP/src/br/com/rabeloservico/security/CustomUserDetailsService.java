package br.com.rabeloservico.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.jdbc.JdbcDaoImpl;

/**
 * @author desenvolvimento
 */
public class CustomUserDetailsService
    extends JdbcDaoImpl
{

	@Override
	protected UserDetails createUserDetails (String username, UserDetails userFromUserQuery,
	    GrantedAuthority[] combinedAuthorities)
	{
		Usuario usuario = new Usuario();

		usuario.setLogin(userFromUserQuery.getUsername());
		usuario.setSenha(userFromUserQuery.getPassword());
		usuario.setAtivo(userFromUserQuery.isEnabled());
		usuario.setAuthorities(combinedAuthorities);

		this.carregarInformacoesAdicionais(usuario);

		return usuario;
	}

	private void carregarInformacoesAdicionais (final Usuario usuario)
	{
		String sql = "select email, tentativas_login from Usuario where login = ?";

		this.getJdbcTemplate().query(sql, new Object[]{usuario.getUsername()}, new RowMapper() {
			public Object mapRow (ResultSet rs, int rowNum) throws SQLException
			{
				usuario.setEmail(rs.getString("email"));
				usuario.setTentativasLogin(rs.getInt("tentativas_login"));
				return null;
			}
		});
	}

}
