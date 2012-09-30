<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<s:head/>
	<sx:head/>
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="resources/js/oap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><s:text name="application.title"/></title>
</head>

<body leftmargin="2">
<div align="center">
<s:form action="autenticarPaciente" method="post" validate="true">

	<table style="border-collapse:collapse;" align="center" cellpadding="1" cellspacing="1" border="0">
		<tr> 
		  	<td rowspan="2">
				<img src="images/logo/logo1.jpg" alt="Logomarca OAP" width="150" height="150" border="0" style="background-color:transparent;"/>
			</td>
		</tr>
		<tr>
			<td>
				<br> 
				<span class="texto_TNR_Azul_26px"><s:text name="business.title"/></span>					
			</td>
		</tr>
	</table>
	<table style="border-collapse:collapse;background-color: #EAF4FF;" width="400px" height="300px" align="center" cellpadding="1" cellspacing="1" border="0px">
		    <tr> 
		    	<td colspan="2" valign="top" align="center" bordercolor="#EAF4FF;">		    		
					<span class="texto_Azul_Negrito_20px">
					<img src="images/usuario.gif" alt="Segurança" style="background-color: transparent;" align="bottom"/>
					<s:text name="%{getText('label.autenticacaoLaudos')}"/>
					</span>
					<br>
				</td>
			</tr>
			<tr>				
				<td colspan="2"><span class="titulo_azul_negrito_grande"/></td>
			</tr>					
			<!--tr>
				<td colspan="2" style="font-family: sans-serif;color: blue;font-size: 10px;">
					<img src="images/linhaHorizPaisagem.gif" style="background-color: transparent;" align="bottom"/>
				</td>
			</tr-->
			<tr>
				<td colspan="2" class="texto_Azul_14px">
					<pre class="texto_Azul_14px">
						&bull; Utilize o <u>Número da Matrícula</u> e a <u>Data de Nascimento</u> do Titular 
						     para ter acesso aos serviços disponibilizados para resultados de exames.
							  					
						&bull; O campo <u>Número da Matrícula</u> deve ser somente números.
												
						&bull; A <u>Data de Nascimento</u> deve ser no formato dd/mm/aaaa (Ex.: 01/05/1989)
					</pre>
				</td>
			</tr>
			<!-- tr>
				<td colspan="2" style="font-family: sans-serif;color: blue;font-size: 10px;">
					<img src="images/linhaHorizPaisagem.gif" hspace="2px" background-color: transparent;" align="ceter"/>
				</td>
			</tr-->
			<tr>
				<td colspan="2">
					<!-- span class="titulo_azul_negrito_grande"> </span -->
				</td>
			</tr>	
			<tr>
				<td colspan="2">
					<s:textfield id="nroCadastroPacienteId" name="nroCadastroPaciente" label="Número da Matrícula" cssClass="inputText_16px"/>
					<sx:datetimepicker name="dataNascimento" label="Data de Nascimento" displayFormat="dd/MM/yyyy" cssClass="inputText_16px"/>
				</td>
			</tr>
			<tr><td colspan="2" align="left"><br></td></tr>
			
			<br>
			<div align="center">
				<s:submit value="Validar Dados" onclick="javascrit:validarAutenticacaoLaudoOnLine();" cssClass="button" cssStyle="width:200px" align="center"/>
			</div>	
	</table>
	<br/>
</s:form>
</div>
</body>
</html>
<script type="text/javascript">

	var form = document.forms[0];
	
	if(form.nroCadastroPacienteId != null)
	{
		form.nroCadastroPacienteId.focus();
	}
	
	function validarAutenticacaoLaudoOnLine()
	{
		var msg_ = 'Número da Matrícula é obrigatório e deve ser preenchido somente com Números.';
		var nroCadastroPaciente_ = form.nroCadastroPacienteId.value;
		if(nroCadastroPaciente_ != null && !validarNumero(nroCadastroPaciente_))
	  	{
	  		alert(msg_);
	  		setaText();
	  		return;
	  	}
	}

	/*
		Seta foco para o TestField nroCadastroPacient
	*/	
	function setaText()
	{
	  	form.nroCadastroPacienteId.value = '';
	  	form.nroCadastroPacienteId.focus();
	}	
</script>