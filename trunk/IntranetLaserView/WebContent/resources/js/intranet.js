//abrir laudo pdf em nova guia.
function openLaudo(nroCadastroPaciente, nrseqresultado, nrrequisicao)
{
	var _action = '/IntranetLaserView/servletReport?Text1=rtf&nroCadastroPaciente='+nroCadastroPaciente+'&nrseqresultado='+nrseqresultado+'&nrrequisicao='+nrrequisicao;
	//console.log(_action);
	window.open(_action,'laudoOnLine','resizable=yes,scrollbars=yes,menubar=no,width=600,height=700,toolbar=no').focus();
}

function openArquivoLocal(nomearquivo, codconvenio)
{
	var _action = '/IntranetLaserView/carregarArquivoLocal?Text1=local&nomearquivo='+nomearquivo+'&codconvenio='+codconvenio;
	//console.log(_action);
	window.open(_action,'laudoOnLine','resizable=yes,scrollbars=yes,menubar=no,width=600,height=700,toolbar=no').focus();
}
/*
  Função 
	validarConteudo

  Parâmetros
  	evento: Obrigatório, do tipo event. Recebe o valor de uma variável do navegador que guarda o evento do teclado.
	tipo: Obrigatório, do tipo String. Recebe o nome do tipo de validação que deve ser feita. Valores possíveis: numero, texto ou textoNumerico.

  Funcionalidade
	Não permite a entrada de caracteres inválidos.

  Exemplo de utilização
 	<input type="text" name="tfValor" size="11" maxlength="11" onKeyPress="return(validarConteudo(event, 'numero'))">

  Observação
	O retorno false não permite a entrada do caractere no respectivo textField.	
*/
function validarConteudo(evento, tipo)
{
	var entrada;
	var navegador = navigator.appName;

	if (navegador.indexOf("Netscape") != -1) {
		entrada = String.fromCharCode(evento.which);
		if (evento.which == 8 || evento.which == 9 || evento.which == 0) return true;
	} else {
		entrada = String.fromCharCode(evento.keyCode);
	}

	if(tipo == "numero") {
		return (validarNumero(entrada));
	} else {
		if( tipo == "float" ) {
			return (validarFloat(entrada));
		}
		if (tipo == "texto") {
			//return (validarTexto(entrada));
		} else  {			
			if (tipo == "textoNumerico") {
				//return (validarTextoNumerico(entrada));
			}
		}
		return;
	}
}


/*
  Função: validarNumero

  Parâmetros
  	conteudo: Obrigatório, do tipo String. Recebe o caracter correspondente ao evento do teclado.

  Funcionalidade
	Não permite a entrada de caracteres que não sejam numéricos.

  Exemplo de utilização
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
  Função 
	validarFloat

  Parâmetros
  	conteudo: Obrigatório, do tipo String. Recebe o caracter correspondente ao evento do teclado.

  Funcionalidade
	Não permite a entrada de caracteres que não sejam numéricos e permite a digitação do caracter '.' (ponto)

  Exemplo de utilização
	validarFloat(entrada)
*/
function validarFloat(conteudo)
{
	var validos = "0123456789.,-";
	var valor = new String(conteudo);

	for (var i = 0; i < valor.length; i++)
	{
		if (validos.indexOf(valor.charAt(i)) == -1)
			return false;
	}

	return true;
}

/*
Formata conteúdo digitado para data dd/mm/aaaa
Exemplo: 
<input type="text" name="tfDataVencimento" size="10" maxlength="10" onKeyUp=CalendarioFormataDigito(this)>
*/
function CalendarioFormataDigito(campo){
	campo.value = campo.value.replace(/[^0123456789\/]/g,'');
	vr = campo.value;
	tam = vr.length;
	if ( tam == 2 || tam == 5) campo.value = vr + '/'; 
}

/*
  Função 
	formatarData

  Parâmetros
  	componente: Obrigatório, do tipo textField (campo para entrada de caracteres). Recebe o componente textField que deve ter o seu conteúdo validado.

  Funcionalidade
	Valida o conteúdo do componente conforme formatação de data adotada.

  Exemplo de utilização
	<input type="text" name="tfDataVencimento" size="10" maxlength="10" onKeyUp=formatarData(this)>
	<html:text property="data" styleId="data" size="12" maxlength="10" styleClass="inputText" 
	onkeyup="javascript:formatarData(this); javascript:CalendarioFormataDigito(this);"
	onkeypress="return(validarConteudo(event, 'numero'));"/> 

  Observação
	Esta função é chamada sempre que o evento keyup é disparado.	
*/
function formatarData(componente)
{
	if (componente.value.length == 2) 
	{
		if (!testarDia(componente.value)) 
		{
			componente.value = "";
			componente.focus();
		} 
		else 
		{
			componente.value += '/';
		}
	}

	if (componente.value.length == 5) 
	{
		if (!testarMes(componente.value.substring(3,5), componente.value.substring(0,2))) 
		{ 
			componente.value = componente.value.substring(0,3);
			componente.focus();
		} 
		else 
		{
			componente.value += '/';
		}
	}

	if (componente.value.length == 10) 
	{
		if (!testarAno(componente.value.substring(6,10), componente.value.substring(3,5), componente.value.substring(0,2))) 
		{
			componente.value = componente.value.substring(0,5);
			componente.focus();
		}
	}
}

