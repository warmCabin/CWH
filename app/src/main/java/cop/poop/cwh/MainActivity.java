package cop.poop.cwh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickButton(View view){
        System.out.println("What did I JUST say.");
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

}
