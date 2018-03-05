/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shadowdata.hsbczxing.camera.open;

import android.hardware.Camera;
import android.util.Log;

/**
 * Abstraction over the {@link Camera} API that helps open them and return their metadata.
 */
@SuppressWarnings("deprecation") // camera APIs
public final class OpenCameraInterface {

  private static final String TAG = OpenCameraInterface.class.getName();

  /** For {@link #open(int)}, means no preference for which camera to open. */
  public static final int NO_REQUESTED_CAMERA = -1;

  private OpenCameraInterface() {
  }

  /**
   * Opens the requested camera with {@link Camera#open(int)}, if one exists.
   *
   * @param cameraId camera ID of the camera to use. A negative value
   *  or {@link #NO_REQUESTED_CAMERA} means "no preference", in which case a rear-facing
   *  camera is returned if possible or else any camera
   * @return handle to {@link OpenCamera} that was opened
   */
  public static Camera open(int cameraId) {

    int numCameras = Camera.getNumberOfCameras();
    if (numCameras == 0) {
      Log.w(TAG, "No cameras!");
      return null;
    }

    boolean explicitRequest = cameraId >= 0;

    if (!explicitRequest) {
      // Select a camera if no explicit camera requested
      int index = 0;
      while (index < numCameras) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(index, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
          break;
        }
        index++;
      }

      cameraId = index;
    }

    Camera camera;
    if (cameraId < numCameras) {
      Log.i(TAG, "Opening camera #" + cameraId);
      camera = Camera.open(cameraId);
    } else {
      if (explicitRequest) {
        Log.w(TAG, "Requested camera does not exist: " + cameraId);
        camera = null;
      } else {
        Log.i(TAG, "No camera facing back; returning camera #0");
        camera = Camera.open(0);
      }
    }

    return camera;
  }

  public static Camera open(){
    return open(-1);
  }

}
