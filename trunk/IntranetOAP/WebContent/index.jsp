<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
  <head>
    <title>Spring Security</title>
  </head>
  
  <body>
		<h1>Spring Security</h1><hr/>		

		<p>
			Est� p�gina � p�blica, todos podem v�-la  
		</p>

		<p>
			<a href="usuarios/index.jsp">P�gina dos usu�rios</a> - apenas para usu�rios do ROLE_USUARIO<br>
			<a href="admin/index.jsp">P�gina dos administradores</a> - apenas para usu�rios do ROLE_ADMIN<br>
		</p>
		
		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_USUARIO">
			<p>
				<b>Informa��es do usu�rio logado:</b><br>
				Login: <sec:authentication property="principal.login" /><br>
				Senha: <sec:authentication property="principal.senha" /><br>
				E-mail: <sec:authentication property="principal.email" /><br>
			</p>
			
			<a href="j_spring_security_logout">Logout</a><br>
		</sec:authorize>
  </body>
</html>