/**
 * 
 */
package teste;

/**
 * @author rabelo
 */
public final class Teste
{

	/**
	 * @param args
	 */
	public static void main (String[] args)
	{
		new Teste().exec();
	}

	private final void exec ()
	{
		// try
		// {
		// Runtime.getRuntime().exec("C:\\Program
		// Files\\Notepad++\\notepad++.exe");

		// server SRVOAP C:/CENTURA5/ACDUPLIC.EXE ADCON ADCON@ORACLE
		String[] cm = {"ssh", "SRVOAP@SRVOAP", "C:/CENTURA5/ACDUPLIC.EXE ADCON ADCON@ORACLE"

		};

		try
		{
			Process q = Runtime.getRuntime().exec(cm);
			q.waitFor();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
	}
}
