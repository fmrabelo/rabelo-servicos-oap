<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
	<s:head/>
	<link href="<s:url value="/resources/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="resources/js/oap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="user-scalable=no, initial-scale = 1, minimum-scale = 1, maximum-scale = 1, width=device-width" />
	<title><s:text name="application.title"/></title>
</head>

<body>
	<div align="center">
	<s:form action="initRegisterUser" id="form" method="post" validate="true" cssStyle="margin-top: 3%;">
		<div class="widget titulo_branco_grande" align="center">
			<p> Área Restrita para Usuários OAP *********** <s:text name="%{getText('label.signUp')}"/></p>
		</div>
			
		<div>
			<div align="center" class="texto_vermelho_10px"> 
			  <p>
			  <s:fielderror/> 	 	
			  <s:actionerror/>
			  </p>
			</div>
			<div align="center" style="font:bold;color:blue;">
			   <s:actionmessage/>		 
			</div>	
	      	<p>
	      		<s:textfield name="user.nrusuario" id="nrusuario" required="true" placeholder="Número do Usuário OAP" cssStyle="width:300px" onkeypress="javascript:validarConteudo(event, 'numero');"/>
	      	</P>
	      	<p>
	      		<s:password name="user.senhaweb" id="senhaweb" placeholder="Senha"  cssStyle="width:300px"/>
	      	</P>
		</div>
		<table>
			<tr>
				<td>
		   			<s:submit action="autenticarColaboradorOap" id="login" key="label.send" onclick="javascrit:validar();" cssClass="button" cssStyle="width:200px"/>
				</td>
				<td>		
		   	   		<s:submit action="loginOap" id="init" key="label.cancel" cssClass="button" cssStyle="width:200px"/>
				</td>		
			</tr>
		</table>
	</s:form>
	</div>
</body>
</html>
<script type="text/javascript">

	var form = document.forms[0];

	function validar()
	{
		var msg_ = 'Número do Usuário é obrigatório e deve ser preenchido somente com Números.';
		var nroCadastroPaciente_ = form.nrusuario.value;
		if(nroCadastroPaciente_ != null && !validarNumero(nroCadastroPaciente_))
	  	{
	  		alert(msg_);
	  		form.nrusuario.value='';
	  		return false;
	  	}
	}
	
</script>