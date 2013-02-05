/**
 * 
 */
package br.com.oappr.infra.report;

import java.io.Serializable;

/**
 * Interface com nomes dos parametros dos relatórios desnevolvidos no IReport.
 * @author Rabelo Serviços.
 */
public interface ReportParameters
    extends Serializable
{
	public static final java.lang.String LOGO_PATH = "LOGO_PATH";
	public static final java.lang.String LABEL_PACIENTE = "PACIENTE";
	public static final java.lang.String LABEL_NOME_LAUDO = "NOME_LAUDO";
	public static final java.lang.String LABEL_DESCR_MEDICO = "DESCR_MEDICO";
	public static final java.lang.String LABEL_MEDICO_RESPONSAVEL_EXAME = "MEDICO_RESPONSAVEL_EXAME";
	public static final java.lang.String LABEL_CRM_MEDICO_RESPONSAVEL_EXAME = "CRM_MEDICO_RESPONSAVEL_EXAME";
	public static final java.lang.String LABEL_CIDADE_DATA_EXAME = "CIDADE_DATA_EXAME";
	public static final java.lang.String LABEL_ASSINATURA_MEDICO_PATH = "ASSINATURA_MEDICO_PATH";
}
