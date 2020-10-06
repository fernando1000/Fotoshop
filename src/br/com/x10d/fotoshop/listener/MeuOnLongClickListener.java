package br.com.x10d.fotoshop.listener;

import android.content.ClipData;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;

public class MeuOnLongClickListener implements OnLongClickListener {
	@Override
	public boolean onLongClick(View view_imageViewQseraArrastada) {
		
		ClipData clipData = ClipData.newPlainText("simple_text", "text");
		
		DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view_imageViewQseraArrastada);//findViewById(R.id.shadow)
		
		view_imageViewQseraArrastada.startDrag(clipData, dragShadowBuilder, view_imageViewQseraArrastada, 0);
		//view_imageViewQseraArrastada.setVisibility(View.INVISIBLE);
		
		return(true);
	}
}
