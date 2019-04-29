package com.gminney.collegeapp_minney;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.File;

import static com.backendless.media.SessionBuilder.TAG;




public class ProfileFragment extends Fragment {

    Profile mProfile = new Profile();

    private ImageButton mSelfieButton;
    private ImageView mSelfieView;
    private File mSelfieFile;
    private final int REQUEST_SELFIE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle bundle){
        super.onCreateView(inflater, view, bundle);

        View rootView = inflater.inflate(R.layout.fragment_profile, view, false);
        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        Backendless.Data.of(Profile.class).save(mProfile, new AsyncCallback<Profile>() {
            @Override
            public void handleResponse(Profile response) {
                Log.i(TAG, "Saved profile to Backendless");
            }
            public void handleFault(BackendlessFault fault) {
                Log.i(TAG, "Failed to save profile!" + fault.getMessage());
            }
        });
    }

    public File getPhotoFile() {
        File externalFilesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        return new File (externalFilesDir, mProfile.getPhotoFilename());
    }

    final Intent captureSelfie = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    boolean canTakeSelfie = mSelfieFile != null && captureSelfie.resolveActivity(getActivity().getPackageManager()) != null;
    mSelfieButton.setEnabled(canTakeSelfie);
    if (canTakeSelfie) {
        Uri uri = Uri.fromFile(mSelfieFile);
        captureSelfie.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    }
    mSelfieButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(captureSelfie, REQUEST_SELFIE);
        }
    });

}
