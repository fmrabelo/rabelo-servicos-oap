//abrir laudo pdf em nova guia.
function openLaudo(nroCadastroPaciente, nrseqresultado, nrrequisicao)
{
	var _action = '/IntranetLaserView/servletReport?Text1=rtf&nroCadastroPaciente='+nroCadastroPaciente+'&nrseqresultado='+nrseqresultado+'&nrrequisicao='+nrrequisicao;
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