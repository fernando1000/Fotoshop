package mobile.contratodigital.fotoshop.listener;

import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.FrameLayout;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import br.com.extend.fotoshop.R;
import mobile.contratodigital.fotoshop.util.StatusMemoria;
import mobile.contratodigital.fotoshop.util.TelaBuilder;

	public class MeuOnDragListener implements OnDragListener {
	
	//private int numero;
	private Context context;
	
	public MeuOnDragListener(Context _context){//int num, 
		super();
		//this.numero = num;
		this.context = _context;
	}
	
	@Override
	public boolean onDrag(View view_queRecebeImageView, DragEvent dragEvent) {
		
		int acaoDoDragEvent = dragEvent.getAction();
		
		switch(acaoDoDragEvent){
		
			case DragEvent.ACTION_DRAG_STARTED:
				if(dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
					return(true);
				}
				return(false);
				
			case DragEvent.ACTION_DRAG_ENTERED:
				//view_onDrag.setBackgroundColor(Color.YELLOW);
				break;
				
			case DragEvent.ACTION_DRAG_LOCATION:
				//Log.i("Script", numero+" - ACTION_DRAG_LOCATION");
				break;
				
			case DragEvent.ACTION_DRAG_EXITED:
				//view_onDrag.setBackgroundColor(Color.BLUE);
				break;
				
			case DragEvent.ACTION_DROP:
				trataAcaoDeSoltarAImageView(view_queRecebeImageView, dragEvent);	
				break;
				
			case DragEvent.ACTION_DRAG_ENDED:
				//viewOnDrag.setBackgroundColor(Color.BLUE);
				break;
		}
		return true;
	}
	
	private void trataAcaoDeSoltarAImageView(View view_queRecebeImageView, DragEvent dragEvent){
		
		View view_QfoiArrastada = (View) dragEvent.getLocalState();
				
		ViewGroup viewGroup = (ViewGroup) view_QfoiArrastada.getParent();		
				  viewGroup.removeView(view_QfoiArrastada);
				 
		FrameLayout frameLayout_tela = (FrameLayout) view_queRecebeImageView;
	
		ImageView imageView_QfoiArrastada = (ImageView) view_QfoiArrastada;
				  imageView_QfoiArrastada.setOnLongClickListener(null);
				  imageView_QfoiArrastada.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				  imageView_QfoiArrastada.setOnTouchListener(new MeuOnTouchListener());  
				  imageView_QfoiArrastada.setScaleType(ImageView.ScaleType.MATRIX);
				  imageView_QfoiArrastada.setTag(R.id.numero, frameLayout_tela.getChildCount());
									  
		frameLayout_tela.addView(imageView_QfoiArrastada);
		
		if(viewGroup.getTag().equals("ll_equipamento")){
			
			criaNovoImageViewParaReporOqueFoiRemovidoDoLinearLayout_paleta(imageView_QfoiArrastada, viewGroup);
		}		
	}
	
	private void criaNovoImageViewParaReporOqueFoiRemovidoDoLinearLayout_paleta(ImageView imageView_QfoiArrastada, ViewGroup viewGroup_ll_equipamento){

		TelaBuilder widgetBuilder = new TelaBuilder(context);

		int idDaImageViewQfoiArrastada = Integer.parseInt(imageView_QfoiArrastada.getTag().toString());
		
		viewGroup_ll_equipamento.addView(widgetBuilder.criaImageView(idDaImageViewQfoiArrastada, viewGroup_ll_equipamento.getWidth()));		
	   	
	} 
	
}
