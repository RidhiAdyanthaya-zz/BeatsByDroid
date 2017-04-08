package com.beatsbydroid.beatsbydroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String mode;
    Button btnBuild, btnPlay;
    RelativeLayout layoutPad;

    final int btnMinSize = 100;
    final int btnMaxSize = 300;

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
                        if (event.getRawX() + dX < 0) {
                            endX = 0;
                        } else if (event.getRawX() + dX + view.getLayoutParams().width > layoutPad.getWidth()) {
                            endX = layoutPad.getWidth() - view.getLayoutParams().width;
                        } else {
                            endX = event.getRawX() + dX;
                        }
                        if (event.getRawY() + dY < 0) {
                            endY = 0;
                        } else if (event.getRawY() + dY + view.getLayoutParams().height > layoutPad.getHeight()) {
                            endY = layoutPad.getHeight() - view.getLayoutParams().height;
                        } else {
                            endY = event.getRawY() + dY;
                        }
                        view.animate()
                                .x(endX)
                                .y(endY)
                                .setDuration(0)
                                .start();
                        /*view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        endX = view.getX();
                        endY = view.getY();*/
                        System.out.println(endX + "," +endY + "," + (event.getRawX() + dX));

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
                if (mode.equals("build")) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_layout);
                    Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete);
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RelativeLayout)view.getParent()).removeView(view);
                            dialog.dismiss();
                        }
                    });
                    final Button btnExample = new Button(dialog.getContext());
                    final RelativeLayout exampleLayout = (RelativeLayout) dialog.findViewById(R.id.exampleLayout);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(view.getWidth(), view.getHeight());
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    exampleLayout.addView(btnExample, params);
                    SeekBar heightBar = (SeekBar) dialog.findViewById(R.id.heightBar);
                    SeekBar widthBar = (SeekBar) dialog.findViewById(R.id.widthBar);
                    heightBar.setProgress((view.getHeight() - btnMinSize) / 2);
                    widthBar.setProgress((view.getHeight() - btnMinSize) / 2);

                    heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            RelativeLayout.LayoutParams exampleParams = new RelativeLayout.LayoutParams(btnExample.getLayoutParams());
                            exampleParams.height = btnMinSize + i * 2;
                            exampleParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            btnExample.setLayoutParams(exampleParams);

                            int topMargin = ((RelativeLayout.LayoutParams)view.getLayoutParams()).topMargin;
                            int leftMargin = ((RelativeLayout.LayoutParams)view.getLayoutParams()).leftMargin;
                            RelativeLayout.LayoutParams liveParams = new RelativeLayout.LayoutParams(view.getLayoutParams());
                            liveParams.height = btnMinSize + i * 2;
                            liveParams.topMargin = topMargin;
                            liveParams.leftMargin = leftMargin;
                            view.setLayoutParams(liveParams);
                            System.out.println(liveParams.width + ", " + liveParams.height);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    widthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            RelativeLayout.LayoutParams exampleParams = new RelativeLayout.LayoutParams(btnExample.getLayoutParams());
                            exampleParams.width = btnMinSize + i * 2;
                            exampleParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            btnExample.setLayoutParams(exampleParams);

                            int topMargin = ((RelativeLayout.LayoutParams)view.getLayoutParams()).topMargin;
                            int leftMargin = ((RelativeLayout.LayoutParams)view.getLayoutParams()).leftMargin;
                            RelativeLayout.LayoutParams liveParams = new RelativeLayout.LayoutParams(view.getLayoutParams());
                            liveParams.width = btnMinSize + i * 2;
                            liveParams.topMargin = topMargin;
                            liveParams.leftMargin = leftMargin;
                            view.setLayoutParams(liveParams);
                            System.out.println(liveParams.width + ", " + liveParams.height);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });


                    final Spinner colorSpinner = (Spinner) dialog.findViewById(R.id.colorSpinner);
                    colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                            switch (colorSpinner.getSelectedItem().toString()) {
                                case "White":
                                    view.setBackgroundColor(Color.WHITE);
                                    btnExample.setBackgroundColor(Color.WHITE);
                                    break;
                                case "Blue":
                                    view.setBackgroundColor(Color.BLUE);
                                    btnExample.setBackgroundColor(Color.BLUE);
                                    break;
                                case "Red":
                                    view.setBackgroundColor(Color.RED);
                                    btnExample.setBackgroundColor(Color.RED);
                                    break;
                                case "Green":
                                    view.setBackgroundColor(Color.GREEN);
                                    btnExample.setBackgroundColor(Color.GREEN);
                                    break;
                                case "Yellow":
                                    view.setBackgroundColor(Color.YELLOW);
                                    btnExample.setBackgroundColor(Color.YELLOW);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    dialog.show();

                }

                else {
                    //when mode is in "play"
                }
            }

        };

        layoutPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mode.equals("build")) {
                    double x = motionEvent.getX();
                    double y = motionEvent.getY();
                    Button button = new Button(MainActivity.this);
                    button.setBackgroundColor(Color.WHITE);
                    button.setOnClickListener(btnClickListener);
                    button.setOnTouchListener(btnTouchListener);

                    RelativeLayout.LayoutParams params = new
                            RelativeLayout.LayoutParams((btnMinSize + btnMaxSize) / 2, (btnMinSize + btnMaxSize) / 2);

                    if (x + params.width > layoutPad.getWidth()) {
                        params.leftMargin = layoutPad.getWidth() - params.width;
                    }
                    else {
                        params.leftMargin = (int) x;
                    }

                    if (y + params.height > layoutPad.getHeight()) {
                        params.topMargin = layoutPad.getHeight() - params.height;
                    }
                    else {
                        params.topMargin = (int) y;
                    }

                    layoutPad.addView(button, params);
                }

                else {
                    //when mode is in play, this should do nothing
                }

                return false;
            }
        });
    }
}
