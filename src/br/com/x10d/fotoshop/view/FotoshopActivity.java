package br.com.x10d.fotoshop.view;

import java.util.ArrayList;
import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import br.com.x10d.fotoshop.R;
import br.com.x10d.fotoshop.listener.MeuOnDragListener;
import br.com.x10d.fotoshop.util.DiminuiMBimagens;
import br.com.x10d.fotoshop.util.Equipamento;
import br.com.x10d.fotoshop.util.ListaComTodasImageViews;
import br.com.x10d.fotoshop.util.TelaBuilder;
import br.com.x10d.fotoshop.util.TrabalhaComFotos;
import br.com.x10d.fotoshop.util.Utilitario;

public class FotoshopActivity extends Activity {

	public static final int REQUISICAO_PERMISSAO_TIRAR_FOTO = 111;
	public static final int REQUISICAO_PERMISSAO_LEITURA = 222;
	public static final int REQUISICAO_PERMISSAO_ESCRITA = 333;
	
	private Context context;
	private TelaBuilder widgetBuilder;
	private LinearLayout ll_principal;
		private ScrollView scrollView_paleta;
			private LinearLayout linearLayout_paleta;
		private LinearLayout linearLayout_tela;
			private FrameLayout frameLayout_tela;
		
	private ArrayList<Integer> arrayList_itens = new ArrayList<Integer>();
	private Utilitario utilitario;		
	private String nomeEmpresa_cnpj;		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		context = FotoshopActivity.this;

		utilitario = new Utilitario();
		
		setContentView(constroiTelaInicial());	
		
