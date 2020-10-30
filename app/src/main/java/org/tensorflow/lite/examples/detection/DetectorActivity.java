/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.camera2.CameraCharacteristics;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.tensorflow.lite.examples.detection.response.RegistrationResponse;
import org.tensorflow.lite.examples.detection.customview.OverlayView;
import org.tensorflow.lite.examples.detection.customview.OverlayView.DrawCallback;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;
import org.tensorflow.lite.examples.detection.tflite.SimilarityClassifier;
import org.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;
import org.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
  private static final Logger LOGGER = new Logger();


  // FaceNet
//  private static final int TF_OD_API_INPUT_SIZE = 160;
//  private static final boolean TF_OD_API_IS_QUANTIZED = false;
//  private static final String TF_OD_API_MODEL_FILE = "facenet.tflite";
//  //private static final String TF_OD_API_MODEL_FILE = "facenet_hiroki.tflite";

  // MobileFaceNet
  private static final int TF_OD_API_INPUT_SIZE = 112;
  private static final boolean TF_OD_API_IS_QUANTIZED = false;
  private static final String TF_OD_API_MODEL_FILE = "mobile_face_net.tflite";


  private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt";

  private static final DetectorMode MODE = DetectorMode.TF_OD_API;
  // Minimum detection confidence to track a detection.
  private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
  private static final boolean MAINTAIN_ASPECT = false;

  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  //private static final int CROP_SIZE = 320;
  //private static final Size CROP_SIZE = new Size(320, 320);


  private static final boolean SAVE_PREVIEW_BITMAP = false;
  private static final float TEXT_SIZE_DIP = 10;
  OverlayView trackingOverlay;
  private Integer sensorOrientation;

  private SimilarityClassifier detector;

  private long lastProcessingTimeMs;
  private Bitmap rgbFrameBitmap = null;
  private Bitmap croppedBitmap = null;
  private Bitmap cropCopyBitmap = null;

  private boolean computingDetection = false;
  private boolean addPending = false;
  //private boolean adding = false;

  private long timestamp = 0;

  private Matrix frameToCropTransform;
  private Matrix cropToFrameTransform;
  //private Matrix cropToPortraitTransform;

  private MultiBoxTracker tracker;

  private BorderedText borderedText;

  // Face detector
  private FaceDetector faceDetector;

  // here the preview image is drawn in portrait way
  private Bitmap portraitBmp = null;
  // here the face is cropped and drawn
  private Bitmap faceBmp = null;

  private FloatingActionButton fabAdd;

  //private HashMap<String, Classifier.Recognition> knownFaces = new HashMap<>();
  private boolean isRegistration = false;
  private String currentRegisterId = "";
  private String currentFaceData = "";
  private boolean isTeacherRegistration = false;
  private boolean isForLogIn = false;
  private boolean isCheckIn = false;
  private int THRESHOLD_FOR_ACCEPTING_RESULT = 3;
  private int THRESHOLD_FOR_DENYING_RESULT = 3;
  private int numOfTimeRecognized = 0;
  private int numOfTimeNotRecognized = 0;
  private int MINIMUM_WIDTH_FACE_SIZE_TO_PROCESS_PROM_MLKIT = 150;
  private int MINIMUM_HEIGHT_FACE_SIZE_TO_PROCESS_PROM_MLKIT = 150;
  private final float THRESHOLD_FOR_ACCEPT_DISTANCE = 0.8f;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    fabAdd = findViewById(R.id.fab_add);
    fabAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onAddClick();
      }
    });

    // Real-time contour detection of multiple faces
    FaceDetectorOptions options =
            new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                    .build();


    FaceDetector detector = FaceDetection.getClient(options);

    faceDetector = detector;

  }

  @SuppressLint("RestrictedApi")
  @Override
  public synchronized void onResume() {
    super.onResume();

    numOfTimeRecognized = 0;
    numOfTimeNotRecognized = 0;
    Intent intent = getIntent();
    boolean mode = intent.getBooleanExtra("Mode", false);
    currentRegisterId = intent.getStringExtra("qrData");
    currentFaceData = intent.getStringExtra("faceData");
    isTeacherRegistration = intent.getBooleanExtra("registerMode", false);
    isForLogIn = intent.getBooleanExtra("isForLogIn", false);
    isCheckIn = intent.getBooleanExtra("isCheckIn", false);
    isRegistration = mode;
    if(!isRegistration) {
      fabAdd.setVisibility(View.INVISIBLE);
    }
  }

  private void onAddClick() {

    addPending = true;

  }

  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {
    final float textSizePx =
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);

    tracker = new MultiBoxTracker(this);


    try {
      detector =
              TFLiteObjectDetectionAPIModel.create(
                      getAssets(),
                      TF_OD_API_MODEL_FILE,
                      TF_OD_API_LABELS_FILE,
                      TF_OD_API_INPUT_SIZE,
                      TF_OD_API_IS_QUANTIZED);
      //cropSize = TF_OD_API_INPUT_SIZE;

      detector.reloadDataSet(isRegistration ? null : SaveDataSet.deSerializeHashMap(currentFaceData));

    } catch (final IOException e) {
      e.printStackTrace();
      LOGGER.e(e, "Exception initializing classifier!");
      Toast toast =
              Toast.makeText(
                      getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
      toast.show();
      finish();
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();

    sensorOrientation = rotation - getScreenOrientation();
    LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);


    int targetW, targetH;
    if (sensorOrientation == 90 || sensorOrientation == 270) {
      targetH = previewWidth;
      targetW = previewHeight;
    }
    else {
      targetW = previewWidth;
      targetH = previewHeight;
    }
    int cropW = (int) (targetW / 2.0);
    int cropH = (int) (targetH / 2.0);

    croppedBitmap = Bitmap.createBitmap(cropW, cropH, Config.ARGB_8888);

    portraitBmp = Bitmap.createBitmap(targetW, targetH, Config.ARGB_8888);
    faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Config.ARGB_8888);

    frameToCropTransform =
            ImageUtils.getTransformationMatrix(
                    previewWidth, previewHeight,
                    cropW, cropH,
                    sensorOrientation, MAINTAIN_ASPECT);

