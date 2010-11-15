<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>OAP - Oftalmologistas Associados do Paraná</title>
</head>
<body>

<h2>Olá mundo com Struts2!</h2>

<table width="600px" align="center">
	    <tr> 
    		<s:url id="show" action="listaTarefas"/>
	        <td>
	         	<s:a href="%{show}">Listar Tarefas...</s:a>
	        </td>

    		<s:url id="add" action="cadastroTarefa"/>
	        <td>
	         	<s:a href="%{add}">Adicionar Tarefas...</s:a>
	        </td>
	        
    	</tr>
</table>
<br/>

	
</body>
</html>