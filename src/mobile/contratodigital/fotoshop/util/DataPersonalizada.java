package mobile.contratodigital.fotoshop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataPersonalizada {
	
	private static String YYYYMMDD_HHMMSS = "yyyyMMdd_hhmmss";
	
	public String pegaDataAtual_YYYYMMDD_HHMMSS() {
			
		return devolveDataAtualNoFormato(YYYYMMDD_HHMMSS);
	}
	
	private String devolveDataAtualNoFormato(String formato){

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
		
		return simpleDateFormat.format(new Date());
	}
	
}

