<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<sx:head />
	<link href="<s:url value="/resources/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>OAP - Oftalmologistas Associados do Paraná</title>
</head>

<body leftmargin="2">

<s:form action="autenticarPaciente" method="post">

	<h2><center>OAP - Oftalmologistas Associados do Paraná</center></h2>

	<table style="border-collapse:collapse;background-color: #CFD3A8;" width="400px" align="center" cellpadding="1" cellspacing="1">
		    <tr> 
		    	<td colspan="2" valign="top" align="center">
					<h1>Autenticar Paciente</h1>
				</td>
			</tr>
			<br>
			<tr>
				<td class="tdLabel" colspan="2" valign="top" align="center">					
						<s:textfield name="nroCadastroPaciente" label="Nº. Matricula Paciente"/>
						<s:fielderror fieldName="nroCadastroPaciente"/>
						<sx:datetimepicker name="dataNascimento" label="Data Nascimento" displayFormat="dd/MM/yyyy"/>
				</td>
			</tr>
			<br>	
			<tr align="center">
				<br><br>
				<table width="200px" align="center" border="0" cellpadding="1" cellspacing="1">
					<tr>
						<td width="100%" align="left" style="text-align: center; border-bottom-color: buttonface;">
							<s:submit value="Validar Paciente"/>
						</td>
					</tr>
				</table>
			</tr>
	</table>
	<br/>
</s:form>

</body>
</html>