<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
	<s:head/>
	
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="resources/js/intranet.js"></script>
	<title><s:text name="%{getText('label.listaLaudos')}"/></title>
</head>


<body>

<s:form action="#" method="get" validate="false" target="_blank">
<div align="center">
	
	<div class="widget titulo_branco_grande" align="center" style="height: 50px;width: 90%">
		<img src="images/logo/logo1.jpg" alt="Logomarca" align="left" width="100" height="50" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
		
		<!--img src="images/001_54.png" alt="Usuário" width="20" height="20" border="0" style="background-color:transparent;"/-->&nbsp;&nbsp;
		<b style="padding-bottom: 50px"><br> ${pessoaVo.cdPessoa}    -    ${pessoaVo.nomePessoa} </b>			
	</div>

		<s:if test="%{!pessoaVo.listaLaudos.isEmpty()}">
			<table border="0">
				<tr>
					<td align="right" colspan="2" style="text-decoration: none; font-size: 11px; color:#666;">
			        	<!-- img src="images/acrobat.jpg" width="20px" border="0" style="background: #ECECEC;"/>&nbsp;&nbsp;
						Para visualizar os Laudos é necessário o programa Acrobat Reader. Caso não o tenha instalado,
						<a href="http://www.adobe.com.br/products/acrobat/readstep2.html" target="_new">clique aqui</a> 
						para fazer o download.-->
					</td>
				</tr>
			</table>
		</s:if>	

	<div>
  		 <s:if test="hasFieldErrors() || hasActionErrors()">
			<div align="center" class="texto_vermelho_12px"> 
			  <p>
	  		  	  <img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
				  <s:fielderror/>
				  <s:actionerror/>
			  </p>
			</div>
		 </s:if>
		 <s:if test="hasActionMessages()">
			<div align="center" style="font:bold;color:blue;">
				<img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
		   		<s:actionmessage/>
			</div>	
		 </s:if>		 
	</div>

	<!-- lista de laudos -->
	<div align="center">
		<s:if test="hasActionMessages()">
			<div align="center">
				<img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
				<s:actionmessage/>
			</div>
			<br>
		</s:if>
		<s:if test="%{!pessoaVo.listaLaudos.isEmpty()}">
			<div align="center" class="titulo_cinza_negrito_grande">
				<s:property value="pessoaVo.listaLaudos.size"/>
				<s:if test="%{pessoaVo.listaLaudos.size>1}">  <s:text name="%{getText('label.laudosLocalizados')}"/></s:if>
				<s:else>  <s:text name="%{getText('label.laudoLocalizado')}"/></s:else>				 
			</div>
			<br>
			
	        <a href="javascript:wback();">
	         	<img src="images/001_23.png" width="15" height="15" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
	         	<s:text name="%{getText('label.back')}"/>
	        </a>
			
			<div class="table oxo scrollList">
				<article class="list">
				<ul>
					<table border="0">
					<tr>
						<td align="center" class="titulo_cinza_negrito_grande">Exame</td>
						<td style="text-align: right;" class="titulo_cinza_negrito_grande">Data</td>
					</tr>
					<tr><td colspan="2"></td></tr>
					<s:iterator value="pessoaVo.listaLaudos" status="songStatus">
						<tr align="left">
							<td>
							<a href="javascript:openArquivoLocal('${dsexamecompl}','${pessoaVo.cdPessoa}');">
								<img src="images/printer1.jpg" border="0" class="list li img" style="width:1.4em; height:1.2em;" alt='Imprimir'/>
								${dsexamecompl}
							</a>			
							</td>
							<td align="right">							
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
        <!-- <s:if test="%{pessoaVo.urlSite=='is_url_site'}">
    		<s:url id="show" action="autenticacaoLaudoOnline"/>
         	<s:a href="%{show}">
         		<img src="images/001_23.png" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
         		<s:text name="%{getText('label.back')}"/>
         	</s:a>
        </s:if> -->
        <a href="javascript:wback();">
         	<img src="images/001_23.png" width="15" height="15" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
         	<s:text name="%{getText('label.back')}"/>
        </a>
	</div>
</div>
</s:form>
</body>
</html>

<script type="text/javascript">
function wback(){ 
	//var x=window.history.length; 
	// if (window.history[x]!=window.location) 
	//{ 
	    window.history.back(-1);	    
	//} 
}
</script> 