<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
	<body>
		<h1>Área do Colaborador</h1><hr/>
		<p>
			Página restrita a usuários autenticados
		<p>
		<sec:authorize ifAllGranted="ROLE_ADMIN">
			<p>
				Você é um administrador e também pode acessar esta <a href="../admin/index.jsp">página</a>.
			<p>
		</sec:authorize>
		
		<a href="../">Voltar...</a><br>
		<a href="../j_spring_security_logout">Logout</a><br>
	</body>
</html>