package br.com.oappr.infra.util.email.attachment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.activation.DataSource;

/**
 * Classe utilitário para conter conteúdo de arquivo in-memory a ser enviado por
 * email como anexo.
 * @author desenvolvimento
 */
public class InputStreamDataSource
    implements DataSource, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2608758512121260875L;
	private final String name;
	private final String contentType;
	private final ByteArrayOutputStream baos;

	/**
	 * @param name
	 * @param contentType
	 * @param inputStream
	 * @throws IOException
	 */
	public InputStreamDataSource (String name, String contentType, InputStream inputStream)
	    throws IOException
	{
		this.name = name;
		this.contentType = contentType;
		baos = new ByteArrayOutputStream();
		int read;
		byte[] buff = new byte[256];
		while ((read = inputStream.read(buff)) != -1)
		{
			baos.write(buff, 0, read);
		}
	}

	/**
	 * @param name
	 * @param contentType
	 * @param outputStream
	 * @throws IOException
	 */
	public InputStreamDataSource (String name, String contentType,
	    ByteArrayOutputStream outputStream) throws IOException
	{
		this.name = name;
		this.contentType = contentType;
		baos = outputStream;
	}

	public String getContentType ()
	{
		return contentType;
	}

	public InputStream getInputStream () throws IOException
	{
		return new ByteArrayInputStream(baos.toByteArray());
	}

	public String getName ()
	{
		return name;
	}

	public OutputStream getOutputStream () throws IOException
	{
		throw new IOException("recurso apenas para leitura, não sendo possível escrita.");
	}

}
