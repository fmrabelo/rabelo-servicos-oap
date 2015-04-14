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
  Fun��o 
	validarConteudo

  Par�metros
  	evento: Obrigat�rio, do tipo event. Recebe o valor de uma vari�vel do navegador que guarda o evento do teclado.
	tipo: Obrigat�rio, do tipo String. Recebe o nome do tipo de valida��o que deve ser feita. Valores poss�veis: numero, texto ou textoNumerico.

  Funcionalidade
	N�o permite a entrada de caracteres inv�lidos.

  Exemplo de utiliza��o
 	<input type="text" name="tfValor" size="11" maxlength="11" onKeyPress="return(validarConteudo(event, 'numero'))">

  Observa��o
	O retorno false n�o permite a entrada do caractere no respectivo textField.	
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
  Fun��o 
	validarFloat

  Par�metros
  	conteudo: Obrigat�rio, do tipo String. Recebe o caracter correspondente ao evento do teclado.

  Funcionalidade
	N�o permite a entrada de caracteres que n�o sejam num�ricos e permite a digita��o do caracter '.' (ponto)

  Exemplo de utiliza��o
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
Formata conte�do digitado para data dd/mm/aaaa
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
  Fun��o 
	formatarData

  Par�metros
  	componente: Obrigat�rio, do tipo textField (campo para entrada de caracteres). Recebe o componente textField que deve ter o seu conte�do validado.

  Funcionalidade
	Valida o conte�do do componente conforme formata��o de data adotada.

  Exemplo de utiliza��o
	<input type="text" name="tfDataVencimento" size="10" maxlength="10" onKeyUp=formatarData(this)>
	<html:text property="data" styleId="data" size="12" maxlength="10" styleClass="inputText" 
	onkeyup="javascript:formatarData(this); javascript:CalendarioFormataDigito(this);"
	onkeypress="return(validarConteudo(event, 'numero'));"/> 

  Observa��o
	Esta fun��o � chamada sempre que o evento keyup � disparado.	
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
  Fun��o 
	formatarMesAno -> Ex Saida: '10/2010'

  Par�metros
  	componente: Obrigat�rio, do tipo textField (campo para entrada de caracteres). Recebe o componente textField que deve ter o seu conte�do validado.

  Funcionalidade
	Valida o conte�do do componente conforme formata��o de data somente com o m�s e ano 'mm/yyyy'.

  Exemplo de utiliza��o
	<input type="text" name="tfDataVencimento" size="10" maxlength="10" onKeyUp=formatarMesAno(this)>

  Observa��o
	Esta fun��o � chamada sempre que o evento keyup � disparado.	
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
  Fun��o 
	testarDia

  Par�metros
  	dia: Obrigat�rio, do tipo value (conte�do de um TextField). Recebe o conte�do que deve ser validado.

  Funcionalidade
	Verifica se o conte�do passado como par�metro est� no formato dia.

  Exemplo de utiliza��o
	testarDia(componente.value)

  Observa��o
	Um dia tem que estar entre 1 e 31.
*/
function testarDia(dia) 
{
	if (dia <= 0 || dia >= 32) 
	{
		alert("Dia n�o pode ter valor inferior a 01 e superir a 31 !");
		return false;
	}

	return true;
}


/*
  Fun��o 
	testarMes

  Par�metros
  	mes: Obrigat�rio, do tipo value (conte�do de um TextField). Recebe o conte�do que deve ser validado.
	dia: Obrigat�rio, do tipo value (conte�do de um TextField). Recebe o conte�do que deve ser validado.

  Funcionalidade
	Verifica se o conte�do passado como par�metro est� no formato m�s e se o dia informado � v�lido para o respectivo m�s.

  Exemplo de utiliza��o
	testarMes(componente.value.substring(3,5), componente.value.substring(0,2)) 

  Observa��o
	Um m�s tem que estar entre 1 e 12.
*/
function testarMes(mes, dia) 
{
	if (mes < 1 || mes > 12) 
	{
		alert("M�s inv�lido.");
		return false;
	}

	if ((mes == 4 || mes == 6 || mes == 9 || mes == 11 ) && dia > 30) 
	{
		alert("Dia inv�lido para este m�s !");
		return false;
	}

	if ((mes == 2) && dia > 29) 
	{
		alert("Dia inv�lido para este m�s !");
		return false;
	}

	return true;
}

/*
  Fun��o 
	testarAno

  Par�metros
	ano: Obrigat�rio, do tipo value (conte�do de um TextField). Recebe o conte�do que deve ser validado.
	mes: Obrigat�rio, do tipo value (conte�do de um TextField). Recebe o conte�do que deve ser validado.
  	dia: Obrigat�rio, do tipo value (conte�do de um TextField). Recebe o conte�do que deve ser validado.

  Funcionalidade
	Verifica se o conte�do passado como par�metro est� no formato data. Verifica tamb�m se toda a data � v�lida.

  Exemplo de utiliza��o
	testarAno(componente.value.substring(6,10), componente.value.substring(3,5), componente.value.substring(0,2))

  Observa��o
	 O m�s e o dia ('mes' e 'dia') devem estar de acordo com o ano ('ano') passado como par�metro. Este teste � nescess�rio devido aos anos bissextos.
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
					alert("O m�s de fevereiro n�o possui dia 29 para este ano !");
					return false;
				}
			}
			else
			{
				if ((ano % 4) != 0) 
				{ 
					alert("O m�s de fevereiro n�o possui dia 29 para este ano !");
					return false;
				}
			}
		}
	}
	
	return true;
}