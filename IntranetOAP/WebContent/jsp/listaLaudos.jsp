<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<s:head/>
	<sx:head/>
	<link href="<s:url value="/resources/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Lista de Laudos Cadastrados - OAP - Oftalmologistas Associados do Paraná</title>
</head>


<body>

<s:form action="#" method="get" validate="false" target="_blank">

	<table style="border-collapse:collapse;" align="center" cellpadding="1" cellspacing="1" border="0">
		<tr> 
		  	<td rowspan="2">
				<img src="images/logo/logo1.jpg" alt="Logomarca OAP" width="150" height="150" border="0" style="background-color:transparent;"/>
			</td>
		</tr>
		<tr>
			<td>
				<br> 
				<span class="texto_TNR_Azul_26px">OFTALMOLOGISTAS ASSOCIADOS DO PARANÁ</span>					
			</td>
			<!--center>
				O arquivo está em formato pdf, que requer o software Adobe Acrobat Reader. 
				Caso não o tenha instalado, clique no seguinte endereço http://get.adobe.com/br/reader/
			</center-->
		</tr>
	</table>

	<table style="border-collapse:collapse;" align="center" cellpadding="1" cellspacing="1" border="0">
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
				<span class="texto_azul_negrito_22px">Laudos</span> 
			</th>
		</tr>
		
	</table>

	<table align="center" style="border-collapse:collapse;background-color: #CFD3A8;" border="0" cellpadding="1" >
		<s:iterator value="pessoaVo.listaLaudos" status="songStatus">
			<tr class="<s:if test="#songStatus.odd == true ">odd</s:if><s:else>even</s:else>" >
				<td>
					${dsexamecompl}
					<!-- Assim tambem funciona...JSTL -->
				</td> 
				<td>
					<s:date name="dtconsulta" format="dd/MM/yyyy"/>
				</td>
				
				<td style="text-align: center;">
					<!--s:url id="gerarPDF" action="gerarLaudoPdf"-->
						<!--s:param name="id" value="id" /-->
					<!--/s:url-->
					<!--s:a id="a_%{id}" href="%{gerarPDF}"-->
						<!--b-->
						<!--img src="images/printer1.jpg" alt="Imprimir" align="bottom" border="none"/-->
					<!--/s:a-->
					
					<b>

					<!-- s:url id="gerarRTF" includeParams="all" value="/servletReport?Text1=rtf"-->
						<!-- s:param name="nrseqresultado" value="nrseqresultado" /-->
						<!-- s:param name="nrrequisicao" value="nrrequisicao" /-->
					<!-- /s:url-->
					<!-- s:a id="a_%{nrseqresultado}" href="%{gerarRTF}" onclick="alert('Esse processo pode demorar alguns segundos!! Clique no OK para continuar!!');" -->
					<!-- s:a id="a_%{nrseqresultado}" href="%{gerarRTF}" onclick="javascript:openLaudo(this.href);"-->
						<!-- b-->
						<!-- img src="images/printer1.jpg" alt="Imprimir" align="bottom" border="none"/-->
					<!-- /s:a-->		
					
					<a href="javascript:openLaudo('${pessoaVo.cdPessoa}','<s:property value="nrseqresultado"/>','<s:property value="nrrequisicao"/>');">
						<img src="images/printer1.jpg" alt="Imprimir" align="bottom" border="none"/>
					</a>			

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

	//abrir laudo pdf em nova guia.
	function openLaudo(nroCadastroPaciente, nrseqresultado, nrrequisicao)
	{
		var _action = '/IntranetOAP/servletReport?Text1=rtf&nroCadastroPaciente='+nroCadastroPaciente+'&nrseqresultado='+nrseqresultado+'&nrrequisicao='+nrrequisicao;
		window.open(_action,'laudoOnLine','resizable=yes,scrollbars=yes,menubar=no,width=600,height=700,toolbar=no').focus();
	}
	
</script>