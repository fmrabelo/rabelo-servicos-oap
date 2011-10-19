package br.com.oappr.infra.util;

import java.util.Date;

import org.apache.commons.validator.GenericValidator;

/**
 * Classe para validação de formulário de dados.
 * @author desenvolvimento
 */
public final class Validator
{
	/**
	 * Verifica se a Data do tipo string é válida.
	 * @param value
	 * @return
	 */
	public static boolean isDate (String value)
	{
		return (!Validator.isBlankOrNull(value)) ? GenericValidator.isDate(value,
		    GenericUtils.getLocale()) : false;
	}

	/**
	 * Verifica se a Data do tipo Date é válida.
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
	 * Verifica se valor string é um número long válido
	 * @param value
	 * @return
	 */
	public static boolean isLong (String value)
	{
		return (value == null) ? false : GenericValidator.isLong(value.trim());
	}

	/**
	 * Verifica se valor string é um número int válido
	 * @param value
	 * @return
	 */
	public static boolean isInt (String value)
	{
		return (value == null) ? false : GenericValidator.isInt(value.trim());
	}

	/**
	 * Verifica se valor string é um número short válido
	 * @param value
	 * @return
	 */
	public static boolean isShort (String value)
	{
		return (value == null) ? false : GenericValidator.isShort(value.trim());
	}

	/**
	 * Verifica se valor string é um número int válido
	 * @param value
	 * @return
	 */
	public static boolean isByte (String value)
	{
		return (value == null) ? false : GenericValidator.isByte(value.trim());
	}

	/**
	 * Verifica se valor string é um número float válido
	 * @param value
	 * @return
	 */
	public static boolean isFloat (String value)
	{
		return (value == null) ? false : GenericValidator.isFloat(value.trim());
	}

	/**
	 * Verifica se valor string é um número double válido
	 * @param value
	 * @return
	 */
	public static boolean isDouble (String value)
	{
		return (value == null) ? false : GenericValidator.isDouble(value.trim());
	}

	/**
	 * Verifica se valor string é uma URL valida
	 * @param value
	 * @return
	 */
	public static boolean isUrl (String value)
	{
		return GenericValidator.isUrl(value);
	}

	/**
	 * Verifica se valor string é um email válido
	 * @param value
	 * @return
	 */
	public static boolean isEmail (String value)
	{
		return GenericValidator.isEmail(value);
	}

	/**
	 * Verifica se valor string é vazio ou nulo
	 * @param value
	 * @return
	 */
	public static boolean isBlankOrNull (String value)
	{
		return GenericValidator.isBlankOrNull(value);
	}

	/**
	 * Verifica se valor string é um cartao de credito válido
	 * @param value
	 * @return
	 */
	public static boolean isCreditCard (String value)
	{
		return GenericValidator.isCreditCard(value);
	}

	/**
	 * Valida intervalo específico para o periodo entre duas datas; útil na
	 * validação das datas inicial e final para relatórios
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
	 * Valida intervalo para o periodo entre duas datas; útil na validação das
	 * datas inicial e final.
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static final String validaIntervalo (String dataInicial, String dataFinal)
	{
		// valida o intervalo permitido entre a data inicial e final para o
		// relatorio.(atualmente o periodo maximo permitido é em meses e
		// definido no arquivo web.xml)
		return validaIntervalo(dataInicial, dataFinal, Constants.Date.MONTH, null);
	}

	/**
	 * Validação para data de início e fim de períodos.
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static String validaDataInicioFim (String dataInicial, String dataFinal)
	{
		if (isBlankOrNull(dataInicial) || isBlankOrNull(dataFinal))
		{
			return "As Datas Inicial e Final são obrigatórias!!";
		}

		// validacao das datas se existirem.
		if (!isDate(dataInicial) || (dataInicial.length() < 10))
		{
			return "A Data Inicial deve ser uma data válida!!";
		}
		if (!isDate(dataFinal) || (dataFinal.length() < 10))
		{
			return "A Data Final deve ser uma data válida!!";
		}
		if (DateUtils.parseDate(dataInicial).compareTo(DateUtils.parseDate(dataFinal)) > 0)
		{
			return "Atenção!!! A Data Inicial deve ser Menor que a Data Final!!";
		}
		return null;
	}

	/**
	 * Verifica se collection não é nula e vazia
	 * @param value
	 * @return
	 */
	public static boolean notEmptyCollection (java.util.Collection<?> collection)
	{
		return ((collection != null) && !collection.isEmpty());
	}

}
