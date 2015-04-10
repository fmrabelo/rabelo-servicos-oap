package teste;

import br.com.laserviewpr.infra.util.DateUtils;

public class Teste {

	public static void main(String[] args) {
		String dt = "311090";
		String dia = dt.substring(0, 2);
		String mes = dt.substring(2, 4);
		String ano = dt.substring(4, 6);
		System.out.println(dia);
		System.out.println(mes);
		System.out.println(ano);
		java.util.Date dtf = DateUtils.parseDate(dia + "/" + mes + "/" + ano);
		System.out.println(dtf);
	}
}
