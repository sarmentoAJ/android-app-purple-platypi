package com.cmsc355.contactapp;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import yogesh.firzen.filelister.FileListerDialog;
import yogesh.firzen.filelister.OnFileSelectedListener;

import static com.cmsc355.contactapp.App.context;

public class FileHandler extends Activity {
    private String fileAsString;
    private File file;
    private byte[] byteRepresentation;
    private FileListerDialog fileListerDialog = FileListerDialog.createFileListerDialog(context);
    Activity activity;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_GALLERY = 1;

    public FileHandler(Activity activity) {
        this.activity = activity;
    }

    private String getFileAsString() {
        if (file.exists()) {
            try {
                byteRepresentation = FileUtils.readFileToByteArray(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            fileAsString = byteRepresentation.toString();
            return fileAsString;
        }
        return "";
    }

    public void addFileToContact(File file, Contact contact) {
        contact.addAttribute(file.getName(),getFileAsString());
    }

    public File chooseFile() {
        fileListerDialog.setOnFileSelectedListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(File selectedFile, String path) {
                file = selectedFile;
            }
        });
        fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.ALL_FILES);
        fileListerDialog.show();
        return file;
    }

    public void choosePicture() {
        final String[] options = {"Take Photo","Choose From Library","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selected) {
                switch (options[selected]) {
                      case "Take Photo":
                          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                          activity.startActivityForResult(intent,REQUEST_CAMERA);
                          break;
                      case "Choose From Library":
                          Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                          galleryIntent.setType("image/*");
                          activity.startActivityForResult(galleryIntent, REQUEST_GALLERY);
                          break;
                      case "Cancel":
                          dialog.dismiss();
                          break;
                      default:
                          break;
                }
            }
        });
        builder.show();
    }

    //What to do when startActivityForResult returns
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = activity.findViewById(R.id.info_pic);

        //Check if
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_GALLERY) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = null;
        if (data.getExtras() != null) {
            thumbnail = (Bitmap) data.getExtras().get("data");
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        }
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ImageView img = findViewById(R.id.info_pic);
        img.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        ImageView img = activity.findViewById(R.id.info_pic);
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            img.setImageBitmap(selectedImage);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
