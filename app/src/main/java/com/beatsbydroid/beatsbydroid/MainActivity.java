package com.beatsbydroid.beatsbydroid;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

        final View.OnTouchListener btnTouchListener = new View.OnTouchListener() {
            float dX = 0, dY = 0;
            float startX = 0, startY = 0, endX = 0, endY = 0;
            boolean shouldClick;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        startX = view.getX();
                        startY = view.getY();
                        shouldClick = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        endX = view.getX();
                        endY = view.getY();
                        if (Math.abs(endX - startX) > 5 || Math.abs(endY - startY) > 5)
                            shouldClick = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (shouldClick)
                            view.performClick();
                        break;
                }
                return true;
            }
        };

        final View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((RelativeLayout)view.getParent()).removeView(view);
                    }
                });

                alertDialog.show();

            }
        };

        layoutPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mode.equals("build")) {
                    double x = motionEvent.getX();
                    double y = motionEvent.getY();
                    Button button = new Button(MainActivity.this);
                    button.setOnClickListener(btnClickListener);
                    button.setOnTouchListener(btnTouchListener);

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
