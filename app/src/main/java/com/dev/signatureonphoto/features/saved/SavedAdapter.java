package com.dev.signatureonphoto.features.saved;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.R;
import com.jackandphantom.circularimageview.RoundedImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dev.signatureonphoto.Constants.FOLDER_NAME;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.GalleryHolder> {
    private Context context;
    private List<GalleryItem> listGallery;
    private int checkSelectedOld = -1;
    private SavedMvp savedMvp;


    public SavedAdapter(Context context, List<GalleryItem> listGallery, SavedMvp savedMvp) {
        this.context = context;
        this.listGallery = listGallery;
        this.savedMvp = savedMvp;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return listGallery.size();
    }


    class GalleryHolder extends RecyclerView.ViewHolder {
        private int position;
        @BindView(R.id.img_item)
        RoundedImage imgItem;
        @BindView(R.id.img_check_item)
        ImageView imgCheckItem;
        @BindView(R.id.img_delete)
        ImageView imgDelete;

        GalleryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.img_item, R.id.img_delete})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.img_item:
                    savedMvp.itemOnClicked(listGallery.get(position).getPath());
                    break;
                case R.id.img_delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alert!!");
                    builder.setIcon(R.drawable.ic_alert);
                    builder.setMessage("Are you sure to delete image?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(listGallery.get(position).getPath());
                            if (file.exists()) {
                                file.delete();
                                Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                                listGallery=getListImage();
                                notifyDataSetChanged();
                            }
                        }
                    });
                    builder.show();
                    break;
            }
        }

        void onBind(int position) {
            this.position = position;
            Glide.with(context).load(listGallery.get(position).getPath())
                    .into(imgItem);
        }
    }
    private ArrayList<GalleryItem>getListImage(){
        ArrayList<GalleryItem> imageList = new ArrayList<>();
        String pathFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + FOLDER_NAME;
        File dir = new File(pathFolder);
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getName().endsWith(".jpg")) {
                    File file = listFile[i];
                    File fileDate = new File(file.getPath());
                    Date lastModDate = new Date(fileDate.lastModified());
                    GalleryItem item = new GalleryItem(file.getPath(), lastModDate);
                    imageList.add(item);
                }
            }
        }
        return imageList;
    }
}
