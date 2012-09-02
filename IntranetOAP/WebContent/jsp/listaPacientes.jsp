<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html>
<html>
<head>
	<s:head/>
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="resources/js/oap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="user-scalable=no, initial-scale = 1, minimum-scale = 1, maximum-scale = 1, width=device-width" />
	<title><s:text name="application.title"/></title>
</head>

<body>
	<div align="center">
		<s:form action="initListarPacientes" id="form" method="post" validate="false" cssStyle="margin-top: 3%;">
			<div class="widget titulo_branco_grande" align="center">
				<p>
					<s:text name="%{getText('label.findPatients')}"/>
				</p>
			</div>
	
			<div align="center" class="texto_vermelho_10px"> 
			  <s:fielderror/> 	 	
			  <s:actionerror/>
			</div>

			<div>
		      		<s:textfield name="codPaciente" id="codPaciente" required="true" placeholder="Código do Paciente" cssStyle="width:300px" onkeypress="javascript:validarConteudo(event, 'numero');"/>
		      		<s:textfield name="nomePaciente" id="nomePaciente" placeholder="Nome do Paciente" cssStyle="width:300px"/>
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
		<br>	
		<div align="center" class="titulo_cinza_negrito_grande">
		<s:actionmessage/>
		</div>
		<br>
		<s:if test="%{!listPacientes.isEmpty()}">
			<div align="center" class="titulo_cinza_negrito_grande">
				<s:property value="listPacientes.size"/><s:if test="%{listPacientes.size>1}">  Pacientes Localizados</s:if><s:else>  Paciente Localizado</s:else>				 
			</div>
			<br>
			<div class="table oxo scrollListOap">
				<ul>
					<s:iterator value="listPacientes" status="pacienteStatus">
						<li style="text-align: left">
							<a href="javascript:alert('${cdPessoa}');"> ${cdPessoa} - ${nomePessoa}</a>
						</li>			
					</s:iterator>
				</ul>
			</div>
		</s:if>
	</div>	
	
</body>
</html>
<script type="text/javascript">

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
	
</script>
