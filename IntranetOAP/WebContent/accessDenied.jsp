<html>
	<body>
		<h1>Spring Security</h1><hr/>
		<p>
			<font color="red">Acesso negado. O usu�rio n�o tem permiss�o para acessar essa p�gina.</font>
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
