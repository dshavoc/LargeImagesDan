package SignInTiles;

import java.util.Vector;

import com.DNI.largeimagesdan.MainActivity;

import android.content.Context;
import android.graphics.RectF;
import android.view.View;

public class SignInView extends View {
	int screenWidth, screenHeight;
	Vector<LetterTile> letters = new Vector<LetterTile>();
	Vector<RectF> keys = new Vector<RectF>(); 
	public SignInView(Context context) {
		super(context);
		MainActivity main = (MainActivity) context;
		screenWidth = main.screenWidth;
		screenHeight = main.screenHeight;
		// TODO Auto-generated constructor stub
	}
	
	private void createEmptyKeys(){
		int dim = screenWidth/6;
		float startingPoint = screenHeight*.3f;
		float left, top, right, bottom;
		
		for (int i = 0; i <26; i ++){ 
			left = i%6 * dim;
			top  = i/6 * dim + startingPoint;
			right = left + dim;
			bottom = top + dim + startingPoint;
			keys.add(new RectF(left,top,right,bottom));	
		}
		
	}
	
	private void assignLettersToKeys(){
		Vector<LetterTile> shuffledLetters = new Vector<LetterTile>();
	}

}
