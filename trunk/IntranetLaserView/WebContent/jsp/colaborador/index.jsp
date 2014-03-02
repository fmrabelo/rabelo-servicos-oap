<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
	<body>
		<h1>�rea do Colaborador</h1><hr/>
		<p>
			P�gina restrita a usu�rios autenticados
		<p>
		<sec:authorize ifAllGranted="ROLE_ADMIN">
			<p>
				Voc� � um administrador e tamb�m pode acessar esta <a href="../admin/index.jsp">p�gina</a>.
			<p>
		</sec:authorize>
		
		<a href="../">Voltar...</a><br>
		<a href="../j_spring_security_logout">Logout</a><br>
	</body>
</html>