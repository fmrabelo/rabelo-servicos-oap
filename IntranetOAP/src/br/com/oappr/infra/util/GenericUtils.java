package br.com.oappr.infra.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.MaskFormatter;

import sun.misc.BASE64Encoder;

/**
 * Classe Generica para tratar configurações de localização, conversões e
 * formatações de valores numéricos.
 * @author Rabelo Serviços.
 */
@SuppressWarnings("unchecked")
public final class GenericUtils
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3064770053357626320L;

	private static final Locale[] locales = {new Locale("en", "US"), new Locale("de", "DE"),
	    new Locale("fr", "FR"), new Locale("pt", "BR")};

	/**
	 * @return
	 */
	public static final Locale getLocale ()
	{
		return locales[Constants.Default.LC_DEFAULT];
	}

	/**
	 * @param pattern
	 * @param value
	 * @param locale
	 * @return
	 */
	private static final synchronized String localizedFormat (String pattern, double value,
	    byte locale)
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance(locales[locale]);
			DecimalFormat df = (DecimalFormat)nf;
			df.applyPattern(pattern);
			nf = null;
			return df.format(value);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Retorna valor double com padrão 5 para escala decimal. Será considerada
	 * até a quinta casa decimal para aplicar fator de arredondamento.
	 * @param value
	 * @return
	 */
	public static double round (String value)
	{
		return round(value, 5);
	}

	/**
	 * Retorna valor double com padrão 5 para escala decimal. Será considerada
	 * até a quinta casa decimal para aplicar fator de arredondamento.
	 * @param value
	 * @return
	 */
	public static double round (double value)
	{
		return round(value, 5);
	}

	/**
	 * Retorna valor double com padrão especificado no parametro precision para
	 * o valor da escala decimal. Quanto maior o valor para a escala decimal,
	 * mais seguro e preciso será o valor retornado.
	 * @param value
	 * @param precision
	 * @return
	 */
	public static synchronized double round (String value, int precision)
	{
		if ((value == null) || (value.trim().length() <= 0))
		{
			return 0d;
		}
		try
		{
			double valor = new Double(value).doubleValue();
			return round(valor, precision);
		}
		catch (Exception ex)
		{
			return new Double(value).doubleValue();
		}
	}

	/**
	 * Retorna valor double com padrão especificado no parametro precision para
	 * o valor da escala decimal. Quanto maior o valor para a escala decimal,
	 * mais seguro e preciso será o valor retornado.
	 * @param value
	 * @param precision
	 * @return
	 */
	public static final synchronized double round (double value, int precision)
	{
		try
		{
			BigDecimal bigValue = new BigDecimal(value);
			BigDecimal result = bigValue.multiply(new BigDecimal("1"));
			result = result.setScale(precision, BigDecimal.ROUND_HALF_EVEN);
			return result.doubleValue();
		}
		catch (Exception ex)
		{
			return value;
		}
	}

	/**
	 * Retorna valor double com padrão 5 para escala decimal. Será considerada
	 * até a quinta casa decimal para aplicar fator de arredondamento.
	 * @param value
	 * @return
	 */
	public static double roundCeil (String value)
	{
		return roundCeil(value, 5);
	}

	/**
	 * Retorna valor double com precisão 5 para escala decimal. Será considerada
	 * até a quinta casa decimal para aplicar fator de arredondamento para cima
	 * ROUND_CEILING (teto).
	 * @param value
	 * @return
	 */
	public static double roundCeil (double value)
	{
		return roundCeil(value, 5);
	}

	/**
	 * Retorna valor double com a precisão especificada no parametro precision
	 * (ROUND_CEILING - teto ) para o valor da escala decimal. Quanto maior o
	 * valor para a escala decimal, mais seguro e preciso será o valor
	 * retornado.
	 * @param value
	 * @param precision
	 * @return double
	 */
	public static synchronized double roundCeil (String value, int precision)
	{
		if ((value == null) || (value.trim().length() <= 0))
		{
			return 0d;
		}
		try
		{
			return roundCeil(new Double(value).doubleValue(), precision);
		}
		catch (Exception ex)
		{
			return new Double(value).doubleValue();
		}
	}

	/**
	 * Retorna valor double com a precisão especificada no parametro precision
	 * (ROUND_CEILING - teto ) para o valor da escala decimal. Quanto maior o
	 * valor para a escala decimal, mais seguro e preciso será o valor
	 * retornado.
	 * @param value
	 * @param precision
	 * @return double
	 */
	public static final synchronized double roundCeil (double value, int precision)
	{
		try
		{
			// TODO: PROBLEMA DE ARREDONDAMENTO COM 2 CASAS DECIMAIS PARA
			// VALORES COMO: 0.125d, 0.135d, 0.145d,
			// BigDecimal bigValue = new BigDecimal(value);
			// BigDecimal result = bigValue.multiply(new BigDecimal("1"));
			// result = result.setScale(precision, BigDecimal.ROUND_CEILING);
			// return result.doubleValue();

			// positive value only.
			double power_of_ten = 1;
			double fudge_factor = 0.05;
			while (precision-- > 0)
			{
				power_of_ten *= 10.0d;
				fudge_factor /= 10.0d;
			}
			return Math.round((value + fudge_factor) * power_of_ten) / power_of_ten;
		}
		catch (Exception ex)
		{
			return value;
		}
	}

	/**
	 * Formata o valor double passado como parâmetro em valor monetário,
	 * obedecendo o numero de casas decimais do parametro casas.
	 * @param value, variavel double a ser formatada.
	 * @param casas, número de casas decimais a ser utilizado (0,1,2,3)
	 * @return
	 */
	public static final String formatValue (double value, int casas)
	{
		if (casas == 0)
		{
			return localizedFormat(Constants.Default.PATTERN_ZERO, value,
			    Constants.Locale.LOCALE_BR);
		}
		else if (casas == 1)
		{
			return localizedFormat(Constants.Default.PATTERN_ONE, value, Constants.Locale.LOCALE_BR);
		}
		else if (casas == 2)
		{
			return localizedFormat(Constants.Default.PATTERN_TWO, value, Constants.Locale.LOCALE_BR);
		}
		else if (casas == 3)
		{
			return localizedFormat(Constants.Default.PATTERN_THREE, value,
			    Constants.Locale.LOCALE_BR);
		}
		else if (casas == 4)
		{
			return localizedFormat(Constants.Default.PATTERN_FOUR, value,
			    Constants.Locale.LOCALE_BR);
		}
		else
		{
			return localizedFormat(Constants.Default.DEFAULT_PATTERN, value,
			    Constants.Locale.LOCALE_BR);
		}
	}

	/**
	 * Formata o valor double passado como parâmetro em valor monetário,
	 * obedecendo o numero de casas decimais do parametro casas. Retornando
	 * números negativos entre parenteses
	 * @param value, variavel double a ser formatada.
	 * @param casas, número de casas decimais a ser utilizado (0,1,2,3)
	 * @return
	 */
	public static final String formatValueNegativeAmount (double value, int casas)
	{
		if (casas == 0)
		{
			return localizedFormat(Constants.Default.PATTERN_ZERO_NEGATIVE_AMOUNT, value,
			    Constants.Locale.LOCALE_BR);
		}
		else if (casas == 1)
		{
			return localizedFormat(Constants.Default.PATTERN_ONE_NEGATIVE_AMOUNT, value,
			    Constants.Locale.LOCALE_BR);
		}
		else if (casas == 2)
		{
			return localizedFormat(Constants.Default.PATTERN_TWO_NEGATIVE_AMOUNT, value,
			    Constants.Locale.LOCALE_BR);
		}
		else if (casas == 3)
		{
			return localizedFormat(Constants.Default.PATTERN_THREE_NEGATIVE_AMOUNT, value,
			    Constants.Locale.LOCALE_BR);
		}
		else if (casas == 4)
		{
			return localizedFormat(Constants.Default.PATTERN_FOUR_NEGATIVE_AMOUNT, value,
			    Constants.Locale.LOCALE_BR);
		}
		else
		{
			return localizedFormat(Constants.Default.DEFAUT_PATTERN_NEGATIVE_AMOUNT, value,
			    Constants.Locale.LOCALE_BR);
		}
	}

	/**
	 * @param x
	 * @return
	 */
	public static final String getcpfformatado (String x)
	{
		if (x != null)
		{
			return new StringBuffer(x).insert(3, '.').insert(7, '.').insert(11, '-').toString();
		}
		return x;
	}

	public static final String removeApostrofe (String string)
	{
		if ((string != null) && (string.indexOf("'") > 0))
		{
			string = string.replaceAll("'", "''");
		}

		return string;
	}

	/**
	 * Método responsável por converter em maiúsculo e retirar acentuações e
	 * cedilha do conteúdo string a ser pesquisado na base de dados.
	 * @param arg
	 * @return String
	 */
	public final static synchronized String translateSql (String arg)
	{
		return " translate(upper(trim("
		    + arg
		    + ")),  'áÁàÀãÃâÂâäÄéÉêÊËëÈèíÍïÏÌìóÓôÔõÕöÖòÒúÚÙùúûüÜÛÇç','AAAAAAAAAAAEEEEEEEEIIIIIIOOOOOOOOOOUUUUUUUUUCC' ) ";
	}

	public final static synchronized String replaceValidUpperChar (String arg)
	{
		if (arg != null)
		{
			return arg.replace("á", "A").replace("Á", "A").replace("à", "A").replace("À", "A").replace(
			    "ã", "A").replace("Ã", "A").replace("â", "A").replace("Â", "A").replace("â", "A").replace(
			    "ä", "A").replace("Ä", "A").replace("é", "E").replace("É", "E").replace("ê", "E").replace(
			    "Ê", "E").replace("Ë", "E").replace("ë", "E").replace("È", "E").replace("è", "E").replace(
			    "í", "I").replace("Í", "I").replace("ï", "I").replace("Ï", "I").replace("Ì", "I").replace(
			    "ì", "I").replace("ó", "O").replace("Ó", "O").replace("ô", "O").replace("Ô", "O").replace(
			    "õ", "O").replace("Õ", "O").replace("ö", "O").replace("Ö", "O").replace("ò", "O").replace(
			    "Ò", "O").replace("ú", "U").replace("Ú", "U").replace("Ù", "U").replace("ù", "U").replace(
			    "ú", "U").replace("û", "U").replace("ü", "U").replace("Ü", "U").replace("Û", "U").replace(
			    "Ç", "C").replace("ç", "C");
		}
		return arg;
	}

	public static String MD5Print (byte[] text)
	{
		char[] hexOutput = new char[text.length * 2];
		String hexString;
		for (int i = 0; i < text.length; i++)
		{
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.toUpperCase().getChars(hexString.length() - 2, hexString.length(), hexOutput,
			    i * 2);
		}
		return new String(hexOutput);
	}

	/**
	 * Limpa valores nao numericos na string informada
	 * @param value
	 * @return
	 */
	public static final String limparNaoNumerico (String value)
	{
		StringBuilder numberValue = new StringBuilder();
		if ((value != null) && (value.trim().length() > 0))
		{
			final String numeros = "0123456789";
			value = value.trim();

			for (int i = 0; i < value.length(); i++)
			{
				String letra = String.valueOf(value.charAt(i));
				if (numeros.contains(letra))
				{
					numberValue.append(letra);
				}
			}
		}

		return numberValue.toString();
	}

	/**
	 * insere \ antes do apostrofo para ser interpretado pelo jsp e javascript
	 * na camada de apresentação.
	 * @param var
	 * @return
	 */
	public static final String scapeHtml (String var)
	{
		return (var != null) ? var.replace("'", "\\'") : null;
	}

	/**
	 * Método generico para formatação.
	 * @param texto
	 * @param mascara
	 * @return
	 * @throws ParseException <br>
	 *         <code>
	  MaskFormatter is used to format and edit strings. The behavior of a MaskFormatter is controlled by way of a String mask that specifies the valid characters that can be contained at a particular location in the Document model. The following characters can be specified:
	  Character
	  Description
	  
	 # 	Any valid number, uses Character.isDigit.
	 ' 	Escape character, used to escape any of the special formatting characters.
	 U	Any character (Character.isLetter). All lowercase letters are mapped to upper case.
	 L	Any character (Character.isLetter). All upper case letters are mapped to lower case.
	 A	Any character or number (Character.isLetter or Character.isDigit)
	 ?	Any character (Character.isLetter).
	 *	Anything.
	 H	Any hex character (0-9, a-f or A-F).
	  
	  </code>
	 */
	public final static String formatarString (String texto, String mascara)
	{
		if ((texto == null) || (texto.trim().length() == 0) || (mascara == null)
		    || (mascara.trim().length() == 0))
		{
			return null;
		}
		try
		{
			MaskFormatter mf = new MaskFormatter(mascara);
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(texto);
		}
		catch (ParseException pe)
		{
			return null;
		}
	}

	/**
	 * Método que recebe um parametro numerico como String e retorna um valor
	 * numerico Double.
	 * @param vlr
	 * @return
	 */
	public static final Double toDouble (String vlr)
	{
		if ((vlr != null) && (vlr.trim().length() > 0))
		{
			return new Double(vlr.trim());
		}
		return 0D;
	}

	/**
	 * Método que recebe um parametro numerico como Double e verifica se este é
	 * null, caso afirmativo, retorna 0 Double.
	 * @param vlr
	 * @return
	 */
	public static final Double toDouble (final Double vlr)
	{
		if (vlr == null)
		{
			return 0D;
		}
		return vlr;
	}

	/**
	 * Método que recebe um parametro numerico como String e retorna um valor
	 * numerico Long.
	 * @param vlr
	 * @return
	 */
	public static final Long toLong (String vlr)
	{
		if ((vlr != null) && (vlr.trim().length() > 0))
		{
			return new Long(vlr.trim());
		}
		return 0L;
	}

	/**
	 * Método que recebe um parametro numerico como Long e verifica se este é
	 * null, caso afirmativo, retorna 0 Long.
	 * @param vlr
	 * @return
	 */
	public static final Long toLong (final Long vlr)
	{
		if (vlr == null)
		{
			return 0L;
		}
		return vlr;
	}

	/**
	 * Método que recebe um parametro String e remove espaços vazios no final.
	 * @param vlr
	 * @return String
	 */
	public static final String toTrim (String vlr)
	{
		return (vlr != null) ? vlr.trim() : vlr;
	}

	/**
	 * Método que recebe um parametro String e se for null, retorna vazio.
	 * @param vlr
	 * @return
	 */
	public static final String nullToBlank (String vlr)
	{
		return (vlr == null) ? "" : vlr;
	}

	/**
	 * Método que recebe um parametro String e se for vazio, retorna null.
	 * @param vlr
	 * @return
	 */
	public static final String blankToNull (String vlr)
	{
		return ((vlr != null) && (vlr.trim().length() == 0)) ? null : vlr;
	}

	/**
	 * Caso o parametro vlr seja zero, retornar null.
	 * @param vlr
	 * @return
	 */
	public static final Double zeroToNull (final Double vlr)
	{
		return ((vlr != null) && (vlr.doubleValue() == 0)) ? null : vlr;
	}

	/**
	 * Caso o parametro vlr seja zero, retornar null.
	 * @param vlr
	 * @return
	 */
	public static final Long zeroToNull (final Long vlr)
	{
		return ((vlr != null) && (vlr.longValue() == 0)) ? null : vlr;
	}

	/**
	 * Método por verificar se um parametro esta com conteúdo setado para ON ou
	 * OFF. Útil para trabalhar com conteudos html.
	 * @param vlr
	 * @return boolean
	 */
	public static final boolean onOff (String vlr)
	{
		return (vlr != null) ? (Constants.ON.equalsIgnoreCase(vlr) ? true : false) : false;
	}

	/**
	 * @param text
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public static final String cryptMD5 (final String text)
	{
		try
		{
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("UTF-8"));
			return new BASE64Encoder().encode(md.digest());
		}
		catch (UnsupportedEncodingException e)
		{
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		return text;
	}

	/**
	 * @return
	 */
	public static final boolean verificarNomeExames (final String nomeExame,
	    final String[] arrayCompare)
	{
		if (!Validator.isBlankOrNull(nomeExame)
		    && ((arrayCompare != null) && (arrayCompare.length > 0)))
		{
			for (String z : arrayCompare)
			{
				if (nomeExame.toLowerCase().contains(z.toString().toLowerCase()))
				{
					return true;
				}
			}
		}
		return false;
	}

}
