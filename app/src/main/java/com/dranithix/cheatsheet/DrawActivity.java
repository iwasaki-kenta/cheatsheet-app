package com.dranithix.cheatsheet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.*;

import android.widget.ImageView;
import butterknife.OnLongClick;
import com.dranithix.cheatsheet.model.Stroke;
import com.dranithix.cheatsheet.model.StrokeSerializer;
import com.thebluealliance.spectrum.SpectrumDialog;
import com.wacom.ink.manipulation.Intersector;
import com.wacom.ink.path.PathBuilder;
import com.wacom.ink.path.PathUtils;
import com.wacom.ink.path.SpeedPathBuilder;
import com.wacom.ink.penid.PenRecognizer;
import com.wacom.ink.rasterization.BlendMode;
import com.wacom.ink.rasterization.InkCanvas;
import com.wacom.ink.rasterization.Layer;
import com.wacom.ink.rasterization.SolidColorBrush;
import com.wacom.ink.rasterization.StrokePaint;
import com.wacom.ink.rasterization.StrokeRenderer;
import com.wacom.ink.rendering.EGLRenderingContext;
import com.wacom.ink.smooth.MultiChannelSmoothener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wacom.ink.path.PathUtils.*;

public class DrawActivity extends AppCompatActivity {

    private InkCanvas inkCanvas;
    private Layer viewLayer;
    private SpeedPathBuilder pathBuilder;
    private StrokePaint paint;
    private SolidColorBrush brush;
    private MultiChannelSmoothener smoothener;
    private int pathStride;
    private StrokeRenderer strokeRenderer;
    private Layer strokesLayer;
    private Layer currentFrameLayer;
    private StrokeSerializer serializer;
    private Intersector<Stroke> intersector;

    private LinkedList<Stroke> strokesList = new LinkedList<>();
    private LinkedList<ClipboardEvent> undoStrokesList = new LinkedList<>();
    private boolean drawing = true;


    @Bind(R.id.noteView)
    SurfaceView notesView;

    @OnClick(R.id.undo)
    public void undo() {
        undoDraw();
    }

    @OnClick(R.id.redo)
    public void redo() {
        redoDraw();
    }

    @OnClick(R.id.eraser)
    public void erase() {
        toggleEraser();
    }

    @OnLongClick(R.id.eraser)
    public boolean eraseAll() {
        confirmEraseAll();
        return true;
    }

