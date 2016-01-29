package com.tonykeiser.photomapping;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class FormFragment extends Fragment {

    private static final int REQUEST_TAKE_PICTURE = 0x9999;

    private EditText  title;
    private CheckBox  favorite;
    private Boolean   checked = false;
    private ImageView photo;
    private Button    save;
    private Button    add;
    private Uri       photoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Double latitude  =  getArguments().getDouble("latitude");
        final Double longitude =  getArguments().getDouble("longitude");

        View view = inflater.inflate(R.layout.fragment_form, container, false);

        title    = (EditText)  view.findViewById(R.id.titleText);
        favorite = (CheckBox)  view.findViewById(R.id.checkFavorite);
        photo    = (ImageView) view.findViewById(R.id.locationImage);
        save     = (Button)    view.findViewById(R.id.saveButton);
        add      = (Button)    view.findViewById(R.id.addPhoto);

        // Open Camera to Take Photo
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoUri = getPhotoUri();
                if(photoUri != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorite.isChecked()) {
                    checked = true;
                }
                if (photoUri == null) {
                    Toast.makeText(getActivity(), "Photo Required!", Toast.LENGTH_LONG).show();
                } else if (String.valueOf(title.getText()).trim().equals("")) {
                    Toast.makeText(getActivity(), "Title Cannot be Empty",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Save New Location
                    saveData(title.getText().toString(), photoUri.toString(),
                            checked, latitude, longitude);

                    // Go back to Map
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    MapsFragment frag = new MapsFragment();
                    ft.replace(R.id.fragContainer, frag);
                    ft.commit();
                }
            }
        });
        return view;
    }

    // Get Custom Unique URI to Save Photo in Gallery
    private Uri getPhotoUri() {

        String photoName = new SimpleDateFormat("MMddyyyy_HHmmss")
                .format(new Date(System.currentTimeMillis()));

        File imageDir = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES);

        File appDir = new File(imageDir, "Photo Mapping");
        appDir.mkdirs();

        File image = new File(appDir, photoName + ".jpg");
        try {
            image.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(image);
    }

    // On Photo Success! Display photo within ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode != getActivity().RESULT_CANCELED) {
            if(photoUri != null) {
                photo.setImageBitmap(BitmapFactory.decodeFile(photoUri.getPath()));
                addToPhotoGallery(photoUri);
            } else {
                photo.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
            }
        }
    }

    // Add Photo to the Phone's Gallery
    private void addToPhotoGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        getActivity().sendBroadcast(scanIntent);
    }

    // Save Serializable Data
    public void saveData(String title, String photo, Boolean favorite,
                                Double latitude, Double longitude) {
        ArrayList<PhotoMaps> arrayList = new ArrayList<>();
        // Load Data and Append New Data
        arrayList = readData();
        arrayList.add(new PhotoMaps(title, photo, favorite, latitude, longitude));
        try {
            FileOutputStream fos = new FileOutputStream(
                    new File(getActivity().getApplicationContext().getFilesDir(),"")+
                            File.separator+"photos.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayList);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Serializable Data
    public ArrayList readData() {
        ArrayList<PhotoMaps> photoMaps = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(
                    new File(getActivity().getApplicationContext().getFilesDir(),"")+
                            File.separator+"photos.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            photoMaps = (ArrayList<PhotoMaps>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoMaps;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
