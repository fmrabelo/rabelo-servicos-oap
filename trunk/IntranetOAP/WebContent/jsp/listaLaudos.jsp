<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
	<s:head/>
	
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="resources/js/oap.js"></script>
	<title><s:text name="%{getText('label.listaLaudosOAP')}"/></title>
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

	<div align="center">
		<s:if test="%{!pessoaVo.listaLaudos.isEmpty()}">
			<table style="border-collapse:collapse; width:100%; size:10px;" align="center" cellpadding="1" cellspacing="1" border="0">
				<tr style="background-color: #ECECEC;">
					<td colspan="2" align="center" style="text-align: center; text-decoration: none; font-size: 11px; color:#666; left: 510px; top: 160px; ">
			        	<img src="images/acrobat.jpg" width="20px" border="0" style="background: #ECECEC;"/>&nbsp;&nbsp;
						Para visualizar os Laudos é necessário o programa Acrobat Reader. Caso não o tenha instalado,
						<a href="http://www.adobe.com.br/products/acrobat/readstep2.html" target="_new">clique aqui</a> 
						para fazer o download.
					</td>
				</tr>
			</table>
		</s:if>
	</div>	
	<div align="center">
		<table style="border-collapse:collapse; width: 100%;" align="center" cellpadding="1" cellspacing="1" border="0">
			<tr>
		        <td colspan="2" align="center" style="text-align: center;color: blue; text-decoration: none">
		        	<img src="images/001_54.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
		        	<b>${pessoaVo.cdPessoa}    -    ${pessoaVo.nomePessoa}
		        </td>	   
			</tr>
		</table>
	</div>

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
				<s:property value="pessoaVo.listaLaudos.size"/>
				<s:if test="%{pessoaVo.listaLaudos.size>1}">  <s:text name="%{getText('label.laudosLocalizados')}"/></s:if>
				<s:else>  <s:text name="%{getText('label.laudoLocalizado')}"/></s:else>				 
			</div>
			<br>
			<div class="table oxo scrollListOap">
				<article class="list">
				<ul>
					<table>
					<tr>
						<td align="center" class="texto_Azul_Negrito_20px">Laudo</td>
						<td class="texto_Azul_Negrito_20px">Data</td>
					</tr>
					<tr><td colspan="2"></td></tr>
					<s:iterator value="pessoaVo.listaLaudos" status="songStatus">
						<tr>
							<td>
							<a href="javascript:openLaudo('${pessoaVo.cdPessoa}','<s:property value="nrseqresultado"/>','<s:property value="nrrequisicao"/>');">
								<img src="images/printer1.jpg" class="list li img" style="width:2em; height:1.7em;" alt='Imprimir'/>
								${dsexamecompl}
							</a>			
							</td>
							<td>							
							<s:date name="dtconsulta" format="dd/MM/yyyy"/>
							</td>
						</tr>							
					</s:iterator>
					</table>
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
         		<s:text name="%{getText('label.back')}"/>
         	</s:a>
        </s:if>
	</div>
</div>
</s:form>
</body>
</html>