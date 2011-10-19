package br.com.oappr.infra.util;

import java.util.Date;

import org.apache.commons.validator.GenericValidator;

/**
 * Classe para valida��o de formul�rio de dados.
 * @author desenvolvimento
 */
public final class Validator
{
	/**
	 * Verifica se a Data do tipo string � v�lida.
	 * @param value
	 * @return
	 */
	public static boolean isDate (String value)
	{
		return (!Validator.isBlankOrNull(value)) ? GenericValidator.isDate(value,
		    GenericUtils.getLocale()) : false;
	}

	/**
	 * Verifica se a Data do tipo Date � v�lida.
	 * @param value
	 * @return
	 */
	public static boolean isDate (Date value)
	{
		if (value == null)
		{
			return false;
		}
		return GenericValidator.isDate(DateUtils.formatDate(value), GenericUtils.getLocale());
	}

	/**
	 * Verifica se valor string � um n�mero long v�lido
	 * @param value
	 * @return
	 */
	public static boolean isLong (String value)
	{
		return (value == null) ? false : GenericValidator.isLong(value.trim());
	}

	/**
	 * Verifica se valor string � um n�mero int v�lido
	 * @param value
	 * @return
	 */
	public static boolean isInt (String value)
	{
		return (value == null) ? false : GenericValidator.isInt(value.trim());
	}

	/**
	 * Verifica se valor string � um n�mero short v�lido
	 * @param value
	 * @return
	 */
	public static boolean isShort (String value)
	{
		return (value == null) ? false : GenericValidator.isShort(value.trim());
	}

	/**
	 * Verifica se valor string � um n�mero int v�lido
	 * @param value
	 * @return
	 */
	public static boolean isByte (String value)
	{
		return (value == null) ? false : GenericValidator.isByte(value.trim());
	}

	/**
	 * Verifica se valor string � um n�mero float v�lido
	 * @param value
	 * @return
	 */
	public static boolean isFloat (String value)
	{
		return (value == null) ? false : GenericValidator.isFloat(value.trim());
	}

	/**
	 * Verifica se valor string � um n�mero double v�lido
	 * @param value
	 * @return
	 */
	public static boolean isDouble (String value)
	{
		return (value == null) ? false : GenericValidator.isDouble(value.trim());
	}

	/**
	 * Verifica se valor string � uma URL valida
	 * @param value
	 * @return
	 */
	public static boolean isUrl (String value)
	{
		return GenericValidator.isUrl(value);
	}

	/**
	 * Verifica se valor string � um email v�lido
	 * @param value
	 * @return
	 */
	public static boolean isEmail (String value)
	{
		return GenericValidator.isEmail(value);
	}

	/**
	 * Verifica se valor string � vazio ou nulo
	 * @param value
	 * @return
	 */
	public static boolean isBlankOrNull (String value)
	{
		return GenericValidator.isBlankOrNull(value);
	}

	/**
	 * Verifica se valor string � um cartao de credito v�lido
	 * @param value
	 * @return
	 */
	public static boolean isCreditCard (String value)
	{
		return GenericValidator.isCreditCard(value);
	}

	/**
	 * Valida intervalo espec�fico para o periodo entre duas datas; �til na
	 * valida��o das datas inicial e final para relat�rios
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static final String validaIntervalo (String dataInicial, String dataFinal, Byte tipo,
	    Integer intervalo)
	{
		String validaDatas = validaDataInicioFim(dataInicial, dataFinal);
		if (validaDatas != null)
		{
			return validaDatas;
		}
		else
		{
			try
			{
				return DateUtils.validaIntervalo(dataInicial, dataFinal, tipo, intervalo);
			}
			catch (Exception e)
			{
			}
		}
		return null;
	}

	/**
	 * Valida intervalo para o periodo entre duas datas; �til na valida��o das
	 * datas inicial e final.
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static final String validaIntervalo (String dataInicial, String dataFinal)
	{
		// valida o intervalo permitido entre a data inicial e final para o
		// relatorio.(atualmente o periodo maximo permitido � em meses e
		// definido no arquivo web.xml)
		return validaIntervalo(dataInicial, dataFinal, Constants.Date.MONTH, null);
	}

	/**
	 * Valida��o para data de in�cio e fim de per�odos.
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static String validaDataInicioFim (String dataInicial, String dataFinal)
	{
		if (isBlankOrNull(dataInicial) || isBlankOrNull(dataFinal))
		{
			return "As Datas Inicial e Final s�o obrigat�rias!!";
		}

		// validacao das datas se existirem.
		if (!isDate(dataInicial) || (dataInicial.length() < 10))
		{
			return "A Data Inicial deve ser uma data v�lida!!";
		}
		if (!isDate(dataFinal) || (dataFinal.length() < 10))
		{
			return "A Data Final deve ser uma data v�lida!!";
		}
		if (DateUtils.parseDate(dataInicial).compareTo(DateUtils.parseDate(dataFinal)) > 0)
		{
			return "Aten��o!!! A Data Inicial deve ser Menor que a Data Final!!";
		}
		return null;
	}

	/**
	 * Verifica se collection n�o � nula e vazia
	 * @param value
	 * @return
	 */
	public static boolean notEmptyCollection (java.util.Collection<?> collection)
	{
		return ((collection != null) && !collection.isEmpty());
	}

}
