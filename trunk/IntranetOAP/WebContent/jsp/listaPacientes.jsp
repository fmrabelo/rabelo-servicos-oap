<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>

<html>
<head>
	<s:head/>
	<sj:head jqueryui="true" compressed="true"/>

	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="user-scalable=no, initial-scale = 1, minimum-scale = 1, maximum-scale = 1, width=device-width" />
	<title><s:text name="application.title"/></title>

	<script type="text/javascript" src="resources/js/oap.js"></script>
	<script type="text/javascript">
	
		/**
		 * Funções a serem executadas quando terminar de carregar a pagina
		 */
		$(document).ready(function (){
	
		});
		
		var form = document.forms[0];
	
		function validar()
		{
			var msg_ = 'Código do Paciente deve ser preenchido somente com Números.';
			var codPaciente_ = form.codPaciente.value;
			if(codPaciente_ != null && !validarNumero(codPaciente_))
		  	{
		  		form.codPaciente.value='';
		  		alert(msg_);
		  		return false;
		  	}
		}
	
		function cancelLaudosDialogIDButton() {
			$('#laudosDialogID').dialog('close');
		}	
	</script>	
	
</head>

	<!-- listar laudos dialog  -->
	<s:url id="urlLaudosDialogID" action="listarLaudos"/>
	<sj:dialog 
	   	id="laudosDialogID" 
	   	title="Laudos" 
	   	loadingText="Loading..." 
	   	autoOpen="false" 
	   	modal="true" 
	   	showEffect="slide" 
	   	hideEffect="slide"	   	
	   	resizable="false" 
	   	position="center" 
	   	height="800"
	   	width="800"
	   	closeOnEscape="true"
	   	/>

<body>
	<div align="center">
		<s:form action="initListarPacientes" id="form" method="post" validate="false" cssStyle="margin-top: 3%;">
			
			<div class="widget titulo_branco_grande" align="center">
				<p>
					<img src="images/001_37.png" alt="Pesquisa" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
					<s:text name="%{getText('label.findPatients')}"/>
				</p>
			</div>
			<div class="titulo_cinza_negrito_grande" align="center"></div>
			<div class="titulo_cinza_negrito_grande" align="center">
				<p align="center" style="font-size: 13pt">
					<img src="images/001_54.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;					
					<s:property value="user.nomePessoa"/>
				</p>
				<p align="left">
					<img src="images/001_53.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
					Use os campos abaixo para pesquisar Pacientes pelo Código ou Nome
				</p>
			</div>				
			<div align="center" class="texto_vermelho_10px"> 
			  	<s:fielderror/> 	 	
			  	<s:actionerror/>
			</div>
			<div>
		      		<s:textfield name="codPaciente" id="codPaciente" required="true" placeholder="Código do Paciente" cssStyle="width:250px" onkeypress="javascript:validarConteudo(event, 'numero');"/>
		      		<s:textfield name="nomePaciente" id="nomePaciente" placeholder="Nome do Paciente" cssStyle="width:250px"/>
			</div>
			<table>
				<tr>
					<td>
			   			<s:submit action="listarPacientes" id="listarPacientesID" key="label.find" onclick="javascrit:validar();" cssClass="button" cssStyle="width:200px"/>
					</td>
					<!--td>		
			   	   		<s:submit action="initListarPacientes" id="initListarPacientesID" key="label.cancel" cssClass="button" cssStyle="width:200px"/>
					</td-->		
				</tr>
			</table>
		</s:form>
	</div>
	
	<!-- lista de pacientes -->
	<div align="center">
		<div align="center">
		<s:actionmessage/>
		</div>
		<br>
		<s:if test="%{!listPacientes.isEmpty()}">
			<div align="center" class="titulo_cinza_negrito_grande">
				<img src="images/001_18.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
				<s:property value="listPacientes.size"/><s:if test="%{listPacientes.size>1}">  Pacientes Localizados</s:if><s:else>  Paciente Localizado</s:else>				 
			</div>
			<br>
			<div class="table oxo scrollListOap">
				<article class="list">
				<ul>
					<s:iterator value="listPacientes" status="pacienteStatus">
						<li style="text-align: left">
							<sj:a openDialog="laudosDialogID" href="%{urlLaudosDialogID}?nroCadastroPaciente=%{cdPessoa}">${cdPessoa} - ${nomePessoa}</sj:a>
						</li>			
					</s:iterator>
				</ul>
				</article>
			</div>
		</s:if>
	</div>	
	
</body>
</html>
