/**
 * 
 */
package br.com.laserviewpr.infra.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.laserviewpr.infra.DAO.DaoFactory;
import br.com.laserviewpr.infra.action.servlet.CarregarArquivoLocal;
import br.com.laserviewpr.infra.report.ReportParameters;
import br.com.laserviewpr.infra.util.DateUtils;
import br.com.laserviewpr.infra.util.GenericUtils;
import br.com.laserviewpr.infra.util.Validator;
import br.com.laserviewpr.intranet.vo.LaudoVO;
import br.com.laserviewpr.intranet.vo.PessoaVO;
import br.com.laserviewpr.intranet.vo.UsuarioInternoVO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe responsável pela lógica de autenticação para acesso ao laudo onLine.
 * 
 * @author Rabelo Serviços.
 */
public class LaudoOnlineAction extends ActionSupport
		implements
			ReportParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 13102010197645454L;

	private PessoaVO pessoaVo;

	// numero do cadastro do paciente
	private Long nroCadastroPaciente;
	private Date dataNascimento;
	// numero de laudo especifico.
	private Long nroLaudo;

	/**
	 * 
	 */
	@Override
	@Action(value = "autenticacaoLaudoOnline", results = {@Result(location = "/jsp/autenticacaoLaudoOnline.jsp", name = "success")})
	public String execute() {
		//this.setDataNascimento(Calendar.getInstance().getTime());
		return SUCCESS;
	}

	/**
	 * Método util na pesquisa de laudos.<br>
	 * Esse método é útil em dois processos:<br>
	 * <p>
	 * 1-Listar Laudo On-line para os Pacientes
	 * </p>
	 * <p>
	 * 2-Listar Laudos internos para os Colaboradores. <br>
	 * Neste caso é preciso criar o objeto pessoa apartir do codigo do cadastro
	 * passado como parametro para a pesquisa.
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "listarLaudos", results = {
			@Result(location = "/jsp/listaLaudos.jsp", name = "success"),
			@Result(location = "/jsp/listaLaudos.jsp", name = "error")})
	public String listarLaudos() throws Exception {
		// pesquisar laudos
		try {
			final UsuarioInternoVO us = LaudoColaboradoresAction
					.getUserSession();
			if ((this.getPessoaVo() == null) && (us != null)
					&& (this.getNroCadastroPaciente() != null)
					&& (this.getNroCadastroPaciente() > 0)) {
				// Laudos internos, pesquisar o paciente e atribuí-lo a pessoa
				// do laudo.
				this.setPessoaVo(DaoFactory.getInstance()
						.getPacienteByCodMatricula(
								this.getNroCadastroPaciente()));
			} else {
				// setar valor para indicar que é pesquisa de laudo por
				// paciente.
				this.getPessoaVo().setUrlSite("is_url_site");
			}
			// pesquisar laudos na base de dados
			// this.setListaLaudos(DaoFactory.getInstance().getLaudos(this.getNroCadastroPaciente(),
			// null, null));

			// imagens de laudos específicos do paciente logado no sistema.
			this.setListaLaudos(this.lerArquivosImages());
			if (!Validator.notEmptyCollection(this.getListaLaudos())) {
				this.addFieldError("nroCadastroPaciente",
						"Nenhum Laudo localizado!!");
				return ERROR;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		}
		return SUCCESS;
	}

	/**
	 * @return
	 */
	@Action(value = "autenticarPaciente", results = {
			@Result(location = "/jsp/listaLaudos.jsp", name = "success"),
			@Result(location = "/jsp/autenticacaoLaudoOnline.jsp", name = "error")})
	public String autenticarPaciente() {
		final Long cdPessoa = this.getNroCadastroPaciente();

		// validar campos de entrada.
		if (cdPessoa == null) {
			this.addFieldError("nroCadastroPaciente",
					"* Campo Obrigatório. (Usar apenas Números)");
			return ERROR;
		}
		if (this.getDataNascimento() == null) {
			this
					.addFieldError(
							"dataNascimento",
							"* Campo Obrigatório. Preencher com uma Data Válida no formato dd/mm/aaaa (Exemplo: 01/05/1989)");
			return ERROR;
		}

		try {
			this.setPessoaVo(DaoFactory.getInstance()
					.getPacienteByCodMatricula(cdPessoa));
			if ((this.getPessoaVo() == null)
					|| (this.getPessoaVo().getDataNascimento() == null)) {
				this.addFieldError("nroCadastroPaciente",
						"* Nº de Matricula Incorreto!!");
				return ERROR;
			}
			// pessoa localizada, validar a data nascimento. Adicionar +1h nas
			// datas para garantir distorção no horário de verão em algumas
			// regioes.
			final Date dataNascPessoa = DateUtils.addAmountDate(this
					.getPessoaVo().getDataNascimento(), +1,
					Calendar.HOUR_OF_DAY);
			final Date dataNascField = DateUtils.addAmountDate(this
					.getDataNascimento(), +1, Calendar.HOUR_OF_DAY);

			if (dataNascPessoa.compareTo(dataNascField) != 0) {
				this
						.addFieldError("dataNascimento",
								"Data de Nascimento Incorreta para a Matrícula Informada!!");
				// Dt.Nasc. Matr: " +
				// (DateUtils.formatDateDDMMYYYY(dataNascPessoa) + " Dt.Nasc.
				// Informada: " + DateUtils.formatDate(dataNascField)));
				return ERROR;
			}

			// Pessoa localizada e data nascimento conferida. pesquisar os
			// laudos.
			this.listarLaudos();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			this.addFieldError("nroCadastroPaciente", ex.getMessage());
			return ERROR;
		} catch (Exception ex) {
			ex.printStackTrace();
			this.addFieldError("nroCadastroPaciente", ex.getMessage());
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * @return the listaLaudos
	 */
	public List<LaudoVO> getListaLaudos() {
		return this.getPessoaVo() != null
				? this.getPessoaVo().getListaLaudos()
				: null;
	}

	/**
	 * @param listaLaudos
	 *            the listaLaudos to set
	 */
	public void setListaLaudos(List<LaudoVO> listaLaudos) {
		if (this.getPessoaVo() == null) {
			this.setPessoaVo(new PessoaVO());
		}
		this.getPessoaVo().setListaLaudos(listaLaudos);
	}

	/**
	 * @return the nroCadastroPaciente
	 */
	public Long getNroCadastroPaciente() {
		return nroCadastroPaciente;
	}

	/**
	 * @param nroCadastroPaciente
	 *            the nroCadastroPaciente to set
	 */
	public void setNroCadastroPaciente(Long nroCadastroPaciente) {
		this.nroCadastroPaciente = nroCadastroPaciente;
	}

	/**
	 * @return the dataNascimento
	 */
	public Date getDataNascimento() {
		return dataNascimento;
	}

	/**
	 * @param dataNascimento
	 *            the dataNascimento to set
	 */
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	/**
	 * @return the pessoaVo
	 */
	public PessoaVO getPessoaVo() {
		return pessoaVo;
	}

	/**
	 * @param pessoaVo
	 *            the pessoaVo to set
	 */
	public void setPessoaVo(PessoaVO pessoaVo) {
		this.pessoaVo = pessoaVo;
	}

	/**
	 * @return the nroLaudo
	 */
	public Long getNroLaudo() {
		return nroLaudo;
	}

	/**
	 * @param nroLaudo
	 *            the nroLaudo to set
	 */
	public void setNroLaudo(Long nroLaudo) {
		this.nroLaudo = nroLaudo;
	}

	/**
	 * Método responsavel por ler nomes dos arquivos de imagens para paciente
	 * especifico.
	 */
	public List<LaudoVO> lerArquivosImages() {
		// C:\Documentos\Sistemas\Risc\Images
		// Há diversos arquivos para diversos pacientes nesse diretorio:
		// 11932_080415_1.pdf, 11932_080415_2.JPG, 11932_080415_2.pdf

		// retornar apenas arquivos com mesmo codigo do paciente logado.
		final Long cdPessoa = this.getPessoaVo().getCdPessoa();
		if (cdPessoa == null) {
			return null;
		}

		String caminho = CarregarArquivoLocal.DIRETORIO_IMAGENS;
		List<LaudoVO> lista = null;

		// retorna conteúdo do diretorio.
		File raiz = new File(caminho);
		if (raiz.exists()) {

			final StringBuilder sb = new StringBuilder();
			Scanner scanner = null;
			lista = new ArrayList<LaudoVO>();
			LaudoVO p = null;
			Long codtemp = null;
			for (File f : raiz.listFiles()) {
				if (f.isFile()) {
					// quebrar nome do arquivo usando expressao regular:
					// 11932_080415_2.JPG
					scanner = new Scanner(f.getName()).useDelimiter("_");
					codtemp = GenericUtils.toLong(scanner.next());
					if (codtemp != null
							&& cdPessoa.longValue() == codtemp.longValue()) {
						p = new LaudoVO();
						while (scanner.hasNext()) {
							p.setCdconvenio(codtemp);
							p.setDsexamecompl(f.getName());// file name.
							p.setDtconsulta(DateUtils.parseDate(DateUtils
									.convertToMinDateDDMMYY(scanner.next())));// data
							scanner.next();// sequencia + extensao
						}
						lista.add(p);
						// log
						sb.append(f.getName());
						sb.append("\n");
					}
				}
			}
			System.out.println(sb.toString());
		}
		raiz = null;
		return lista;
	}

}
