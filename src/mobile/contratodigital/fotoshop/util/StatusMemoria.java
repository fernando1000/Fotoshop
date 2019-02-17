package mobile.contratodigital.fotoshop.util;

import android.util.Log;

public class StatusMemoria {

	private void mostraStatusMemoria(){
		
		final Runtime runtime = Runtime.getRuntime();
		final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
		final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
		final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
		
		Log.i("tag", "Usada MB = "+usedMemInMB);
		Log.i("tag", "Max Heap MB = "+maxHeapSizeInMB);
		Log.i("tag", "Disponivel MB = "+availHeapSizeInMB);
		Log.i("tag", "------------------------------------------------------");
		
	}

}
