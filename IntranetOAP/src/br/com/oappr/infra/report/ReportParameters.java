/**
 * 
 */
package br.com.oappr.infra.report;

import java.io.Serializable;

/**
 * Interface com nomes dos parametros dos relatórios desnevolvidos no IReport.
 * @author rabelo
 */
public interface ReportParameters
    extends Serializable
{
	public static final java.lang.String LOGO_PATH = "LOGO_PATH";
	public static final java.lang.String PACIENTE = "PACIENTE";
	public static final java.lang.String NOME_LAUDO = "NOME_LAUDO";
	public static final java.lang.String DESCR_MEDICO = "DESCR_MEDICO";
	public static final java.lang.String MEDICO_RESPONSAVEL_EXAME = "MEDICO_RESPONSAVEL_EXAME";
	public static final java.lang.String CRM_MEDICO_RESPONSAVEL_EXAME = "CRM_MEDICO_RESPONSAVEL_EXAME";
	public static final java.lang.String CIDADE_DATA_EXAME = "CIDADE_DATA_EXAME";
}
