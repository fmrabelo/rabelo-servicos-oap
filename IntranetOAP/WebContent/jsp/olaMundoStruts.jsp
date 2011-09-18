<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<s:head/>
	<sx:head/>
	<link href="<s:url value="/resources/main.css"/>" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>OAP - Oftalmologistas Associados do Paran�</title>
</head>

<body leftmargin="2">

<s:form action="autenticarPaciente" method="post" validate="true">

	<table style="border-collapse:collapse;background-color: #D8D8D8;" width="800px" align="center" cellpadding="1" cellspacing="1">
		<tr>
			<td style="font-family: sans-serif;color: blue;font-size: 18px;">
				<h2><center><br>OAP - Oftalmologistas Associados do Paran�</center></h2>
			</td>
		</tr>
	</table>

	<table style="border-collapse:collapse;background-color: #D8D8D8;" width="400px" align="center" cellpadding="1" cellspacing="1">
		    <tr> 
		    	<td colspan="2" valign="top" align="center">
					<h1>Autentica��o para Acesso</h1><br><br>
				</td>
			</tr>
			<br>

			
			<tr>
				<td class="tdLabel" colspan="2" valign="top" align="left">
						<s:textfield name="nroCadastroPaciente" id="nroCadastroPacienteId" label="N�mero da Matr�cula"/>				
						
						<sx:datetimepicker name="dataNascimento" label="Data de Nascimento" displayFormat="dd/MM/yyyy"/>
				</td>
			</tr>
			<tr><td colspan="2" align="left"><br></td></tr>
			<tr>
				<td colspan="2" valign="top" align="left" style="font-family: sans-serif;color: blue;font-size: 14px;">
					<pre>
					- Utilize o <u>N�mero da Matr�cula</u> e a <u>Data de Nascimento</u> do Titular 
					  para ter acesso aos servi�os disponibilizados para resultados de exames.
					  					
					- O campo <u>N�mero da Matr�cula</u> deve ser somente n�meros.
										
					- A <u>Data de Nascimento</u> deve ser no formato dd/mm/aaaa (Exemplo: 01/05/1989)
					</pre>
				</td>
			</tr>
			
			<br>	
			<tr align="center">
				<br><br>
				<table width="200px" align="center" border="0" cellpadding="1" cellspacing="1">
					<tr>
						<br>	
						<td width="100%" align="left" style="text-align: center; border-bottom-color: buttonface;">
							<s:submit value="Validar Dados" onclick="javascrit:validar();"/>
						</td>
					</tr>
				</table>
			</tr>
	</table>
	<br/>
</s:form>

</body>
</html>
<script type="text/javascript">

	var form = document.forms[0];
	
	if(form.nroCadastroPacienteId != null)
	{
		form.nroCadastroPacienteId.focus();
	}
	
	function validar()
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
	  Fun��o: validarNumero
	
	  Par�metros
	  	conteudo: Obrigat�rio, do tipo String. Recebe o caracter correspondente ao evento do teclado.
	
	  Funcionalidade
		N�o permite a entrada de caracteres que n�o sejam num�ricos.
	
	  Exemplo de utiliza��o
		validarNumero(entrada)
	*/
	function validarNumero(conteudo)
	{
		var validos = "0123456789";
		var valor = new String(conteudo);
	
		for (var i = 0; i < valor.length; i++)
		{
			if (validos.indexOf(valor.charAt(i)) == -1)
				return false;
		}
		return true;
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