		ActionBar actionBar = getActionBar();
				  actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.color.azul_consigaz))));
		
		Intent intent = getIntent();
	
		if (intent.hasExtra("nomeEmpresa_cnpj")) {
			
		        			   nomeEmpresa_cnpj = intent.getStringExtra("nomeEmpresa_cnpj");
		    actionBar.setTitle(nomeEmpresa_cnpj);
		}else{
							   nomeEmpresa_cnpj = "Nome da empresa não informado";
	        actionBar.setTitle(nomeEmpresa_cnpj);
		}
		
	}

	private LinearLayout constroiTelaInicial() {
		
		widgetBuilder = new TelaBuilder(context);
		
		LinearLayout.LayoutParams params_MATCH_MATCH = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams params_MATCH_MATCH_90 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.90f);
		LinearLayout.LayoutParams params_MATCH_MATCH_10 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.10f);
		FrameLayout.LayoutParams frameParams_MATCH_MATCH = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		   	
		ll_principal = widgetBuilder.criaLinearLayout(LinearLayout.HORIZONTAL, params_MATCH_MATCH, 0);
		//ll_principal.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_paleta ));	
			
							scrollView_paleta = widgetBuilder.criaScrollView(params_MATCH_MATCH_90);
							
									   				   linearLayout_paleta = widgetBuilder.criaLinearLayout(LinearLayout.VERTICAL, params_MATCH_MATCH, 0); 
									   				   linearLayout_paleta.setTag("paleta");
									   				   linearLayout_paleta = criaImageViewsEAdicionaNaPaleta(linearLayout_paleta);		
		 					 scrollView_paleta.addView(linearLayout_paleta);
		ll_principal.addView(scrollView_paleta);
	
							 linearLayout_tela = widgetBuilder.criaLinearLayout(LinearLayout.VERTICAL, params_MATCH_MATCH_10, Color.parseColor(getString(R.color.wheat)));
							 //linearLayout_tela.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.camera));	  	
							 
							 						   frameLayout_tela = widgetBuilder.criaFrameLayout(frameParams_MATCH_MATCH, 0);
							 						   frameLayout_tela.setOnDragListener(new MeuOnDragListener(context));
							 						   frameLayout_tela.setTag("tela");
							 linearLayout_tela.addView(frameLayout_tela);
		ll_principal.addView(linearLayout_tela);

		return ll_principal;
	}
	
	private LinearLayout criaImageViewsEAdicionaNaPaleta(final LinearLayout linearLayout_paleta) {
			
		//este metodo espera o termino da criacao do layout para soh entao pegar os paramentros
		ViewTreeObserver vto = linearLayout_paleta.getViewTreeObserver(); 
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		    	
		        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
		        	
		        	linearLayout_paleta.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        }else {
		        	linearLayout_paleta.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		        }
		       
			   int qtd = 1;
			   int cor = 0;
			   int linearLayout_paleta_width  = linearLayout_paleta.getMeasuredWidth();       
			   int height = linearLayout_paleta_width + 25;
			   LinearLayout.LayoutParams lp_retanguloEmPeh = new LinearLayout.LayoutParams(linearLayout_paleta_width, height);
			   

		       for (Equipamento equipamento : ListaComTodasImageViews.pegaListaDeEquipamentos()) {
		    	   
		    	   //se for par
		    	   if(qtd %2 == 0){
		    		   cor = Color.parseColor(getString(R.color.azul_celeste));
		    	   }else{
		    		   cor = Color.parseColor(getString(R.color.aquamarine));
		    	   }
		    	   
		    	   LinearLayout ll_equipamento = widgetBuilder.criaLinearLayout(LinearLayout.VERTICAL, lp_retanguloEmPeh, cor);
		    	   				ll_equipamento.setTag("ll_equipamento");
		    	   				ll_equipamento.addView(widgetBuilder.criaTextView(equipamento.getNome()));
		    	   				ll_equipamento.addView(widgetBuilder.criaImageView(equipamento.getImagem(), linearLayout_paleta_width));	
		    	   				
		    	   linearLayout_paleta.addView(ll_equipamento);
		    	   
		    	   qtd++;
		       }   
		       
				
		    } 
		});
			
		return linearLayout_paleta;
	}
				
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "Tirar Foto");
		menu.add(0, 1, 0, "Buscar Foto");
		menu.add(0, 2, 0, "Limpar Tela");
		menu.add(0, 3, 0, "Salvar Foto");
		menu.add(0, 4, 0, "Excluir");
		menu.add(0, 5, 0, "Editar");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			tentaTirarFoto();
			return true;

		case 1:
			tentaBuscarFoto();
			return true;

		case 2:
			limparTela();
			return true;

		case 3:
			tentaSalvarFoto();
			return true;
	
		case 4:
			excluir();
			return true;
	
		case 5:
			editar();
			return true;	

		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void tentaTirarFoto(){

		if (Build.VERSION.SDK_INT >= 23) {
			
			if (permitiuTirarFoto()) {
				 
				tirarFoto();
			} 	
	    } 
		else {
	    	tirarFoto();
	    }
		   
	}
	
	private void tentaSalvarFoto(){

		if (Build.VERSION.SDK_INT >= 23) {
			
			if (permitiuEscrever()){
				 
				salvarFoto();
			}		
	    } 
		else {
			salvarFoto();
	    }
		   
	}
	
	private void tentaBuscarFoto(){

		if (Build.VERSION.SDK_INT >= 23) {
			
			if (permitiuLer()){
				 
				buscarFoto();
			}		
	    } 
		else {
			buscarFoto();
	    }
		   
	}
	
	private void tirarFoto() {
	
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);				
	   	   
		//startActivityForResult(intent, REQUISICAO_TIRAR_FOTO);	
		
		startActivityForResult(intent, REQUISICAO_PERMISSAO_TIRAR_FOTO);	
		
		
		
	}
	
	private boolean permitiuTirarFoto(){
		
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            
   	     	ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUISICAO_PERMISSAO_TIRAR_FOTO);		      
 		
            return false;
        }

        return true;
    }
	
	private boolean permitiuEscrever(){
        
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUISICAO_PERMISSAO_ESCRITA);		      
 		
            return false;
        }
        return true;
    }
	
	private boolean permitiuLer(){
        
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUISICAO_PERMISSAO_LEITURA);		      
 		
            return false;
        }
        return true;
    }

	
	private void buscarFoto() {
		
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

		startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem:"), REQUISICAO_PERMISSAO_LEITURA);
	}
	
	private void limparTela() {
		
		linearLayout_tela.setBackground(null);
		linearLayout_tela.setBackgroundColor(Color.parseColor(getString(R.color.wheat)));
					
		removeTodasViewsDentroDeUmFrameLayout(linearLayout_tela);
	}
	
	private void removeTodasViewsDentroDeUmFrameLayout(LinearLayout linearLayout_tela){
		
		for (int i = 0; i < linearLayout_tela.getChildCount(); i++) {

			Object child = (Object) linearLayout_tela.getChildAt(i);

			if (child instanceof FrameLayout) {

				FrameLayout frameLayout = (FrameLayout) child;
							frameLayout.removeAllViews();										
			}
		}
	}

	private void salvarFoto() {

		TrabalhaComFotos trabalhaComFotos = new TrabalhaComFotos();
		
		boolean deuErro = trabalhaComFotos.tiraScreenShot(FotoshopActivity.this, linearLayout_tela, nomeEmpresa_cnpj);

		if (deuErro) {
			Toast.makeText(context, "Favor tentar novamente", Toast.LENGTH_SHORT).show();
		} 
		else {
			Toast.makeText(context, "Foto salva!", Toast.LENGTH_SHORT).show();
		
			limparTela();		
		}
		
	}
	
	private void excluir(){

		utilitario.excluiViewSelecionada(context, frameLayout_tela, arrayList_itens);
	}
		
	private void editar(){

		utilitario.editaViewQEstaDentroDeUmFrameLayout(context, frameLayout_tela, arrayList_itens);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
	 	 
		Log.i("tag", "onRequestPermissionsResult requestCode: " + requestCode);
		
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {	
	    	  
    	  if (requestCode == REQUISICAO_PERMISSAO_TIRAR_FOTO) {
	    	  
    		  tirarFoto();  	  	    	
	      }
    	  
    	  if (requestCode == REQUISICAO_PERMISSAO_ESCRITA) {
   
    		  salvarFoto();
    	  }
    	  
    	  if (requestCode == REQUISICAO_PERMISSAO_LEITURA) {
    			  
    		  buscarFoto();		  
    	  }
    	  
	  }	 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		
		Log.i("tag", "onActivityResult requestCode: " + requestCode);
		
		if (resultCode != Activity.RESULT_CANCELED) {

			if (requestCode == REQUISICAO_PERMISSAO_ESCRITA || requestCode == REQUISICAO_PERMISSAO_LEITURA) {
				
				Uri caminhoDaFotoSelecionada = intent.getData();

				TrabalhaComFotos trabalhaComFotos = new TrabalhaComFotos();
				
				String picturePath = trabalhaComFotos.devolveStringPicturePathBaseadoEmCaminhoDaFotoSelecionada(context, caminhoDaFotoSelecionada);
				
				Bitmap bitmap = DiminuiMBimagens.decodeSampledBitmapFromPicturePath(picturePath, 200, 200);
				
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

				linearLayout_tela.setBackgroundDrawable(bitmapDrawable);				
			}

			//if (requestCode == REQUISICAO_TIRAR_FOTO) {
			if (requestCode == REQUISICAO_PERMISSAO_TIRAR_FOTO) {
							
				
				
				
				Bundle dundle = intent.getExtras();
		        
				Bitmap bitmap = (Bitmap) dundle.get("data");
				
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

				linearLayout_tela.setBackgroundDrawable(bitmapDrawable);	
			}
		}
		
	}

		
	/*
	private LinearLayout crialinearLayoutLinhaHolderEAdicionaNaTela(LinearLayout linearLayout_tela, int qtdDeLinhasHolder, int qtdPixelsPorlinha) {

		int de = 0;
		int ateh = 0;
		int valorInicial = 0;

		float tamanhoDeCadaLinhaHolder = ((float) 100 / qtdDeLinhasHolder) / 100;
		float tamanhoDeCadaPixelsPorlinha = ((float) 100 / qtdPixelsPorlinha) / 100;

		LinearLayout.LayoutParams params_linhaHolder = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, tamanhoDeCadaLinhaHolder);

		for (int linha = 1; linha <= qtdDeLinhasHolder; linha++) {

			de = valorInicial + 1;
			ateh = qtdPixelsPorlinha * linha;

			LinearLayout linearLayout_linha1_holder = widgetBuilder.criaLinearLayout(LinearLayout.HORIZONTAL, params_linhaHolder, 0);
						 linearLayout_linha1_holder = criaLinearLayoutPixelEAdicionaNaLinhaHolder(linearLayout_linha1_holder, de, ateh, tamanhoDeCadaPixelsPorlinha);
			linearLayout_tela.addView(linearLayout_linha1_holder);

			valorInicial = valorInicial + qtdPixelsPorlinha;
		}
		return linearLayout_tela;
	}
	*/

	/*
	private LinearLayout criaLinearLayoutPixelEAdicionaNaLinhaHolder(LinearLayout linearLayout_linhaX_holder, int de, int ateh, float tamanhoDeCadaPixelsPorlinha) {

		LinearLayout.LayoutParams params_pixels = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, tamanhoDeCadaPixelsPorlinha);

		for (int i = de; i <= ateh; i++) {

			LinearLayout linearLayout_linhaX_pixelX = widgetBuilder.criaLinearLayout(LinearLayout.HORIZONTAL, params_pixels, 0);
			linearLayout_linhaX_pixelX.setTag("pixel");
			linearLayout_linhaX_pixelX.setOnDragListener(new Meu_OnDragListener(i, context));

			linearLayout_linhaX_holder.addView(linearLayout_linhaX_pixelX);
		}
		return linearLayout_linhaX_holder;
	}
	*/
	
}
