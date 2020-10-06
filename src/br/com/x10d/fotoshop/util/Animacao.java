package br.com.x10d.fotoshop.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Animacao {

	public void iniciaAnimacao(Context context, ImageView imageView, int animacaoDesejada) {

		Animation animation = AnimationUtils.loadAnimation(context, animacaoDesejada);
				  animation.reset();

		imageView.clearAnimation();
		imageView.startAnimation(animation);
	}

}
