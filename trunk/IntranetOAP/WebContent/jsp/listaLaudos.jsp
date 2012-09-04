<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>

<html>
<head>
	<s:head/>
	<sj:head jqueryui="true" compressed="true"/>
	
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Lista de Laudos - OAP</title>

	<script type="text/javascript" src="resources/js/oap.js"></script>
	<script type="text/javascript">
	
		/**
		 * Funções a serem executadas quando terminar de carregar a pagina
		 */
		$(document).ready(function (){
		});
		
		//abrir laudo pdf em nova guia.
		function openLaudo(nroCadastroPaciente, nrseqresultado, nrrequisicao)
		{
			var _action = '/IntranetOAP/servletReport?Text1=rtf&nroCadastroPaciente='+nroCadastroPaciente+'&nrseqresultado='+nrseqresultado+'&nrrequisicao='+nrrequisicao;
			console.log(_action);
			window.open(_action,'laudoOnLine','resizable=yes,scrollbars=yes,menubar=no,width=600,height=700,toolbar=no').focus();
		}
		
	</script>
</head>


<body>

<s:form action="#" method="get" validate="false" target="_blank">
<div align="center">
	
		<div class="widget titulo_branco_grande" align="center">
			<p>
				<img src="images/logo/logo1.jpg" alt="Logomarca OAP" width="50" height="50" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
				<s:text name="%{getText('business.title')}"/>
			</p>
		</div>	

	<table style="border-collapse:collapse;" align="center" cellpadding="1" cellspacing="1" border="0">
		<tr> 
		  	<td rowspan="2">
			</td>
		</tr>
		<tr>
			<!--center>
				O arquivo está em formato pdf, que requer o software Adobe Acrobat Reader. 
				Caso não o tenha instalado, clique no seguinte endereço http://get.adobe.com/br/reader/
			</center-->
		</tr>
	</table>

	<div>
		<div align="center" class="texto_vermelho_10px"> 
		  <p>
  		  <s:if test="hasFieldErrors() || hasActionErrors()">
  		  	  <img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
			  <s:fielderror/>
			  <s:actionerror/>
		  </s:if>
		  </p>
		</div>
		<div align="center" style="font:bold;color:blue;">
			<s:if test="hasActionMessages()">
				<img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
		   		<s:actionmessage/>
		   	</s:if>		 
		</div>	
	</div>
	
	<div align="center">
	<table style="border-collapse:collapse;" align="center" cellpadding="1" cellspacing="1" border="0">
		<tr>
			<p/>
	        <td align="left" style="text-align: left; text-decoration: none">
	        <b>
	        <p>
	        	<img src="images/001_54.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
	         <p>
	        </td>	
	        	  
	       	<p/><p/>      
	        <td align="left" style="text-align: center;color: blue;">
	        <b><p>  ${pessoaVo.cdPessoa}    -    ${pessoaVo.nomePessoa}  <p>
	        </td>	   
		</tr>
		<tr><td><p></td></tr>
	</table>
	</div>

	<!-- lista de laudos -->
	<div align="center">
		<div align="center">
			<s:if test="hasActionMessages()">
			<img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
			<s:actionmessage/>
			</s:if>
		</div>
		<br>
		<s:if test="%{!pessoaVo.listaLaudos.isEmpty()}">
			<div align="center" class="titulo_cinza_negrito_grande">
				<img src="images/001_18.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
				<s:property value="pessoaVo.listaLaudos.size"/><s:if test="%{pessoaVo.listaLaudos.size>1}">  Laudos Localizados</s:if><s:else>  Laudo Localizado</s:else>				 
			</div>
			<br>
			<div class="table oxo scrollListOap">
				<article class="list">
				<ul>
					<s:iterator value="pessoaVo.listaLaudos" status="songStatus">
						<li style="text-align: left">
							<a href="javascript:openLaudo('${pessoaVo.cdPessoa}','<s:property value="nrseqresultado"/>','<s:property value="nrrequisicao"/>');">
								<img src="images/printer1.jpg" alt="Imprimir" align="bottom" border="0"  width="5" height="5" border="0" style="background-color:transparent;"/>
							</a>			
							${dsexamecompl}							
							<s:date name="dtconsulta" format="dd/MM/yyyy"/>							
						</li>			
					</s:iterator>
				</ul>
				</article>
			</div>
		</s:if>
	</div>
	<div align="center" class="texto_Azul_12px">
        <s:if test="%{pessoaVo.urlSite=='oap.com'}">
    		<s:url id="show" action="autenticacaoLaudoOnline"/>
         	<s:a href="%{show}">
         		<img src="images/001_23.png" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
         		Voltar
         	</s:a>
        </s:if>
	</div>
</div>
</s:form>
</body>
</html>