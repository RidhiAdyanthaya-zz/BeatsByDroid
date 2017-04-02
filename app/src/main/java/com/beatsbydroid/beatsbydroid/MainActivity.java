package com.beatsbydroid.beatsbydroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String mode;
    Button btnBuild, btnPlay;
    RelativeLayout layoutPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode = "build";

        btnBuild = (Button) findViewById(R.id.btnBuild);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        layoutPad = (RelativeLayout) findViewById(R.id.layoutPad);

        View.OnClickListener modeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = ((Button) view).getText().toString().toLowerCase();
                Toast.makeText(MainActivity.this, mode, Toast.LENGTH_SHORT).show();
            }
        };

        btnBuild.setOnClickListener(modeListener);
        btnPlay.setOnClickListener(modeListener);

        layoutPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mode.equals("build")) {
                    double x = motionEvent.getX();
                    double y = motionEvent.getY();
                    Button button = new Button(MainActivity.this);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((RelativeLayout)view.getParent()).removeView(view);
                        }
                    });

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = (int) x;
                    params.topMargin = (int) y;

                    layoutPad.addView(button, params);
                    
                    System.out.println(layoutPad.getChildCount());
                }

                return false;
            }
        });
    }
}
