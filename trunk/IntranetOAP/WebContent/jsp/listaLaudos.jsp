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

<s:form action="#" method="get" validate="false">

	<h2>
		<center>OAP - Oftalmologistas Associados do Paraná</center>
		<!--center>
			O arquivo está em formato pdf, que requer o software Adobe Acrobat Reader. 
			Caso não o tenha instalado, clique no seguinte endereço http://get.adobe.com/br/reader/
		</center-->
	</h2>
	
	<table border="0" align="center">
		<tr>
		
			<p/>
	        <td align="left" style="text-align: left; text-decoration: none">
	        <b><p>PACIENTE:<p>
	        </td>	
	        	  
	       	<p/><p/>      
	        <td align="left" style="text-align: center;color: blue;">
	        <b><p>  ${pessoaVo.cdPessoa}    -    ${pessoaVo.nomePessoa}  <p>
	        </td>	   
		</tr>
		<tr><td><p></td></tr>
		<tr>
			<th align="center" style="text-align: center;" colspan="5">
				Laudos<br>
			</th>
		</tr>
		
	</table>

	<table align="center" style="border-collapse:collapse;background-color: #CFD3A8;">
		
		<tr class="titleDiv" style="text-align: center;>
			<p/>
			<th>Nro Resultado</th>
			<th>Nro Requisição</th>
			<th>Descrição Exame</th>
			<th>Data Exame</th>
			<th>Relatório</th>
		</tr>		
		<s:iterator value="pessoaVo.listaLaudos" status="songStatus">
			<tr class="<s:if test="#songStatus.odd == true ">odd</s:if><s:else>even</s:else>" >
				<td><s:property value="nrseqresultado"/></td>
				<td><s:property value="nrrequisicao"/></td>
				<td>
					${dsexamecompl}
					<!-- Assim tambem funciona...JSTL -->
				</td> 
				<td>
					<s:date name="dtconsulta" format="dd/MM/yyyy"/>
				</td>
				
				<td style="text-align: center;">
					<s:url id="gerarPDF" action="gerarLaudoPdf">
						<s:param name="id" value="id" />
					</s:url>
					<s:a id="a_%{id}" href="%{gerarPDF}">
						<b>
						<img src="images/printer1.jpg" alt="Imprimir" align="bottom" border="none"/>
					</s:a>
					
					<b>

					<s:url id="gerarRTF" includeParams="all" value="/servletReport?Text1=rtf">
						<s:param name="nrseqresultado" value="nrseqresultado" />
					</s:url>
					<s:a id="a_%{nrseqresultado}" href="%{gerarRTF}" onclick="alert('Esse processo pode demorar alguns segundos!! Clique no OK para continuar!!');">
						<b>
						<img src="images/printer1.jpg" alt="Imprimir" align="bottom" border="none"/>
					</s:a>					

				</td>
			</tr>
		</s:iterator>
	</table>
	
	<table border="0" align="center">
		<tr class="titleDiv">
			<p/><p/>
    		<s:url id="show" action="autenticacaoLaudoOnline"/>
	        <td align="center" style="text-align: center;">
	         	<s:a href="%{show}">Voltar</s:a>
	        </td>		
		</tr>
	</table>

</s:form>
</body>
</html>

<script type="text/javascript">

	var form = document.forms[0];
	
	function gerarRelatorio(id)
	{
		var _action = '/IntranetOAP/servletReport?Text1=rtf&nroResultado='+id;
		alert( _action );
		form.action = _action;
		form.submit();
	}
</script>