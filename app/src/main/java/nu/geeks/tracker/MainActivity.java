package nu.geeks.tracker;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;


public class MainActivity extends Activity {
    private static final String TAG = "-------CAMERA--------";

    byte[] cameraInfo;


    Camera mCamera;
    Preview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        


        setCameraPreviewCallback();
        
        //Log.d(TAG, "Camera Info: " + cameraInfo.length);

    }

    private void setCameraPreviewCallback() {
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();

        int dataBufferSize=(int)(previewSize.height*previewSize.width*
                (ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat())/8.0));

        cameraInfo = new byte[dataBufferSize];

        //Log.d(TAG, "camera info " + cameraInfo.length + " Width: " + previewSize.width + " height: "
        //        + previewSize.height);

        mCamera.addCallbackBuffer(cameraInfo);

        Log.d(TAG, "data[0]: " + cameraInfo[0] + "/nData[1]: " + cameraInfo[1]);
        mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                cameraInfo = data;

                Log.d(TAG, "data[0]: " + data[0] + "/nData[1]: " + data[1]);
            }
        });
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
