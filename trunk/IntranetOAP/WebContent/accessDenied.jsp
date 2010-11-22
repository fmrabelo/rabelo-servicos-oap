<html>
	<body>
		<h1>Spring Security</h1><hr/>
		<p>
			<font color="red">Acesso negado. O usuário não tem permissão para acessar essa página.</font>
		<p>
		<p>
			Remote user....: <%= request.getRemoteUser() %>
		</p>		
		<p>
			User principal....: <%= request.getUserPrincipal() %>
		</p>
		<a href="../">Voltar...</a><br>
		<a href="../j_spring_security_logout">Logout</a><br>
	</body>
</html>
