<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<link href="<s:url value="/resources/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Lista de Laudos Cadastrados</title>
</head>

<body>
	
	<h2><center>OAP - Oftalmologistas Associados do Paraná</center></h2>
	
	<table border="0" align="center">
		<tr>
			<p/>
	        <td align="left" style="text-align: left; text-decoration: underline">
	        <b><p>PACIENTE:<p>
	        </td>	
	        	  
	       	<p/><p/>      
	        <td align="left" style="text-align: center;color: blue;">
	        <p> [ MARIA DA CONCEIÇÃO CRISANTO MALLIN ] <p>
	        </td>	   
		</tr>
	</table>

	<table align="center">
		<tr class="titleDiv" style="text-align: center; text-decoration: underline">
			<p/>
			<th>Id</th>
			<th>Descrição de Laudos</th>
			<th>Finalizado</th>
			<th>Data</th>
			<th>Relatório</th>
		</tr>		
		<s:iterator value="listaLaudos" status="songStatus">
			<tr class="<s:if test="#songStatus.odd == true ">odd</s:if><s:else>even</s:else>" >
			
				<td><s:property value="id"/></td>
				<td>
					${descricao}
					<!-- Assim tambem funciona...JSTL -->
				</td> 
				<td style="text-align: center;">
					<s:if test="finalizado == false">
						NAO
					</s:if>
					<s:if test="finalizado == true">
						SIM
					</s:if>
				</td>
				<td>
					<s:date name="dataFinalizacao" format="dd/MM/yyyy"/>
				</td>
				
				<td style="text-align: center;">
					<s:url id="gerarPDF" action="gerarLaudoPdf">
						<s:param name="id" value="id" />
					</s:url>
					<s:a id="a_%{id}" href="%{gerarPDF}">
						<b>PDF
					</s:a>					
				</td>
			</tr>
		</s:iterator>
	</table>
	
	<table border="0" align="center">
		<tr class="titleDiv">
			<p/><p/>
    		<s:url id="show" action="olaMundoStruts"/>
	        <td align="center" style="text-align: center;">
	         	<s:a href="%{show}">Voltar</s:a>
	        </td>		
		</tr>
	</table>
</body>
</html>