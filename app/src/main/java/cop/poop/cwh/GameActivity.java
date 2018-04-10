package cop.poop.cwh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    BoardView bv;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        bv = findViewById(R.id.chessBoard);
        bv.setVisibility(View.INVISIBLE);
        status = findViewById(R.id.statusMessage);
    }

    public void clickSwitch(View view){

        Switch s = (Switch)view;

        if(s.isChecked()){
            bv.setVisibility(View.VISIBLE);
            System.out.println("Enabling chess.");
        }else{
            bv.setVisibility(View.INVISIBLE);
            System.out.println("Disabling chess.");
        }

    }
}
