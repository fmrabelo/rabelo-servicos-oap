<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
  <head>
    <title>Spring Security</title>
  </head>
  
  <body>
		<h1>Spring Security</h1><hr/>		

		<p>
			Está página é pública, todos podem vê-la  
		</p>

		<p>
			<a href="usuarios/index.jsp">Página dos usuários</a> - apenas para usuários do ROLE_USUARIO<br>
			<a href="admin/index.jsp">Página dos administradores</a> - apenas para usuários do ROLE_ADMIN<br>
		</p>
		
		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_USUARIO">
			<p>
				<b>Informações do usuário logado:</b><br>
				Login: <sec:authentication property="principal.login" /><br>
				Senha: <sec:authentication property="principal.senha" /><br>
				E-mail: <sec:authentication property="principal.email" /><br>
			</p>
			
			<a href="j_spring_security_logout">Logout</a><br>
		</sec:authorize>
  </body>
</html>