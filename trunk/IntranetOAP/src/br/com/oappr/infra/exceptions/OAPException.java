/**
 * 
 */
package br.com.oappr.infra.exceptions;

import java.io.Serializable;

/**
 * @author Rabelo Serviços.
 */
public interface OAPException
    extends Serializable
{
	public static final String erro = "Não foi possível realizar operação!";
}
