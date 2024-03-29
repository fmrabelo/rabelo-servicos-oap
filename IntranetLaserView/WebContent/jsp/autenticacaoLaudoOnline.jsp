<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<s:head/>
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="resources/js/intranet.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><s:text name="application.title"/></title>
</head>

<body leftmargin="2">
<div align="center">
<s:form action="autenticarPaciente" method="post" validate="true">

	<table width="60%" height="30%" align="center" cellpadding="1" cellspacing="1" border="0">
		<tr> 
		  	<td>
				<img src="images/logo/header.jpg" alt="Logomarca"  border="0" style="background-color:transparent;"/>
			</td>
		</tr>
	</table>
	<table style="border-collapse:collapse;background-color: #EAF4FF;" width="70%" height="100%" align="center" cellpadding="1" cellspacing="1" border="0px">
		    <tr> 
		    	<td colspan="2" valign="top" align="center" bordercolor="#EAF4FF;">		    		
					<span class="texto_Azul_Negrito_20px">
						<img src="images/usuario.gif" alt="Seguran�a" style="background-color: transparent;" align="bottom"/>
						<s:text name="%{getText('label.autenticacaoLaudos')}"/>
					</span>
				</td>
			</tr>
			<tr>				
				<td colspan="2"><span class="titulo_azul_negrito_grande"/></td>
			</tr>
			
			<s:textfield id="nroCadastroPacienteId" name="nroCadastroPaciente" title="C�digo do Paciente" label="C�digo do Paciente" cssClass="inputText_16px"/>
			
			<s:textfield id="dataNascimentoId" name="dataNascimento" size="10" maxlength="10" title="Data de Nascimento" 
			label="Data de Nascimento" cssClass="inputText_16px" value="%{getText('{0,date,dd/MM/yyyy}',{dataNascimento})}"
			onkeyup="javascript:formatarData(this); javascript:CalendarioFormataDigito(this);" onkeypress="return(validarConteudo(event, 'numero'));"/>

			<div align="center">
				<s:submit value="Acessar" onclick="javascrit:validarAutenticacaoLaudoOnLine();" cssClass="button" cssStyle="width:200px" align="center"/>
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
		var msg_ = 'N�mero da Matr�cula � obrigat�rio e deve ser preenchido somente com N�meros.';
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