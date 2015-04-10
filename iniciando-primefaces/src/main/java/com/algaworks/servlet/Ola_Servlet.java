package com.algaworks.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OiMundoServlet
 */
@WebServlet(description = "Servlet teste Tomcat.", urlPatterns = {"/Ola_Servlet"})
public class Ola_Servlet
    extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Ola_Servlet ()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<body>");
		out.print("<h1>Oi Mundo</h1>");
		this.printProperties(out);
		out.print("</body>");
		out.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}

	final public void printProperties (final PrintWriter out)
	{
		final Properties p = System.getProperties();
		for (Enumeration<Object> iterator = p.elements(); iterator.hasMoreElements();)
		{
			Object obj = (Object)iterator.nextElement();
			out.print("<h5>" + obj + "</h5>");
		}
	}
}