/*
  Função 
	formatarMesAno -> Ex Saida: '10/2010'

  Parâmetros
  	componente: Obrigatório, do tipo textField (campo para entrada de caracteres). Recebe o componente textField que deve ter o seu conteúdo validado.

  Funcionalidade
	Valida o conteúdo do componente conforme formatação de data somente com o mês e ano 'mm/yyyy'.

  Exemplo de utilização
	<input type="text" name="tfDataVencimento" size="10" maxlength="10" onKeyUp=formatarMesAno(this)>

  Observação
	Esta função é chamada sempre que o evento keyup é disparado.	
*/
function formatarMesAno(componente)
{
	if (componente.value.length == 2) 
	{
		if (!testarMes(componente.value, '01')) 
		{
			componente.value = "";
			componente.focus();
		} 
		else 
		{
			componente.value += '/';
		}
	}

	if (componente.value.length == 7) 
	{
		if (!testarAno(componente.value.substring(3,7), componente.value.substring(0,2), '01')) 
		{
			componente.value = componente.value.substring(0,2);
			componente.focus();
		}
	}
}

/*
  Função 
	testarDia

  Parâmetros
  	dia: Obrigatório, do tipo value (conteúdo de um TextField). Recebe o conteúdo que deve ser validado.

  Funcionalidade
	Verifica se o conteúdo passado como parâmetro está no formato dia.

  Exemplo de utilização
	testarDia(componente.value)

  Observação
	Um dia tem que estar entre 1 e 31.
*/
function testarDia(dia) 
{
	if (dia <= 0 || dia >= 32) 
	{
		alert("Dia não pode ter valor inferior a 01 e superir a 31 !");
		return false;
	}

	return true;
}


/*
  Função 
	testarMes

  Parâmetros
  	mes: Obrigatório, do tipo value (conteúdo de um TextField). Recebe o conteúdo que deve ser validado.
	dia: Obrigatório, do tipo value (conteúdo de um TextField). Recebe o conteúdo que deve ser validado.

  Funcionalidade
	Verifica se o conteúdo passado como parâmetro está no formato mês e se o dia informado é válido para o respectivo mês.

  Exemplo de utilização
	testarMes(componente.value.substring(3,5), componente.value.substring(0,2)) 

  Observação
	Um mês tem que estar entre 1 e 12.
*/
function testarMes(mes, dia) 
{
	if (mes < 1 || mes > 12) 
	{
		alert("Mês inválido.");
		return false;
	}

	if ((mes == 4 || mes == 6 || mes == 9 || mes == 11 ) && dia > 30) 
	{
		alert("Dia inválido para este mês !");
		return false;
	}

	if ((mes == 2) && dia > 29) 
	{
		alert("Dia inválido para este mês !");
		return false;
	}

	return true;
}

/*
  Função 
	testarAno

  Parâmetros
	ano: Obrigatório, do tipo value (conteúdo de um TextField). Recebe o conteúdo que deve ser validado.
	mes: Obrigatório, do tipo value (conteúdo de um TextField). Recebe o conteúdo que deve ser validado.
  	dia: Obrigatório, do tipo value (conteúdo de um TextField). Recebe o conteúdo que deve ser validado.

  Funcionalidade
	Verifica se o conteúdo passado como parâmetro está no formato data. Verifica também se toda a data é válida.

  Exemplo de utilização
	testarAno(componente.value.substring(6,10), componente.value.substring(3,5), componente.value.substring(0,2))

  Observação
	 O mês e o dia ('mes' e 'dia') devem estar de acordo com o ano ('ano') passado como parâmetro. Este teste é nescessário devido aos anos bissextos.
*/
function testarAno(ano, mes, dia) 
{
	if (mes == 2) 
	{
		if (dia == 29) 
		{
			if ((ano % 100) == 0)
			{
				if ((ano % 400) != 0) 
				{ 
					alert("O mês de fevereiro não possui dia 29 para este ano !");
					return false;
				}
			}
			else
			{
				if ((ano % 4) != 0) 
				{ 
					alert("O mês de fevereiro não possui dia 29 para este ano !");
					return false;
				}
			}
		}
	}
	
	return true;
}