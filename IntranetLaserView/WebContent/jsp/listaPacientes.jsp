<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>

<html>
<head>
	<s:head/>
	<sj:head jqueryui="true" compressed="true"/>

	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>	
	<script type="text/javascript" src="resources/js/intranet.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="user-scalable=no, initial-scale = 1, minimum-scale = 1, maximum-scale = 1, width=device-width" />
	<title><s:text name="application.title"/></title>

</head>

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
				<p align="center" style="font-size: 15pt; color:#050607;">
					<img src="images/001_54.png" alt="Usuário" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;					
					<s:property value="user.nomePessoa"/>
				</p>
				<p align="left" style="font-size: 10pt; background-color: #DCEFF5;color: rgb(192, 91, 91);">
					<img src="images/001_53.png" alt="Usuário" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
					<s:text name="%{getText('label.pesquisaPacienteCodNome')}"/>
				</p>
			</div>				
			<div align="center" class="texto_vermelho_12px"> 
			  	<s:if test="hasFieldErrors() || hasActionErrors()">
				  	<img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
				  	<s:fielderror/>
				  	<s:actionerror/>
			  	</s:if>
			</div>
			<div>
		      		<s:textfield name="codPaciente" id="codPaciente" required="true" title="Código do Paciente" placeholder="Código do Paciente" cssStyle="width:250px" onkeypress="javascript:validarConteudo(event, 'numero');"/>
		      		<s:textfield name="nomePaciente" id="nomePaciente" title="Nome do Paciente" placeholder="Nome do Paciente" cssStyle="width:250px"/>
			</div>
			<table>
				<tr>
					<td>
			   			<s:submit action="listarPacientes" id="listarPacientesID" key="label.find" onclick="javascrit:validar();" cssClass="button" cssStyle="width:200px"/>
						<img id="indicator" src="images/indicator.gif" alt="Loading..." style="display:none"/>
					</td>
					<!--td>		
			   	   		<s:submit action="initListarPacientes" id="initListarPacientesID" key="label.cancel" cssClass="button" cssStyle="width:200px"/>
					</td-->		
					<td>
						
						<!-- Registrar usuario/colaborador -->
						<s:if test="%{user.cdPessoa==1}"> 
						<s:submit action="initRegisterUser" id="init" key="label.newUser" cssClass="button" cssStyle="width:200px"/>
						</s:if>
						
						<!-- Agenda Medica  -->
						<s:submit action="agendaMedica" id="init" key="label.agendaMedica" cssClass="button" cssStyle="width:200px"/>
												
						<!-- Logout do sistema -->
						<s:submit action="login" id="init" key="label.exit" cssClass="button" cssStyle="width:200px"/>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	
	<!-- lista de pacientes -->
	<div align="center">
		<div align="center">
			<s:if test="hasActionMessages()">
			<img src="images/001_11.png" width="20" height="20" border="0" style="background-color:transparent;"/>
			<s:actionmessage/>
			</s:if>
		</div>
		<br>
		<s:if test="%{!listPacientes.isEmpty()}">
			<div align="center" class="titulo_cinza_negrito_grande">
				<img src="images/001_18.png" alt="Usuário " width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
				<s:property value="listPacientes.size"/>
				<s:if test="%{listPacientes.size>1}">  <s:text name="%{getText('label.pacientesLocalizados')}"/></s:if>
				<s:else>  <s:text name="%{getText('label.pacienteLocalizado')}"/></s:else>				 
			</div>
			<br>
			<div class="table oxo scrollList">
				<article class="list">
				<ul>
					<s:iterator value="listPacientes" status="pacienteStatus">
						<li style="text-align: left">
							<s:url id="urlLaudosID" action="listarLaudos"/>
							<s:a href="%{urlLaudosID}?nroCadastroPaciente=%{cdPessoa}">
			                	${cdPessoa} - ${nomePessoa}
			                </s:a>			                
						</li>			
					</s:iterator>
				</ul>
				</article>
			</div>
		</s:if>
	</div>	
	
</body>
</html>
	<script type="text/javascript">
	
		var form = document.forms[0];
	
		function validar()
		{
			$("#indicator").show();
			var msg_ = 'Código do Paciente deve ser preenchido somente com Números.';
			var codPaciente_ = form.codPaciente.value;
			if(codPaciente_ != null && !validarNumero(codPaciente_))
		  	{
		  		form.codPaciente.value='';
		  		alert(msg_);
		  		return false;
		  	}
		}
	
	</script>	