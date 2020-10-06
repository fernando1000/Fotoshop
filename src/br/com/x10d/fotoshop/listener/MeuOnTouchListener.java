package br.com.x10d.fotoshop.listener;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MeuOnTouchListener implements OnTouchListener{

    // Essas matrizes serao usadas para mover e dar zoom na imagem:
    private Matrix matrix = new Matrix();
    private Matrix matrix_saved = new Matrix();
    
    // podemos estar em 3 estados possiveis, sao eles:
    private static final int INT_ESTADO_NENHUM = 0;
    private static final int INT_ESTADO_DRAG = 1;
    private static final int INT_ESTADO_ZOOM = 2;
    private int int_estadoMode = INT_ESTADO_NENHUM;
    
    // variaveis para utilizacao do zoom:
    private PointF pointF_start = new PointF();
    private PointF pointF_medio = new PointF();
    private float float_distanciaAntiga = 1f;
    private float float_distancia = 0f;
    private float float_newRot = 0f;
    private float[] floatArray_lastEvent = null;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
    		
    	ImageView imageView = (ImageView) view;
    	
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
        
            case MotionEvent.ACTION_DOWN:
                matrix_saved.set(matrix);
                pointF_start.set(motionEvent.getX(), motionEvent.getY());
                int_estadoMode = INT_ESTADO_DRAG;
                floatArray_lastEvent = null;
                break;
                
            case MotionEvent.ACTION_POINTER_DOWN:
                float_distanciaAntiga = espacamentoEntreOsDoisDedos(motionEvent);
                if (float_distanciaAntiga > 10f) {
                    matrix_saved.set(matrix);
                    pontoMedioEntreDoisDedos(pointF_medio, motionEvent);
                    int_estadoMode = INT_ESTADO_ZOOM;
                }
                floatArray_lastEvent = new float[4];
                floatArray_lastEvent[0] = motionEvent.getX(0);
                floatArray_lastEvent[1] = motionEvent.getX(1);
                floatArray_lastEvent[2] = motionEvent.getY(0);
                floatArray_lastEvent[3] = motionEvent.getY(1);
                float_distancia = rotacao(motionEvent);
                break;
                
            case MotionEvent.ACTION_UP:
            	
            case MotionEvent.ACTION_POINTER_UP:
                int_estadoMode = INT_ESTADO_NENHUM;
                floatArray_lastEvent = null;
                break;
                
            case MotionEvent.ACTION_MOVE:
                if (int_estadoMode == INT_ESTADO_DRAG) {
                    matrix.set(matrix_saved);
                    float dx = motionEvent.getX() - pointF_start.x;
                    float dy = motionEvent.getY() - pointF_start.y;
                    matrix.postTranslate(dx, dy);
                } 
                else if (int_estadoMode == INT_ESTADO_ZOOM) {
                    float newDist = espacamentoEntreOsDoisDedos(motionEvent);
                    if (newDist > 10f) {
                        matrix.set(matrix_saved);
                        float scale = (newDist / float_distanciaAntiga);
                        matrix.postScale(scale, scale, pointF_medio.x, pointF_medio.y);
                    }
                    if (floatArray_lastEvent != null && motionEvent.getPointerCount() == 3) {
                        float_newRot = rotacao(motionEvent);
                        float r = float_newRot - float_distancia;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (imageView.getWidth() / 2) * sx;
                        float yc = (imageView.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }

        imageView.setImageMatrix(matrix);
        return true;
    }
	
    private float espacamentoEntreOsDoisDedos(MotionEvent motionEvent) {
    	
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
 
    private void pontoMedioEntreDoisDedos(PointF pointF, MotionEvent motionEvent) {
    	
        float x = motionEvent.getX(0) + motionEvent.getX(1);
        float y = motionEvent.getY(0) + motionEvent.getY(1);
        pointF.set(x / 2, y / 2);
    }

    private float rotacao(MotionEvent motionEvent) {
    	
        double delta_x = (motionEvent.getX(0) - motionEvent.getX(1));
        double delta_y = (motionEvent.getY(0) - motionEvent.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

}