//    frameToCropTransform =
//            ImageUtils.getTransformationMatrix(
//                    previewWidth, previewHeight,
//                    previewWidth, previewHeight,
//                    sensorOrientation, MAINTAIN_ASPECT);

    cropToFrameTransform = new Matrix();
    frameToCropTransform.invert(cropToFrameTransform);


    Matrix frameToPortraitTransform =
            ImageUtils.getTransformationMatrix(
                    previewWidth, previewHeight,
                    targetW, targetH,
                    sensorOrientation, MAINTAIN_ASPECT);



    trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
    trackingOverlay.addCallback(
            new DrawCallback() {
              @Override
              public void drawCallback(final Canvas canvas) {
                tracker.draw(canvas);
                if (isDebug()) {
                  tracker.drawDebug(canvas);
                }
              }
            });

    tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
  }


  @Override
  protected void processImage() {
    ++timestamp;
    final long currTimestamp = timestamp;
    trackingOverlay.postInvalidate();

    // No mutex needed as this method is not reentrant.
    if (computingDetection) {
      readyForNextImage();
      return;
    }
    computingDetection = true;

    LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

    readyForNextImage();

    final Canvas canvas = new Canvas(croppedBitmap);
    canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
    // For examining the actual TF input.
    if (SAVE_PREVIEW_BITMAP) {
      ImageUtils.saveBitmap(croppedBitmap);
    }

    InputImage image = InputImage.fromBitmap(croppedBitmap, 0);
    faceDetector
            .process(image)
            .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
              @Override
              public void onSuccess(List<Face> faces) {
                if (faces.size() == 0) {
                  updateResults(currTimestamp, new LinkedList<>());
                  return;
                }

                for (Face face : faces) {
                  // Head is rotated to the right rotY degrees
                  float rotY = face.getHeadEulerAngleY();
                  // Head is tilted sideways rotZ degrees
                  float rotZ = face.getHeadEulerAngleZ();
                  Log.d("duong", "right: " + rotY + " z: " + rotZ);
                }

                // Big face filter
                List<Face> bigFaces = new ArrayList<>();

                for (Face face : faces) {
                  RectF faceBounding = new RectF(face.getBoundingBox());
                  if(faceBounding.width() >= MINIMUM_WIDTH_FACE_SIZE_TO_PROCESS_PROM_MLKIT
                          && faceBounding.height() >= MINIMUM_HEIGHT_FACE_SIZE_TO_PROCESS_PROM_MLKIT) {
                    bigFaces.add(face);
                  }
                }
                runInBackground(
                        new Runnable() {
                          @Override
                          public void run() {
                            onFacesDetected(currTimestamp, bigFaces, addPending);
                            addPending = false;
                          }
                        });
              }

            });


  }

  @Override
  protected int getLayoutId() {
    return R.layout.tfe_od_camera_connection_fragment_tracking;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }

  // Which detection model to use: by default uses Tensorflow Object Detection API frozen
  // checkpoints.
  private enum DetectorMode {
    TF_OD_API;
  }

  @Override
  protected void setUseNNAPI(final boolean isChecked) {
    runInBackground(() -> detector.setUseNNAPI(isChecked));
  }

  @Override
  protected void setNumThreads(final int numThreads) {
    runInBackground(() -> detector.setNumThreads(numThreads));
  }


  // Face Processing
  private Matrix createTransform(
          final int srcWidth,
          final int srcHeight,
          final int dstWidth,
          final int dstHeight,
          final int applyRotation) {

    Matrix matrix = new Matrix();
    if (applyRotation != 0) {
      if (applyRotation % 90 != 0) {
        LOGGER.w("Rotation of %d % 90 != 0", applyRotation);
      }

      // Translate so center of image is at origin.
      matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

      // Rotate around origin.
      matrix.postRotate(applyRotation);
    }

//        // Account for the already applied rotation, if any, and then determine how
//        // much scaling is needed for each axis.
//        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
//        final int inWidth = transpose ? srcHeight : srcWidth;
//        final int inHeight = transpose ? srcWidth : srcHeight;

    if (applyRotation != 0) {

      // Translate back from origin centered reference to destination frame.
      matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
    }

    return matrix;

  }

  private void showAddFaceDialog(SimilarityClassifier.Recognition rec) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View dialogLayout = inflater.inflate(R.layout.image_edit_dialog, null);
    ImageView ivFace = dialogLayout.findViewById(R.id.dlg_image);
    TextView nameRegister = dialogLayout.findViewById(R.id.dlg_name_register);
    TextView title = dialogLayout.findViewById(R.id.dlg_title);

    title.setText("Thông tin khuôn mặt đăng ký");
    nameRegister.setText("ID: " + currentRegisterId);
    ivFace.setImageBitmap(rec.getCrop());

    builder.setPositiveButton("ĐĂNG KÝ KHUÔN MẶT MỚI", (dlg, i) -> {
      MyCustomDialog loadingSpinner = new MyCustomDialog(DetectorActivity.this, "Tiến hành đăng kí...");
      loadingSpinner.startLoadingDialogNoAnim();

        // detector.register(name, rec);

        // Get embedding stored in Extra
        final float[] emb = ((float[][]) rec.getExtra())[0];
        String embeddingString = convertArrEmbToString(emb);
        dlg.dismiss();

        if (isTeacherRegistration) {
          postTeacherRegistrationAPI(currentRegisterId, embeddingString, loadingSpinner);
        } else {
          postStudentRegistrationAPI(currentRegisterId, embeddingString, loadingSpinner);
        }
    });
    builder.setView(dialogLayout);
    builder.show();

  }

  private void updateResults(long currTimestamp, final List<SimilarityClassifier.Recognition> mappedRecognitions) {

    tracker.trackResults(mappedRecognitions, currTimestamp);
    trackingOverlay.postInvalidate();
    computingDetection = false;
    //adding = false;


    if (mappedRecognitions.size() > 0) {
       LOGGER.i("Adding results");
       SimilarityClassifier.Recognition rec = mappedRecognitions.get(0);
       if (rec.getExtra() != null) {
         showAddFaceDialog(rec);
       }

    }

//    runOnUiThread(
//            new Runnable() {
//              @Override
//              public void run() {
//                showFrameInfo(previewWidth + "x" + previewHeight);
//                showCropInfo(croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
//                showInference(lastProcessingTimeMs + "ms");
//              }
//            });

  }

  private void onFacesDetected(long currTimestamp, List<Face> faces, boolean add) {

    cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
    final Canvas canvas = new Canvas(cropCopyBitmap);
    final Paint paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStyle(Style.STROKE);
    paint.setStrokeWidth(2.0f);

    float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
    switch (MODE) {
      case TF_OD_API:
        minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
        break;
    }

    final List<SimilarityClassifier.Recognition> mappedRecognitions =
            new LinkedList<SimilarityClassifier.Recognition>();


    //final List<Classifier.Recognition> results = new ArrayList<>();

    // Note this can be done only once
    int sourceW = rgbFrameBitmap.getWidth();
    int sourceH = rgbFrameBitmap.getHeight();
    int targetW = portraitBmp.getWidth();
    int targetH = portraitBmp.getHeight();
    Matrix transform = createTransform(
            sourceW,
            sourceH,
            targetW,
            targetH,
            sensorOrientation);
    final Canvas cv = new Canvas(portraitBmp);

    // draws the original image in portrait mode.
    cv.drawBitmap(rgbFrameBitmap, transform, null);

    final Canvas cvFace = new Canvas(faceBmp);

    boolean saved = false;

    for (Face face : faces) {

      LOGGER.i("FACE" + face.toString());
      LOGGER.i("Running detection on face " + currTimestamp);
      //results = detector.recognizeImage(croppedBitmap);

      final RectF boundingBox = new RectF(face.getBoundingBox());

      //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
      final boolean goodConfidence = true; //face.get;
      if (boundingBox != null && goodConfidence) {

        // maps crop coordinates to original
        cropToFrameTransform.mapRect(boundingBox);

        // maps original coordinates to portrait coordinates
        RectF faceBB = new RectF(boundingBox);
        transform.mapRect(faceBB);

        Log.d("duong", "W: " + faceBB.width() + " H: " + faceBB.height());

        // translates portrait to origin and scales to fit input inference size
        //cv.drawRect(faceBB, paint);
        float sx = ((float) TF_OD_API_INPUT_SIZE) / faceBB.width();
        float sy = ((float) TF_OD_API_INPUT_SIZE) / faceBB.height();
        Matrix matrix = new Matrix();
        matrix.postTranslate(-faceBB.left, -faceBB.top);
        matrix.postScale(sx, sy);

        cvFace.drawBitmap(portraitBmp, matrix, null);

        //canvas.drawRect(faceBB, paint);

        String label = "";
        float confidence = -1f;
        Integer color = Color.BLUE;
        Object extra = null;
        Bitmap crop = null;

        if (add) {
          crop = Bitmap.createBitmap(portraitBmp,
                            (int) faceBB.left,
                            (int) faceBB.top,
                            (int) faceBB.width(),
                            (int) faceBB.height());
        }

        final long startTime = SystemClock.uptimeMillis();
        final List<SimilarityClassifier.Recognition> resultsAux = detector.recognizeImage(faceBmp, add);
        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

        if (resultsAux.size() > 0) {

          SimilarityClassifier.Recognition result = resultsAux.get(0);

          extra = result.getExtra();
//          Object extra = result.getExtra();
//          if (extra != null) {
//            LOGGER.i("embeeding retrieved " + extra.toString());
//          }

          float conf = result.getDistance();
          if (conf < THRESHOLD_FOR_ACCEPT_DISTANCE && !isRegistration) {
            numOfTimeRecognized ++;
            confidence = conf;
            label = result.getTitle();
            if (result.getId().equals("0")) {
              color = Color.GREEN;
            }
            else {
              color = Color.RED;
            }

//            Bitmap cropImageForSendAPI = Bitmap.createBitmap(portraitBmp,
//                    (int) faceBB.left,
//                    (int) faceBB.top,
//                    (int) faceBB.width(),
//                    (int) faceBB.height());
//
//            moveToCheckAndSendAPI(label, confidence, cropImageForSendAPI);
          } else {
            numOfTimeNotRecognized++;
          }

          if(numOfTimeRecognized >= THRESHOLD_FOR_ACCEPTING_RESULT && !isRegistration && confidence > 0) {
            Bitmap cropImageForSendAPI = Bitmap.createBitmap(portraitBmp,
                    (int) faceBB.left,
                    (int) faceBB.top,
                    (int) faceBB.width(),
                    (int) faceBB.height());

            if (isForLogIn) {
              moveToLoginWithFace(label);
            } else {
              if(isCheckIn) {
                moveToFaceCheckInConfirm(label, confidence, cropImageForSendAPI);
              } else {
                moveToFaceCheckOutConfirm(label, confidence, cropImageForSendAPI);
              }
            }
          } else if (numOfTimeNotRecognized >= THRESHOLD_FOR_DENYING_RESULT && !isRegistration) {
            moveToFaceNotRecognized();
          }

        }

        if (getCameraFacing() == CameraCharacteristics.LENS_FACING_FRONT) {

          // camera is frontal so the image is flipped horizontally
          // flips horizontally
          Matrix flip = new Matrix();
          if (sensorOrientation == 90 || sensorOrientation == 270) {
            flip.postScale(1, -1, previewWidth / 2.0f, previewHeight / 2.0f);
          }
          else {
            flip.postScale(-1, 1, previewWidth / 2.0f, previewHeight / 2.0f);
          }
          //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
          flip.mapRect(boundingBox);

        }

        final SimilarityClassifier.Recognition result = new SimilarityClassifier.Recognition(
                "0", label.split("&")[0], confidence, boundingBox);

        result.setColor(color);
        result.setLocation(boundingBox);
        result.setExtra(extra);
        result.setCrop(crop);
        mappedRecognitions.add(result);

      }


    }

    //    if (saved) {
//      lastSaved = System.currentTimeMillis();
//    }

    updateResults(currTimestamp, mappedRecognitions);


  }

  public void moveToFaceCheckInConfirm(String name, float distance, Bitmap faceDetected) {
    Log.d("duong", name);
    Intent intent = new Intent(DetectorActivity.this, FaceCheckInConfirm.class);
    intent.putExtra("NameDetected", name);
    intent.putExtra("ConfidenceDetected", distance);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    faceDetected.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] bytes = stream.toByteArray();
    intent.putExtra("FaceDetected",bytes);

    startActivity(intent);
  }

  public void moveToFaceCheckOutConfirm(String name, float distance, Bitmap faceDetected) {
    Intent intent = new Intent(DetectorActivity.this, FaceCheckOutConfirm.class);
    intent.putExtra("NameDetected", name);
    intent.putExtra("ConfidenceDetected", distance);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    faceDetected.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] bytes = stream.toByteArray();
    intent.putExtra("FaceDetected",bytes);

    startActivity(intent);
  }

  public void moveToFaceNotRecognized() {
    Intent intent = new Intent(DetectorActivity.this, UnknownFace.class);
    startActivity(intent);
  }

  private void postStudentRegistrationAPI(String id, String embeddings, MyCustomDialog loadingSpinner) {
    Retrofit retrofit = APIClient.getClient();
    APIService apiCall = retrofit.create(APIService.class);

    Call<RegistrationResponse> call = apiCall.registerForStudent(id, embeddings);

    call.enqueue(new Callback<RegistrationResponse>() {
      @Override
      public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
        loadingSpinner.dismissDialog();
        if (response.isSuccessful()) {
          MyCustomDialog successSpinner = new MyCustomDialog(DetectorActivity.this, "Đăng kí khuôn mặt thành công");
          successSpinner.startSuccessMakeARollCallDialog();
        } else {
          MyCustomDialog failSpinner = new MyCustomDialog(DetectorActivity.this, "Có lỗi khi đăng kí khuôn mặt, thử lại");
          failSpinner.startErrorMakeARollCallDialog();
        }
      }

      @Override
      public void onFailure(Call<RegistrationResponse> call, Throwable t) {
        loadingSpinner.dismissDialog();
      }
    });
  }

  private void postTeacherRegistrationAPI(String id, String embeddings, MyCustomDialog loadingSpinner) {
    Retrofit retrofit = APIClient.getClient();
    APIService apiCall = retrofit.create(APIService.class);

    Call<RegistrationResponse> call = apiCall.registerForTeacher(id, embeddings);

    call.enqueue(new Callback<RegistrationResponse>() {
      @Override
      public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
        loadingSpinner.dismissDialog();
        if (response.isSuccessful()) {
          MyCustomDialog successSpinner = new MyCustomDialog(DetectorActivity.this, "Đăng kí khuôn mặt thành công");
          successSpinner.startSuccessMakeARollCallDialog();
        } else {
          MyCustomDialog failSpinner = new MyCustomDialog(DetectorActivity.this, "Có lỗi khi đăng kí khuôn mặt, thử lại");
          failSpinner.startErrorMakeARollCallDialog();
        }
      }

      @Override
      public void onFailure(Call<RegistrationResponse> call, Throwable t) {
        loadingSpinner.dismissDialog();
      }
    });
  }

  private String convertArrEmbToString(float[] emb) {
    String embArr = "";
    for (float feature : emb) {
      embArr = embArr + feature + "&";
    }

    return embArr;
  }

  public void moveToLoginWithFace(String extraData) {
    Intent intent = new Intent(DetectorActivity.this, FaceConfirmLogin.class);
    intent.putExtra("teacherName", extraData.split("&")[0]);
    intent.putExtra("accountId", extraData.split("&")[1]);
    startActivity(intent);
    finish();
  }
}
