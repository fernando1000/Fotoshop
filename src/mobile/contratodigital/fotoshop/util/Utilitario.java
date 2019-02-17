package mobile.contratodigital.fotoshop.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import br.com.extend.fotoshop.R;

public class Utilitario {

	public void editaViewQEstaDentroDeUmFrameLayout(Context context, FrameLayout frameLayout, ArrayList<Integer> arrayList_itens) {

		if (frameLayout.getChildCount() > 0) {

			arrayList_itens.clear();

			mudaDeImagensParaNumeros(context, frameLayout, arrayList_itens);

			ordenaLista(arrayList_itens);
		
			mostraAlertDialog(context, frameLayout, arrayList_itens, "Editar");
		}
	}
	
	private void mudaDeImagensParaNumeros(Context context, FrameLayout frameLayout, ArrayList<Integer> arrayList_itens){
		
		for (int x = 0; x < frameLayout.getChildCount(); x++) {

			ImageView imageView = (ImageView) frameLayout.getChildAt(x);

			int numeroSalvoNoOnDrag = (Integer) imageView.getTag(R.id.numero);
			
			arrayList_itens.add(numeroSalvoNoOnDrag);
			
			if(numeroSalvoNoOnDrag < ListaComTodasImageViews.pegaListaDeNumeros().size()){
				
				imageView.setImageDrawable(context.getResources().getDrawable(ListaComTodasImageViews.pegaListaDeNumeros().get(numeroSalvoNoOnDrag)));
			}else{
				imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));			
			}
		}
	}

	private void ordenaLista(ArrayList<Integer> arrayList_itens){
		
		Collections.sort(arrayList_itens, new Comparator<Integer>() {
			@Override
			public int compare(Integer intA, Integer intB) {

				return intA.compareTo(intB);
			}
		});
	}

	private void mostraAlertDialog(final Context context, final FrameLayout frameLayout, final ArrayList<Integer> arrayList_itens, final String titulo){
		
		ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.item_menu_geral, arrayList_itens);

		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setTitle(titulo + " imagem nº:");
		builder1.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int posicaoEscolhida) {

				arrayList_itens.clear();

				mudaDeNumerosParaImagens(context, frameLayout);
		
				jogaImagemEscolhidaParaFrente(context, frameLayout, posicaoEscolhida, titulo, arrayList_itens);
				
				dialogInterface.dismiss();
			}
		});
		builder1.show();
	}
		
	private void jogaImagemEscolhidaParaFrente(Context context, FrameLayout frameLayout, int posicaoEscolhida, String titulo, ArrayList<Integer> arrayList_itens){

		for (int x = 0; x < frameLayout.getChildCount(); x++) {

			ImageView imageView = (ImageView) frameLayout.getChildAt(x);
			
			int numeroSalvoNoOnDrag = (Integer) imageView.getTag(R.id.numero);
	
			if (numeroSalvoNoOnDrag == posicaoEscolhida) {

				Animacao animacao = new Animacao();
				
				if(titulo.equals("Editar")){
				
					
					animacao.iniciaAnimacao(context, imageView, R.anim.incolhedepoisvoltaaonormal);
					
					frameLayout.bringChildToFront(imageView);
				}
				
				if(titulo.equals("Deletar")){
			
					animacao.iniciaAnimacao(context, imageView, R.anim.sai_pela_esquerda);
					
					frameLayout.removeView(imageView);
					
					arrayList_itens.clear();

					renomeiaOsNumerosDasTags(frameLayout, arrayList_itens);
				}
			}		  		  
		}
	}
	
	private void renomeiaOsNumerosDasTags(FrameLayout frameLayout, ArrayList<Integer> arrayList_itens){
		
		for (int x = 0; x < frameLayout.getChildCount(); x++) {

			ImageView imageView = (ImageView) frameLayout.getChildAt(x);
					  imageView.setTag(R.id.numero, x);
		}
	}

	private void mudaDeNumerosParaImagens(Context context, FrameLayout frameLayout){
		
		for (int x = 0; x < frameLayout.getChildCount(); x++) {

			ImageView imageView = (ImageView) frameLayout.getChildAt(x);
			  		  //imageView.setImageDrawable(context.getResources().getDrawable(Integer.parseInt(imageView.getTag().toString())));		  
			  		  imageView.setImageBitmap(DiminuiMBimagens.decodeSampledBitmapFromResource(context.getResources(), Integer.parseInt(imageView.getTag().toString()), 100, 100));	  
		}
	}

	public void excluiViewSelecionada(Context context, final FrameLayout frameLayout, ArrayList<Integer> arrayList_itens) {
		
		if (frameLayout.getChildCount() > 0) {

			arrayList_itens.clear();

			mudaDeImagensParaNumeros(context, frameLayout, arrayList_itens);

			ordenaLista(arrayList_itens);
		
			mostraAlertDialog(context, frameLayout, arrayList_itens, "Deletar");
		}
	}

}
