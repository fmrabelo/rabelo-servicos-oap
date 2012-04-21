package br.com.rabeloservico.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.event.authentication.AbstractAuthenticationEvent;
import org.springframework.security.event.authentication.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;

/**
 * Classe para validação usuario.
 * @author Rabelo Serviços.
 */
public class IncorrectPasswordEventListener
    extends JdbcDaoSupport
    implements ApplicationListener
{

	public void onApplicationEvent (ApplicationEvent event)
	{
		if (event instanceof AuthenticationFailureBadCredentialsEvent)
		{
			AuthenticationFailureBadCredentialsEvent badCredentialsEvent = (AuthenticationFailureBadCredentialsEvent)event;

			String sql = "update Usuario set tentativas_login = tentativas_login + 1 where login = ?";
			this.executeSql(badCredentialsEvent, sql);
		}
		if (event instanceof AuthenticationSuccessEvent)
		{
			AuthenticationSuccessEvent successEvent = (AuthenticationSuccessEvent)event;

			String sql = "update Usuario set tentativas_login = 0 where login = ?";
			this.executeSql(successEvent, sql);
		}
	}

	private void executeSql (AbstractAuthenticationEvent event, String sql)
	{
		this.getJdbcTemplate().update(sql, new Object[]{event.getAuthentication().getName()});
	}

}
