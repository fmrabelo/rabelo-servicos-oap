<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>OAP - Oftalmologistas Associados do Paraná</title>
</head>
<body>

	<h2><center>OAP - Oftalmologistas Associados do Paraná</center></h2>

	<table width="400px" align="center" border="0" cellpadding="1" cellspacing="1">
	
	<h3>Autenticar Paciente</h3>
	
		<s:form action="autenticarPaciente" method="post">
			<s:textfield name="nroCadastroPaciente" label="Nro.Paciente"> </s:textfield>
			<s:fielderror fieldName="nroCadastroPaciente"/>
			<s:submit value="autenticarPaciente"/>
		</s:form>
	</table>
	
	<table width="600px" align="center" border="0" cellpadding="1" cellspacing="1">
		    <tr> 
	    		<s:url id="show" action="olaMundoStruts"/>
		        <td>
		         	<s:a href="%{show}">Limpar</s:a>
		        </td>
	
	    		<s:url id="laudos" action="listarLaudos"/>
		        <td>
		         	<s:a href="%{laudos}">Listar Laudos</s:a>
		        </td>
		        
	    	</tr>
	</table>
	<br/>
	
</body>
</html>