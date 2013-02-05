package br.com.oappr.infra.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.naming.InitialContext;

/**
 * Classe utilitaria para tratamento de datas e horarios.
 * @author Rabelo Serviços.
 */
public final class DateUtils
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6958284973082580091L;
	protected static final TimeZone TIME_ZONE = TimeZone.getTimeZone(Constants.Default.timeZone);
	protected static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
	    Constants.Default.formatDate);
	protected static final SimpleDateFormat simpleTimeFormatHHMMSS = new SimpleDateFormat(
	    Constants.Default.formatTimeHHMMSS);
	protected static final SimpleDateFormat simpleTimeFormatHHMM = new SimpleDateFormat(
	    Constants.Default.formatTimeHHMM);
	protected static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
	    Constants.Default.formatDateTime);
	protected static final SimpleDateFormat simpleDateExtensoBr = new SimpleDateFormat(
	    Constants.Default.formatDateExtensoBR, GenericUtils.getLocale());

	public DateUtils ()
	{
	}

	/**
	 * Retorna um Calendar com TIME_ZONE configurado.
	 * @param data java.util.Date
	 * @return Calendar
	 */
	private static Calendar getCalendar (java.util.Date data)
	{
		Calendar calendar = Calendar.getInstance(GenericUtils.getLocale());
		if (data != null)
		{
			calendar.setTime(data);
		}
		return calendar;
	}

	public static java.util.Date getCurrentDate ()
	{
		TimeZone.setDefault(TIME_ZONE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getDefault());
		return calendar.getTime();
	}

	public static java.sql.Date getCurrentSqlDate ()
	{
		return parseSqlDate(getCurrentDate());
	}

	public static java.sql.Timestamp getCurrentTimestamp ()
	{
		return parseTimestamp(getCurrentDate());
	}

	/**
	 * Adiciona ou subtrai uma quantidade de dias passado pelo paramentro
	 * nroDias ao objeto java.util.Date data, também passado no parametro. Para
	 * adicionar, basta passar um nroDias positivo e para diminuir, negativo.
	 * @param data
	 * @param nroDias
	 * @return
	 */
	public static java.util.Date addDiasDate (java.util.Date data, int nroDias)
	{
		return addAmountDate(data, nroDias, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Adiciona ou subtrai uma quantidade de meses, anos, etc passado pelo
	 * paramentro amount ao objeto java.util.Date dateTime, também passado no
	 * parametro. Para adicionar, basta passar um parametro amount positivo e
	 * para diminuir, negativo.
	 * @param dateTime
	 * @param amount
	 * @param field
	 * @return
	 */
	public static java.util.Date addAmountDate (java.util.Date dateTime, int amount, int field)
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeZone(TIME_ZONE);
		calendar.setTime(dateTime);
		calendar.add(field, amount);

		java.util.Date newDate = new java.util.Date();
		newDate.setTime(calendar.getTime().getTime());

		return newDate;
	}

	public static String formatDateExtenso (java.util.Date dateTime)
	{
		if (dateTime == null)
		{
			return null;
		}
		else
		{
			// retira horas da data
		}
		simpleDateExtensoBr.setTimeZone(TIME_ZONE);
		return simpleDateExtensoBr.format(dateTime);
	}

	public static String formatDateDDMMYYYY (java.util.Date dateTime)
	{
		if (dateTime != null)
		{
			simpleDateFormat.setTimeZone(TIME_ZONE);
			return simpleDateFormat.format(dateTime);
		}
		return null;
	}

	public static java.util.Date truncDate (java.util.Date dateTime)
	{
		if (dateTime != null)
		{

			simpleDateFormat.setTimeZone(TIME_ZONE);
			return parseDate(simpleDateFormat.format(dateTime));
		}
		return null;
	}

	public static java.util.Date truncDate (java.sql.Date sqlDateTime)
	{
		return truncDate(parseDate(sqlDateTime));
	}

	public static java.util.Date truncDate (java.sql.Timestamp timestamp)
	{
		return truncDate(parseDate(timestamp));
	}

	public static final Long getYear ()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeZone(TIME_ZONE);

		return new Long(calendar.get(Calendar.YEAR));
	}

	public static final Long getMonth ()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeZone(TIME_ZONE);

		return new Long(calendar.get(Calendar.MONTH));
	}

	/**
	 * Retorno o primeiro dia do Mês corrente.
	 * @return
	 */
	public static final String firstDayMonth ()
	{
		Long month = (DateUtils.getMonth() + 1);
		return "01/" + (month < 10 ? ("0" + month) : month) + "/" + DateUtils.getYear();
	}

	public static String formatTimeHHMMSS (java.util.Date time)
	{
		String timeFormat = null;

		if (time != null)
		{
			simpleTimeFormatHHMMSS.setTimeZone(TIME_ZONE);

			timeFormat = simpleTimeFormatHHMMSS.format(time);
		}
		return timeFormat;
	}

	public static String formatTimeHHMM (java.util.Date time)
	{
		String timeFormat = null;

		if (time != null)
		{
			simpleTimeFormatHHMM.setTimeZone(TIME_ZONE);

			timeFormat = simpleTimeFormatHHMM.format(time);
		}
		return timeFormat;
	}

	public static String formatDate (java.util.Date dateTime)
	{
		String dateTimeFormat = null;
		try
		{
			if (dateTime != null)
			{

				simpleDateTimeFormat.setTimeZone(TIME_ZONE);
				dateTimeFormat = simpleDateTimeFormat.format(dateTime);

				if (dateTimeFormat.substring(11, 19).equalsIgnoreCase("00:00:00"))
				{
					return dateTimeFormat.substring(0, 10);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return dateTimeFormat;
	}

	public static String formatDate (java.sql.Date sqlDateTime)
	{
		return formatDate(parseDate(sqlDateTime));
	}

	public static String formatDate (java.sql.Timestamp timestamp)
	{
		return formatDate(parseDate(timestamp));
	}

	public static java.util.Date parseDate (java.sql.Timestamp timestamp)
	{
		if (timestamp == null)
		{
			return null;
		}

		return new java.util.Date(timestamp.getTime());
	}

	public static java.util.Date parseDate (java.sql.Date sqlDateTime)
	{
		if (sqlDateTime == null)
		{
			return null;
		}
		return new java.util.Date(sqlDateTime.getTime());
	}

	public static java.util.Date parseDate (String date)
	{
		if ((date == null) || (date.trim().length() == 0))
		{
			return null;
		}
		java.util.Date dt = null;
		try
		{
			if (!date.contains("/") && (date.length() == 8))
			{
				// formata data de entada (ddmmyyyy) e retorna data string de
				// saída
				// no formato dd/mm/yyyy
				date = date.substring(0, 2).concat("/").concat(date.substring(2, 4).concat("/")).concat(
				    date.substring(4, 8));
			}
			else if (date.contains("/")
			    && ((date.length() + 2) == Constants.Default.formatDate.length()))
			{
				// formata data de entada (dd/mm/yy) e retorna data string de
				// saída
				// no formato dd/mm/yyyy
				String year = date.substring(6, 8);
				if (Long.valueOf(year).intValue() >= 50)
				{
					year = "19".concat(year);
				}
				else
				{
					year = "20".concat(year);
				}
				date = date.substring(0, 6).concat(year);
			}

			if (date.length() == Constants.Default.formatDate.length())
			{
				simpleDateFormat.setTimeZone(TIME_ZONE);
				dt = simpleDateFormat.parse(date);
			}
			else if (date.length() == Constants.Default.formatDateTime.length())
			{
				simpleDateTimeFormat.setTimeZone(TIME_ZONE);
				dt = simpleDateTimeFormat.parse(date);
			}
			else if (date.length() > Constants.Default.formatDateTime.length())
			{
				try
				{
					simpleDateTimeFormat.setTimeZone(TIME_ZONE);
					dt = simpleDateTimeFormat.parse(date);
				}
				catch (Exception e)
				{
				}
			}
			return dt;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return dt;
	}

	public static java.sql.Timestamp parseTimestamp (String dateTime)
	{
		return parseTimestamp(parseDate(dateTime));
	}

	public static java.sql.Timestamp parseTimestamp (java.util.Date dateTime)
	{
		if (dateTime == null)
		{
			return null;
		}

		return new java.sql.Timestamp(dateTime.getTime());
	}

	public static java.sql.Date parseSqlDate (java.util.Date date)
	{
		if (date == null)
		{
			return null;
		}

		return new java.sql.Date(date.getTime());
	}

	public static java.sql.Date parseSqlDate (String date)
	{
		return parseSqlDate(parseDate(date));
	}

	public static java.util.Date parseTime (String time)
	{
		java.util.Date dt = null;
		try
		{
			if ((time.length() + 3) == Constants.Default.formatTimeHHMMSS.length())
			{
				time += ":00";
			}
			if (time.length() != Constants.Default.formatTimeHHMMSS.length())
			{
				return null;
			}
			simpleTimeFormatHHMMSS.setTimeZone(TIME_ZONE);
			dt = simpleTimeFormatHHMMSS.parse(time);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return dt;
	}

	public static final String isWeekend (java.util.Date date)
	{
		String day = null;
		try
		{
			if (date == null)
			{
				date = getCurrentDate();
			}

			Calendar calendar = Calendar.getInstance();

			calendar.setTimeZone(TIME_ZONE);
			calendar.setTime(date);

			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				day = "Domingo";
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			{
				day = "Sábado";
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return day;
	}

	/**
	 * <br>
	 * Método que valida o intervalo permitido entre as duas datas. <br>
	 * O valor default é definido pelo parametro (maxInterval) em meses. <br>
	 * Este valor é configurável através da variavel de ambiente
	 * <i>dataMaxIntervalo</i> no arquivo <i>web.xml</i>.
	 */
	public static final String validaIntervalo (String dataIni, String dataFim)
	{
		return validaIntervalo(dataIni, dataFim, Constants.Date.MONTH, null);
	}

	/**
	 * <br>
	 * Método que valida o intervalo permitido entre as duas datas. <br>
	 * O valor default é definido pelo parametro (maxInterval) em meses. <br>
	 * Este valor é configurável através da variavel de ambiente
	 * <i>dataMaxIntervalo</i> no arquivo <i>web.xml</i>. <br>
	 * Outras opções de validação de intervalo como <i>Dias</i> e <i>Anos</i>
	 * podem ser feitas passando o tipo desejado através do parametro
	 * <i>typeInterval</i> e a quantidade do intervalo permitido no parametro
	 * <i>interval</i>
	 */
	public static final String validaIntervalo (String dataIni, String dataFim, byte typeInterval,
	    Integer interval)
	{
		try
		{
			if ((dataIni != null) && (dataFim != null)
			    && (dataIni.length() == Constants.Default.formatDate.length())
			    && (dataFim.length() == Constants.Default.formatDate.length()))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeZone(TIME_ZONE);

				simpleDateFormat.setTimeZone(TIME_ZONE);
				calendar.setTime(parseDate(dataIni));
				String msg = "O intervalo máximo permitido entre as datas deste relatório é de ";

				switch (typeInterval)
				{
				case Constants.Date.DATE:
				{
					// adiciona o numero de dias a data de entrada.
					calendar.add(Calendar.DATE, interval.intValue());

					if (calendar.getTime().compareTo(parseDate(dataFim)) < 0)
					{
						return msg + interval.intValue() + " dias!";
					}
					break;
				}
				case Constants.Date.YEAR:
				{
					// adiciona o numero de anos a data de entrada.
					calendar.add(Calendar.YEAR, interval.intValue());
					if (calendar.getTime().compareTo(parseDate(dataFim)) < 0)
					{
						return msg + interval.intValue() + " anos!";
					}
					break;
				}
				default:
				{
					InitialContext context = new InitialContext();
					Integer intervaloMax = (Integer)context.lookup("java:comp/env/maxInterval");
					context = null;
					// se o paramentro intervalo não for nulo, este será usado
					// no lugar do default.
					if (interval != null)
					{
						intervaloMax = interval;
					}
					// adiciona o numero de meses a data de entrada.
					calendar.add(Calendar.MONTH, intervaloMax.intValue());
					if (calendar.getTime().compareTo(parseDate(dataFim)) < 0)
					{
						return msg + intervaloMax.intValue() + " meses!";
					}
					break;
				}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Método que retorna o número de dias entre duas datas passadas como
	 * parametro.
	 * @param initDate
	 * @param endDate
	 * @return int
	 */
	public static int getNumberDays (java.util.Date initDate, java.util.Date endDate)
	{
		if ((initDate == null) || (endDate == null))
		{
			return 0;
		}
		Calendar c1 = getCalendar(initDate);
		Calendar c2 = getCalendar(endDate);
		long milis = (c2.getTimeInMillis() - c1.getTimeInMillis());
		c1 = c2 = null;
		milis += 3600000L;// 1 hora para compensar horário de verão
		return ((int)(milis / 86400000));// milissegundos por dia
	}

	/**
	 * Método que retorna String com a diferença de dias/horas/minutos entre
	 * duas datas passadas como parametro.
	 * @param initDate
	 * @param endDate
	 * @return String
	 */
	public static final String getNumberDaysHours (java.util.Date initDate, java.util.Date endDate)
	{
		if ((initDate == null) || (endDate == null))
		{
			return null;
		}
		Calendar c1 = getCalendar(initDate);
		Calendar c2 = getCalendar(endDate);
		long milis = (c2.getTimeInMillis() - c1.getTimeInMillis());

		StringBuffer dif = new StringBuffer();

		if ((milis / 86400000) > 0)
		{
			dif.append(String.valueOf(milis / 86400000));
			dif.append(" dia(s) ");
		}
		if (((milis / 1000 / 60 / 60) % 24) > 0)
		{
			dif.append((milis / 1000 / 60 / 60) % 24);
			dif.append(" hora(s) ");
		}
		dif.append((milis / 1000 / 60) % 60);
		dif.append(" minuto(s)");
		return dif.toString();
	}

	/**
	 * Método que calcula o tempo em horas/minuto/segundo com base no parametro
	 * tempo em milisegundos.
	 * @param tempo
	 * @return tempo no formato HH:mm:ss.
	 */
	public static final String time (double tempo)
	{
		double hr = (tempo / (1000d * 60d * 60d));
		double min = (tempo / (1000d * 60d)) % 60;
		double seg = (tempo / (1000d)) % 60;

		return convert(hr) + ":" + convert(min) + ":" + convert(seg);
	}

	/**
	 * Método que calcula o tempo em dias/horas/minuto/segundo com base no
	 * parametro tempo em milisegundos, descontando os finais de semana.
	 * @param tempo
	 * @return tempo no formato HH:mm:ss.
	 */
	public static final String time (double tempo, double weekend)
	{
		double dia, hr, min, seg;

		if (tempo > 86400000)
		{
			tempo = tempo - weekend;
			dia = (tempo / (1000 * 60 * 60 * 24));
			hr = ((dia - (int)dia) * 24);
			min = ((hr - (int)hr) * 60);
			seg = (int)((min - (int)min) * 60);
			return convert(dia) + "d " + convert(hr) + ":" + convert(min) + ":" + convert(seg);
		}
		return time(tempo);
	}

	/**
	 * função que converte um valor de tempo de double para long
	 * @param vlr
	 * @return
	 */
	public static final String convert (double vlr)
	{
		Long l = new Long(new Double(vlr).longValue());
		return l.toString().length() >= 2 ? l.toString() : ("0" + l.toString());
	}

	/**
	 * Método responsável por verificar se duas datas são do mesmo Mês e ano.
	 * @param initialDate
	 * @param finalDate
	 * @return
	 */
	public static boolean isSameMonth (java.util.Date initialDate, java.util.Date finalDate)
	{
		return (isSameTime(initialDate, finalDate, Calendar.MONTH) && isSameYear(initialDate,
		    finalDate));
	}

	/**
	 * Método responsável por verificar se duas datas são do mesmo Ano.
	 * @param initialDate
	 * @param finalDate
	 * @return
	 */
	public static boolean isSameYear (java.util.Date initialDate, java.util.Date finalDate)
	{
		return isSameTime(initialDate, finalDate, Calendar.YEAR);
	}

	/**
	 * Método responsável por verificar se duas datas são da mesma semana do Mês
	 * e ano.
	 * @param initialDate
	 * @param finalDate
	 * @return
	 */
	public static final boolean isSameWeekOfMonth (java.util.Date initialDate,
	    java.util.Date finalDate)
	{
		return (isSameTime(initialDate, finalDate, Calendar.WEEK_OF_MONTH) && isSameMonth(
		    initialDate, finalDate));
	}

	/**
	 * Método private responsável por executar a comparação de duas datas e
	 * verificar se pertencem a unidade de tempo passada como parametro.
	 * @param initialDate
	 * @param finalDate
	 * @return
	 */
	private static final boolean isSameTime (final java.util.Date initialDate,
	    final java.util.Date finalDate, final int time)
	{
		if ((initialDate == null) || (finalDate == null))
		{
			return false;
		}
		switch (time)
		{
		case Calendar.WEEK_OF_MONTH:
			return (getCalendar(initialDate).get(Calendar.WEEK_OF_MONTH) == getCalendar(finalDate).get(
			    Calendar.WEEK_OF_MONTH));
		case Calendar.MONTH:
			return (getCalendar(initialDate).get(Calendar.MONTH) == getCalendar(finalDate).get(
			    Calendar.MONTH));
		case Calendar.YEAR:
			return (getCalendar(initialDate).get(Calendar.YEAR) == getCalendar(finalDate).get(
			    Calendar.YEAR));
		default:
			return false;
		}
	}

	/**
	 * Retorna o próximo dia útil, a partir da data passada como parametro. A
	 * data retornada não é um feriado e nem um final de semana.
	 * @param date a partir da qual será localizado o próximo dia útil.
	 * @return Date Dia útil.
	 */
	public static final java.util.Date proximoDiaUtil (Timestamp date) throws Exception
	{
		if (date == null)
		{
			throw new Exception(
			    "É obrigatório informar uma data para retornar o próximo dia útil !!");
		}
		java.util.Date proxDate = addDiasDate(date, 1);
		while (isWeekend(proxDate) != null)
		{
			proxDate = addDiasDate(proxDate, 1);
		}
		return proxDate;
	}
}
