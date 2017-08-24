package com.georgeren.mytoutiao.module.photo.content;

import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

/**
 * Created by georgeRen on 2017/8/23.
 * 图片的读写权限
 */

public class PhotoContentFragmentPermissionsDispatcher {
    private static final int REQUEST_ONSAVEIMAGE = 0;

    private static final String[] PERMISSION_ONSAVEIMAGE = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private PhotoContentFragmentPermissionsDispatcher() {
    }

    static void onSaveImageWithCheck(PhotoContentFragment target) {
        if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_ONSAVEIMAGE)) {
            target.onSaveImage();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ONSAVEIMAGE)) {
                target.showRationale(new OnSaveImagePermissionRequest(target));
            } else {
                target.requestPermissions(PERMISSION_ONSAVEIMAGE, REQUEST_ONSAVEIMAGE);
            }
        }
    }

    static void onRequestPermissionsResult(PhotoContentFragment target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ONSAVEIMAGE:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.onSaveImage();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ONSAVEIMAGE)) {
                        target.showNeverAsk();
                    } else {
                        target.showDenied();
                    }
                }
                break;
            default:
                break;
        }
    }


    private static final class OnSaveImagePermissionRequest implements PermissionRequest {
        private final WeakReference<PhotoContentFragment> weakTarget;

        private OnSaveImagePermissionRequest(PhotoContentFragment target) {
            this.weakTarget = new WeakReference<PhotoContentFragment>(target);
        }

        @Override
        public void proceed() {
            PhotoContentFragment target = weakTarget.get();
            if (target == null) return;
            target.requestPermissions(PERMISSION_ONSAVEIMAGE, REQUEST_ONSAVEIMAGE);
        }

        @Override
        public void cancel() {
            PhotoContentFragment target = weakTarget.get();
            if (target == null) return;
            target.showDenied();
        }
    }
}
