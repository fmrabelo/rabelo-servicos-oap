package br.com.oappr.infra.util;

import java.io.Serializable;

/**
 * classe que contém as constantes utilizadas na camada de apresentação web.
 * @author Rabelo Serviços.
 */
public abstract class Constants
    implements Serializable
{

	// constantes para controle.
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final String SESSION_USER_OAP = "SESSION_USER_OAP";

	public final class Default
	{

		// Formato de Data
		public static final String formatDateExtensoBR = "dd' de 'MMMM' de 'yyyy";
		public static final String formatDate = "dd/MM/yyyy";
		public static final String formatDateTime = "dd/MM/yyyy HH:mm:ss";
		public static final String formatTimeHHMMSS = "HH:mm:ss";
		public static final String formatTimeHHMM = "HH:mm";
		public static final String timeZone = "America/Fortaleza";
		public static final byte LC_DEFAULT = Locale.LOCALE_BR;

		public static final String DEFAULT_PATTERN = "###,###,###.##";
		public static final String PATTERN_ZERO = "###,###,##0";
		public static final String PATTERN_ONE = "###,###,##0.0";
		public static final String PATTERN_TWO = "###,###,##0.00";
		public static final String PATTERN_THREE = "###,###,##0.000";
		public static final String PATTERN_FOUR = "###,###,##0.0000";

		public static final String DEFAUT_PATTERN_NEGATIVE_AMOUNT = "###,###,###.##;(-###,###,###.##)";
		public static final String PATTERN_ZERO_NEGATIVE_AMOUNT = "###,###,##0;(-###,###,##0)";
		public static final String PATTERN_ONE_NEGATIVE_AMOUNT = "###,###,##0.0;(-###,###,##0.0)";
		public static final String PATTERN_TWO_NEGATIVE_AMOUNT = "###,###,##0.00;(-###,###,##0.00)";
		public static final String PATTERN_THREE_NEGATIVE_AMOUNT = "###,###,##0.000;(-###,###,##0.000)";
		public static final String PATTERN_FOUR_NEGATIVE_AMOUNT = "###,###,##0.0000;(-###,###,##0.0000)";
	}

	public static final class Locale
	{

		public static final byte LOCALE_US = 0;
		public static final byte LOCALE_DE = 1;
		public static final byte LOCALE_FR = 2;
		public static final byte LOCALE_BR = 3;
	}

	public static final class Date
	{

		// Variaveis que indicam o tipo de validação do intervalo das datas se
		// por dia, mes ou ano.
		public static final byte DATE = 1;
		public static final byte MONTH = 2;
		public static final byte YEAR = 3;
	}

	public static final class StatusOptions
	{
		// status locados
		public static final byte EDIT_STATUS = 10;
		public static final byte REMOVE_STATUS = 20;
		public static final byte SAVE_STATUS = 30;
	}

}
