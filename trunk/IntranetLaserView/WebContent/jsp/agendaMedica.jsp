<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>

<!DOCTYPE html>
<html>
<head>
	<s:head/>
	<sx:head/>
	
	<link href="<s:url value="/resources/css/agenda.css"/>" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="resources/js/oap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="user-scalable=no, initial-scale = 1, minimum-scale = 1, maximum-scale = 1, width=device-width" />
	<title><s:text name="application.title"/></title>
</head>

<body>
	<div align="center">
	<s:form action="" method="post" id="formAgenda">
		<div class="widget titulo_branco_grande" align="center">
			<p>
				<img src="images/001_44.png" alt="Logomarca OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;
				<s:text name="%{getText('label.agendaMedicaTitle')}"/>
			</p>
		</div>
		<br>
		<div class="titulo_cinza_negrito_grande" align="center">
				<p align="center" style="font-size: 15pt; color:#050607;">
					<img src="images/001_54.png" alt="Usuário OAP" width="20" height="20" border="0" style="background-color:transparent;"/>&nbsp;&nbsp;					
					<s:property value="userSessionOAP.nomePessoa"/>
				</p>
		</div>		
		<!--div style="padding: 20px;"><br><s:date name="dataAgenda" format="dd/MMM/yyyy"/></div-->
		<table>
			<tr>
				<td style="padding-right: 100px; padding-top: 45px;">
					<s:submit action="diaAnterior" id="ant" src="images/001_60_left.png" title="Dia Anterior" type="image" cssStyle="left: 540px; position: absolute; height: 25px; width:25px; border:0px; background-image: initial;"/>
				</td>
				<td style="padding-right: 100px; padding-left:  80px;">
					<s:submit action="proximoDia" id="post" src="images/001_60.png" title="Proximo Dia" type="image" cssStyle="left: 840px; position: absolute; height: 25px; width:25px; border:0px; background-image: initial;"/>
				</td>
				<td>
					<s:submit action="listarAgenda" id="listarAgenda" src="images/001_39.png" type="image" cssStyle="left: 745px; position: absolute; height: 23px; width:23px; border:0px; background-image: initial;"/>
				</td>
				<td>
		   			<sx:datetimepicker name="dataAgenda" displayFormat="dd-MMM-yyyy" cssStyle="height: 10px;width: 80px;"/>
				</td>
			</tr>
		</table>
					   			

		<!-- lista agenda -->

		<br>	
		<div align="center" class="texto_vermelho_12px"> 
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

		<div align="center" style="line-height: 25px;">
			<br>
			<s:elseif test="%{!listaAgenda.isEmpty()}">
				<display:table  id="employeeList" name="listaAgenda" requestURI="/viewEmployeeAction" export="false" pagesize="0">
					<display:column property="hragenda" style="font-size: 9pt;" title="Hora" sortable="false"/>
					<display:column property="cdpessoa" style="font-size: 9pt;" title="Nº" sortable="false" />
					<display:column property="nmpessoa" maxLength="30" style="font-size: 9pt;" title="Nome" sortable="false" />
					<display:column property="dsconvenio" maxLength="22" style="font-size: 9pt;" title="Convenio"  sortable="false" />
					<display:column property="dstelefone" maxLength="22" style="font-size: 9pt;" title="Fone"  sortable="false"/>
					<display:column property="dsosbagenda" maxLength="22" style="font-size: 9pt;" title="Obs."  sortable="false"/>
					<display:setProperty name="paging.banner.placement" value="bottom"/>
				</display:table>
			</s:elseif>
			<s:else>
			</s:else>
		</div>		
		<br>
		<s:submit action="initListarPacientes" key="label.back" align="center" cssClass="button" cssStyle="width:200px"/>
		
	</s:form>
	</div>

</body>
</html>
<script type="text/javascript">

	var form = document.forms.formAgenda;

	function upateAgenda()
	{
		alert( form.dataAgenda.value );
		form.action='listarAgenda?dataAgendaStr='+form.dataAgenda.value;
		form.submit();
	}

</script>