    @OnClick(R.id.palette)
    public void palette() {
        showPalette();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        ButterKnife.bind(this);

        pathBuilder = new SpeedPathBuilder();
        pathBuilder.setNormalizationConfig(100.0f, 4000.0f);
        pathBuilder.setMovementThreshold(2.0f);
        pathBuilder.setPropertyConfig(PathBuilder.PropertyName.Width, 5f, 10f, Float.NaN, Float.NaN, PathBuilder.PropertyFunction.Power, 1.0f, false);
        pathStride = pathBuilder.getStride();


        notesView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (inkCanvas != null && !inkCanvas.isDisposed()) {
                    releaseResources();
                }

                inkCanvas = InkCanvas.create(holder, new EGLRenderingContext.EGLConfiguration());

                viewLayer = inkCanvas.createViewLayer(width, height);
                strokesLayer = inkCanvas.createLayer(width, height);
                currentFrameLayer = inkCanvas.createLayer(width, height);

                inkCanvas.clearLayer(currentFrameLayer, Color.WHITE);

                brush = new SolidColorBrush();

                paint = new StrokePaint();
                paint.setStrokeBrush(brush);    // Solid color brush.
                paint.setColor(Color.parseColor(getResources().getStringArray(R.array.colors)[0]));
                paint.setWidth(Float.NaN);        // Expected variable width.

                smoothener = new MultiChannelSmoothener(pathStride);
                smoothener.enableChannel(2);

                strokeRenderer = new StrokeRenderer(inkCanvas, paint, pathStride, width, height);

                serializer = new StrokeSerializer();
                intersector = new Intersector<Stroke>();

                loadStrokes();
                drawStrokes();
                renderView();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseResources();
            }
        });

        notesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (strokesList.size() > 0 && !drawing) {
                    buildPath(event);
                    intersector.setTargetAsStroke(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), pathStride);
                    LinkedList<Stroke> removedStrokes = new LinkedList<Stroke>();
                    for (Stroke stroke : strokesList) {
                        if (intersector.isIntersectingTarget(stroke)) {
                            removedStrokes.add(stroke);
                        }
                    }
                    for (Stroke stroke : removedStrokes) {
                        undoStrokesList.addLast(new ClipboardEvent(stroke, true));
                    }
                    strokesList.removeAll(removedStrokes);
                    drawStrokes();
                    renderView();
                } else if (drawing) {
                    boolean bFinished = buildPath(event);
                    drawStroke(event);
                    renderView();
                    if (bFinished) {
                        Stroke stroke = new Stroke();
                        stroke.copyPoints(pathBuilder.getPathBuffer(), 0, pathBuilder.getPathSize());
                        stroke.setStride(pathBuilder.getStride());
                        stroke.setWidth(Float.NaN);
                        stroke.setColor(paint.getColor());
                        stroke.setInterval(0.0f, 1.0f);
                        stroke.setBlendMode(BlendMode.BLENDMODE_NORMAL);
                        stroke.calculateBounds();
                        strokesList.add(stroke);
                        undoStrokesList.clear();

                    }
                }
                saveStrokes();


                return true;
            }
        });
    }

    private void drawStrokes() {
        inkCanvas.setTarget(strokesLayer);
        inkCanvas.clearColor();

        for (Stroke stroke : strokesList) {
            paint.setColor(stroke.getColor());
            strokeRenderer.setStrokePaint(paint);
            strokeRenderer.drawPoints(stroke.getPoints(), 0, stroke.getSize(), stroke.getStartValue(), stroke.getEndValue(), true);
            strokeRenderer.blendStroke(strokesLayer, stroke.getBlendMode());
        }

        inkCanvas.setTarget(currentFrameLayer);
        inkCanvas.clearColor(Color.WHITE);
        inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
    }

    private void renderView() {
        inkCanvas.setTarget(viewLayer);
        // Copy the current frame layer in the view layer to present it on the screen.
        inkCanvas.drawLayer(currentFrameLayer, BlendMode.BLENDMODE_OVERWRITE);
        inkCanvas.invalidate();
    }

    private boolean buildPath(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN
                && event.getAction() != MotionEvent.ACTION_MOVE
                && event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Reset the smoothener instance when starting to generate a new path.
            smoothener.reset();
        }

        Phase phase = PathUtils.getPhaseFromMotionEvent(event);
        // Add the current input point to the path builder
        FloatBuffer part = pathBuilder.addPoint(phase, event.getX(), event.getY(), event.getEventTime());
        MultiChannelSmoothener.SmoothingResult smoothingResult;
        int partSize = pathBuilder.getPathPartSize();

        if (partSize > 0) {
            // Smooth the returned control points (aka path part).
            smoothingResult = smoothener.smooth(part, partSize, (phase == Phase.END));
            // Add the smoothed control points to the path builder.
            pathBuilder.addPathPart(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
        }

        // Create a preliminary path.
        FloatBuffer preliminaryPath = pathBuilder.createPreliminaryPath();
        // Smoothen the preliminary path's control points (return inform of a path part).
        smoothingResult = smoothener.smooth(preliminaryPath, pathBuilder.getPreliminaryPathSize(), true);
        // Add the smoothed preliminary path to the path builder.
        pathBuilder.finishPreliminaryPath(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());

        return (event.getAction() == MotionEvent.ACTION_UP && pathBuilder.hasFinished());
    }


    protected void loadStrokes() {
        try {
            strokesList = serializer.deserialize(new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/will.bin")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void saveStrokes() {
        serializer.serialize(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/will.bin")), strokesList);
    }

    private void drawStroke(MotionEvent event) {

        strokeRenderer.drawPoints(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), event.getAction() == MotionEvent.ACTION_UP);
        strokeRenderer.drawPrelimPoints(pathBuilder.getPreliminaryPathBuffer(), 0, pathBuilder.getFinishedPreliminaryPathSize());

        if (event.getAction() != MotionEvent.ACTION_UP) {
            inkCanvas.setTarget(currentFrameLayer, strokeRenderer.getStrokeUpdatedArea());
            inkCanvas.clearColor(Color.WHITE);
            inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
            strokeRenderer.blendStrokeUpdatedArea(currentFrameLayer, BlendMode.BLENDMODE_NORMAL);
        } else {
            strokeRenderer.blendStroke(strokesLayer, BlendMode.BLENDMODE_NORMAL);
            inkCanvas.setTarget(currentFrameLayer);
            inkCanvas.clearColor(Color.WHITE);
            inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
        }
    }

    private void undoDraw() {
        if (strokesList.size() != 0) {
            if (undoStrokesList.size() != 0 && undoStrokesList.getLast().isDelete()) {
                strokesList.add(undoStrokesList.removeLast().getStroke());
            } else {
                undoStrokesList.addLast(new ClipboardEvent(strokesList.removeLast(), false));
            }
            drawStrokes();
            renderView();
        }
    }

    @OnClick(R.id.palette)
    public void chooseColor() {
        String[] hexColors = getResources().getStringArray(R.array.colors);
        int[] colors = new int[hexColors.length];
        for (int i = 0; i < hexColors.length; i++) {
            colors[i] = Color.parseColor(hexColors[i]);
        }
        new SpectrumDialog.Builder(this).setSelectedColor(paint.getColor()).setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                if (positiveResult) {
                    paint.setColor(color);
                    strokeRenderer.setStrokePaint(paint);

                }
            }
        })
                .setColors(colors).build().show(getSupportFragmentManager(), "Choose Color");


    }

    private void redoDraw() {
        if (undoStrokesList.size() != 0) {
            strokesList.addLast(undoStrokesList.removeLast().getStroke());
            drawStrokes();
            renderView();
        }
    }

    private void toggleEraser() {
        if (drawing) {
            ImageView pencil = (ImageView) findViewById(R.id.eraser);
            pencil.setImageResource(R.drawable.pencil);
        } else {
            ImageView eraser = (ImageView) findViewById(R.id.eraser);
            eraser.setImageResource(R.drawable.eraser);
        }
        drawing = !drawing;
    }

    private void confirmEraseAll() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to clear the screen?\n\nThis action cannot be undone.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                strokesList.clear();
                undoStrokesList.clear();
                drawStrokes();
                renderView();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showPalette() {

    }

    private static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    private void releaseResources() {
        strokeRenderer.dispose();
        inkCanvas.dispose();
    }

    class ClipboardEvent {
        Stroke stroke;
        boolean delete = false;

        public ClipboardEvent(Stroke stroke, boolean delete) {
            this.stroke = stroke;
            this.delete = delete;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public Stroke getStroke() {
            return stroke;
        }

        public void setStroke(Stroke stroke) {
            this.stroke = stroke;
        }
    }
}
