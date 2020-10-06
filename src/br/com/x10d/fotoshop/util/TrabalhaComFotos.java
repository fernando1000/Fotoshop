package br.com.x10d.fotoshop.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class TrabalhaComFotos {

	public String devolveStringPicturePathBaseadoEmCaminhoDaFotoSelecionada(Context context, Uri caminhoDaFotoSelecionada) {

		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(caminhoDaFotoSelecionada, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

		String picturePath = cursor.getString(columnIndex);

		cursor.close();

		//Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

		//BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

		return picturePath;
	}

	
	
	public BitmapDrawable devolveBitmapDrawableBaseadoEmCaminhoDaFotoSelecionada(Context context, Uri caminhoDaFotoSelecionada) {

		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(caminhoDaFotoSelecionada, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

		String picturePath = cursor.getString(columnIndex);

		cursor.close();

		Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

		return bitmapDrawable;
	}

	public boolean tiraScreenShot(Activity activity, LinearLayout linearLayout_tela, String nomeEmpresa_cnpj) {

		boolean deuErro = false;

		//----- Tira ScreenShot da tela inteira
		//View view = activity.getWindow().getDecorView().getRootView();
			 //view.setDrawingCacheEnabled(true);
		//Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
			 //view.setDrawingCacheEnabled(false);
		//----- Tira ScreenShot da tela inteira
		
		//----- Tira ScreenShot apenas da linearLayout_tela
		View view_linearLayout = linearLayout_tela;
	         view_linearLayout.setDrawingCacheEnabled(true);  
	    
        int totalWidth = linearLayout_tela.getChildAt(0).getWidth();
	    int totalHeight = linearLayout_tela.getChildAt(0).getHeight();
	    	    
	         view_linearLayout.layout(0, 0, totalWidth, totalHeight);    
	         view_linearLayout.buildDrawingCache(true);
	    Bitmap bitmap = Bitmap.createBitmap(view_linearLayout.getDrawingCache());             
	         view_linearLayout.setDrawingCacheEnabled(false);	 
 		//----- Tira ScreenShot apenas da linearLayout_tela
		
		File imageFile = new File(criaStringDoCaminhoComDataAtual(nomeEmpresa_cnpj));

		if(!imageFile.exists()){
			imageFile.getParentFile().mkdirs();  	
    	}
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(imageFile);

			int quality = 100;

			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);

			fileOutputStream.flush();
			fileOutputStream.close();
		} 
		catch (Exception e) {

			deuErro = true;
			e.printStackTrace();

		}
		return deuErro;
	}

	private String criaStringDoCaminhoComDataAtual(String nomeEmpresa_cnpj) {

		DataPersonalizada dataPersonalizada = new DataPersonalizada();

		String dataAtual = dataPersonalizada.pegaDataAtual_YYYYMMDD_HHMMSS();
		
		//String diretorio_comJPG = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + dataAtual + nomeEmpresa + ".jpg";
		
		
		String diretorio_comJPG = Environment.getExternalStorageDirectory() +"/ContratoDigital/"+nomeEmpresa_cnpj+"/"+dataAtual+".jpg";	
		
		return diretorio_comJPG;
	}

	public Uri criaEDevolveDiretorioOndeAFotoSeraSalva() {

		File newfile = new File(criaStringDoCaminhoComDataAtual(""));

		try {
			newfile.createNewFile();
		} catch (IOException iOException) {
			Log.e("tag", "" + iOException);
		}

		Uri outputFileUri = Uri.fromFile(newfile);

		return outputFileUri;
	}

	/*
	 * private void abrirImagem(Context context, File imageFile) {
	 * 
	 * Intent intent = new Intent(); intent.setAction(Intent.ACTION_VIEW);
	 * 
	 * Uri uri = Uri.fromFile(imageFile); Log.i("tag", "uri da image File; " +
	 * uri);
	 * 
	 * intent.setDataAndType(uri, "image/*");
	 * 
	 * context.startActivity(intent); }
	 */

}
