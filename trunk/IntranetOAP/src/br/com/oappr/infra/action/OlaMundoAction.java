/**
 * 
 */
package br.com.oappr.infra.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.intranet.struts20.LaudoVO;

/**
 * @author rabelo
 */
// @Validations(requiredStrings = {@RequiredStringValidator(fieldName =
// "nroCadastroPaciente", message = "Valor obrigatório")}, stringLengthFields =
// {@StringLengthFieldValidator(fieldName = "nroCadastroPaciente", minLength =
// "5", message = "Min. 5 car.")})
// -----------------------------------------
// @Results({@Result(name = "success", type = "jasper", params = {"location",
// "/WEB-INF/reports/TONOMETRIA.jasper", "imageServletUrl",
// "/servlets/image?image=",
// "dataSource", "users", "reportParameters", "params", "format", "HTML"})})
public class OlaMundoAction
{
	private List<LaudoVO> listaLaudos;
	// numero do cadastro do paciente
	private Long nroCadastroPaciente;
	private Date dataNascimento;

	@Action(value = "olaMundoStruts", results = {@Result(location = "/jsp/olaMundoStruts.jsp", name = "success")})
	public String execute ()
	{
		System.out.println("Executando a lógica com Struts2");
		this.setDataNascimento(Calendar.getInstance().getTime());
		// return "ok";
		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	@Action(value = "listarLaudos", results = {@Result(location = "/jsp/listaLaudos.jsp", name = "success")})
	public String listarLaudos ()
	{
		System.out.println("listar laudos...");
		// pesquisar laudos

		this.setListaLaudos(this.getLaudos());
		this.setDataNascimento(Calendar.getInstance().getTime());
		if ((listaLaudos != null) && !listaLaudos.isEmpty())
		{
			for (LaudoVO v : listaLaudos)
			{
				System.out.printf("ID: %s Laudo: %s %n", v.getId(), v.getDescricao());
			}
		}

		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	// @Action(value = "gerarLaudoPdf", results = {@Result(location =
	// "/jsp/listaLaudos.jsp", name = "success")})
	public String gerarLaudoPdf ()
	{
		System.out.println("PDF laudos...");

		try
		{
			if ((listaLaudos != null) && !listaLaudos.isEmpty())
			{
				System.out.println("Lista Laudos OK. " + listaLaudos.size());
			}
			else
			{
				this.listarLaudos();
			}
			// JasperCompileManager.compileReportToFile("/reports/TONOMETRIA.jrxml",
			// "/reports/TONOMETRIA.jasper");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return com.opensymphony.xwork2.Action.ERROR;
		}

		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	@Action(value = "autenticarPaciente", results = {@Result(location = "/jsp/listaLaudos.jsp", name = "success")})
	public String autenticarPaciente ()
	{
		System.out.println("Autentiar Pacientes...");
		System.out.println("Nro Paciente: " + this.getNroCadastroPaciente());
		System.out.println("Data Nascimento: " + this.getDataNascimento());
		if (nroCadastroPaciente != null)
		{
			this.listarLaudos();
		}
		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	/**
	 * Monta lista de laudos.
	 * @return
	 */
	private final List<LaudoVO> getLaudos ()
	{
		List<LaudoVO> list = new ArrayList<LaudoVO>();
		LaudoVO l1 = new LaudoVO();
		l1.setId(1098L);
		l1.setDescricao("TONOMETRIA");
		l1.setDataFinalizacao(Calendar.getInstance().getTime());
		l1.setFinalizado(true);
		list.add(l1);

		LaudoVO l2 = new LaudoVO();
		l2.setId(3356L);
		l2.setDescricao("ACUIDADE VISUAL (PAM)");
		l2.setDataFinalizacao(Calendar.getInstance().getTime());
		l2.setFinalizado(true);
		list.add(l2);

		LaudoVO l3 = new LaudoVO();
		l3.setId(1584L);
		l3.setDescricao("BIOMETRIA ULTRASSONICA");
		l3.setDataFinalizacao(Calendar.getInstance().getTime());
		l3.setFinalizado(false);
		list.add(l3);

		LaudoVO l4 = new LaudoVO();
		l4.setId(1123L);
		l4.setDescricao("MICROSCOPIA ESPECULAR DE CORNEA");
		l4.setDataFinalizacao(Calendar.getInstance().getTime());
		l4.setFinalizado(false);
		list.add(l4);

		LaudoVO L5 = new LaudoVO();
		L5.setId(7454L);
		L5.setDescricao("CERATOSCOPIA COMPUTADORIZADA");
		L5.setDataFinalizacao(Calendar.getInstance().getTime());
		L5.setFinalizado(true);
		list.add(L5);

		LaudoVO L6 = new LaudoVO();
		L6.setId(9652L);
		L6.setDescricao("MAPEAMENTO DE RETINA");
		L6.setDataFinalizacao(Calendar.getInstance().getTime());
		L6.setFinalizado(false);
		list.add(L6);

		LaudoVO L7 = new LaudoVO();
		L7.setId(3625L);
		L7.setDescricao("RETINOGRAFIA FLUORESCENTE");
		L7.setDataFinalizacao(Calendar.getInstance().getTime());
		L7.setFinalizado(true);
		list.add(L7);

		l1 = l2 = l3 = l4 = L5 = L6 = null;
		return list;
	}

	/**
	 * @return the listaLaudos
	 */
	public List<LaudoVO> getListaLaudos ()
	{
		return listaLaudos;
	}

	/**
	 * @param listaLaudos the listaLaudos to set
	 */
	public void setListaLaudos (List<LaudoVO> listaLaudos)
	{
		this.listaLaudos = listaLaudos;
	}

	/**
	 * @return the nroCadastroPaciente
	 */
	public Long getNroCadastroPaciente ()
	{
		return nroCadastroPaciente;
	}

	/**
	 * @param nroCadastroPaciente the nroCadastroPaciente to set
	 */
	public void setNroCadastroPaciente (Long nroCadastroPaciente)
	{
		this.nroCadastroPaciente = nroCadastroPaciente;
	}

	/**
	 * @return the dataNascimento
	 */
	public Date getDataNascimento ()
	{
		return dataNascimento;
	}

	/**
	 * @param dataNascimento the dataNascimento to set
	 */
	public void setDataNascimento (Date dataNascimento)
	{
		this.dataNascimento = dataNascimento;
	}
}
