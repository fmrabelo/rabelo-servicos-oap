//abrir laudo pdf em nova guia.
function openLaudo(nroCadastroPaciente, nrseqresultado, nrrequisicao)
{
	var _action = '/IntranetLaserView/servletReport?Text1=rtf&nroCadastroPaciente='+nroCadastroPaciente+'&nrseqresultado='+nrseqresultado+'&nrrequisicao='+nrrequisicao;
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