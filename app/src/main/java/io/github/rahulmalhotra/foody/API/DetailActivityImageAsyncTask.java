package io.github.rahulmalhotra.foody.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivityImageAsyncTask extends AsyncTask<URL, Void, Bitmap> {

    ImageView imageView;

    public DetailActivityImageAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        this.imageView.setImageBitmap(bitmap);
        super.onPostExecute(bitmap);
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap bitmap = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
