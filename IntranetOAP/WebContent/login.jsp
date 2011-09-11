<html>
  <head>
    <title>Login</title>
  </head>

  <body>
    <h1>Área de Autenticação</h1><hr/>

	<p>Usuários:</p>
	<p>login <b>joao</b>, senha <b>123</b></p>
	<p>login <b>admin</b>, senha <b>123</b></p>
 
	<p>
		<!-- substituir o scriplet por JSTL -->
		<% if (request.getParameter("login_error") != null) { %>
			<font color="red">
		        Não foi possível se autenticar.<br/>
		        Motivo: ${SPRING_SECURITY_LAST_EXCEPTION.message}.
		    </font>
		<% } %>
	</p>

    <form action="j_spring_security_check" method="POST">
      <table>
        <tr>
        	<td>Login:</td>
        	<td><input type='text' name='j_username' value="${not empty login_error ? SPRING_SECURITY_LAST_USERNAME : ''}" /></td>
      	</tr>
        
        <tr>
        	<td>Senha:</td><td><input type='password' name='j_password'></td>
        </tr>
        
        <tr>
        	<td><input type="checkbox" name="_spring_security_remember_me"></td>
			<td>Salvar as minhas informações neste computador?</td>
		</tr>

        <tr>
	        <td><input name="submit" type="submit" value="Login"></td>
	        <td><input name="reset" type="reset" value="Limpar"></td>
        </tr>
      </table>

    </form>

	<a href="index.jsp">Voltar...</a><br>

  </body>
</html>