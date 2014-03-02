/**
 * 
 */
package br.com.laserviewpr.infra.exceptions;

import java.io.Serializable;

/**
 * @author Rabelo Serviços.
 */
public interface IntranetException
    extends Serializable
{
	public static final String erro = "Não foi possível realizar operação!";
}
