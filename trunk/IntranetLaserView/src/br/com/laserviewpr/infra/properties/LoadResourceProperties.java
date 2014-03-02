package br.com.laserviewpr.infra.properties;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * Classe Singleton responsável por recuperar arquivos de propriedades na pasta
 * padrão resources do sistema.
 * @author desenvolvimento
 */
public final class LoadResourceProperties
    implements Serializable
{

	private static final long serialVersionUID = 972121649876541L;
	private static LoadResourceProperties instance;

	private LoadResourceProperties ()
	{
	}

	/**
	 * Este método retorna uma instância de objeto se o objeto for vazio, senão
	 * retorna o a instância ja criada. Implementa Design Pattern Singleton.
	 * @return ServiceListener.
	 */
	public static LoadResourceProperties getInstance ()
	{
		if (instance == null)
		{
			instance = new LoadResourceProperties();
		}
		return instance;
	}

	/**
	 * Método que recupera e retorna um arquivo de propriedades numa pasta
	 * específica do sistema.
	 * @param fileName
	 * @return
	 */
	public final Properties getPropertie (String fileName)
	{
		if ((fileName == null) || (fileName.trim().length() <= 0))
		{
			return null;
		}
		Properties properties = null;
		try
		{
			properties = new Properties();
			InputStream resourceAsStream = this.getResourceAsStream(fileName);
			properties.load(resourceAsStream);
			resourceAsStream = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			fileName = null;
		}
		return properties;
	}

	/**
	 * Método que localiza e retorna InputStream do arquivo de propriedades
	 * específico.
	 * @param fileName
	 * @return
	 */
	public final InputStream getResourceAsStream (String fileName)
	{
		if ((fileName == null) || (fileName.trim().length() <= 0))
		{
			return null;
		}
		try
		{
			return this.getClass().getResourceAsStream("/" + fileName);
		}
		catch (Exception e)
		{
			System.out.println("*** Atenção: Não foi possível carregar arquivo de propriedades : "
			    + fileName);
			e.printStackTrace();
		}
		finally
		{
			fileName = null;
		}
		return null;
	}
}
