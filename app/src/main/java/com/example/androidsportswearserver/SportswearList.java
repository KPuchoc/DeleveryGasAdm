package com.example.androidsportswearserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.androidsportswearserver.Common.Common;
import com.example.androidsportswearserver.Interface.ItemClickListener;
import com.example.androidsportswearserver.Model.Sportswear;
import com.example.androidsportswearserver.ViewHolder.SportswearViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class SportswearList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout rootLayout;

    FloatingActionButton fab;

    FirebaseDatabase db;
    DatabaseReference sportswearList;
    FirebaseStorage storage;
    StorageReference storageReference;


    String categotyId="";

    FirebaseRecyclerAdapter<Sportswear, SportswearViewHolder>adapter;
    MaterialEditText edtName,edtDescription,edtPrice,edtDiscout;
    Button btnSelect,btnUpload;
    Sportswear newSportswear;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportwear_list);

        db=FirebaseDatabase.getInstance();
        sportswearList=db.getReference("Sportswear");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        recyclerView=(RecyclerView)findViewById(R.id.recyclerSportswear);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSportswear(categotyId);
            }
        });
        if(getIntent()!=null)
            categotyId=getIntent().getStringExtra("CategoryId");
        if(!categotyId.isEmpty())
            loadListSportswear(categotyId);

    }

    private void showAddSportswear(String categotyId) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(SportswearList.this);
        alertDialog.setTitle("Agregar nueva Sportwear");
        alertDialog.setMessage("Por favor llena toda la informacion");

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_sportswear,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtDescription=add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscout=add_menu_layout.findViewById(R.id.edtDiscoubt);
        btnSelect=add_menu_layout.findViewById(R.id.BtnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.BtnUpload);




        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_car);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if(newSportswear!=null)
                {
                    sportswearList.push().setValue(newSportswear);
                    //   Toast.makeText(Home.this, "Nueva Categoria Agregada", Toast.LENGTH_SHORT).show();
                    Snackbar.make(rootLayout,"Nueva Sportwear"+newSportswear.getName()+"Fue Agregada",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }



    private void uploadImage() {
        if(saveUri!=null)
        {
            final ProgressDialog mDialog= new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(SportswearList.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newSportswear=new Sportswear();
                                    newSportswear.setName(edtName.getText().toString());
                                    newSportswear.setDescription(edtDescription.getText().toString());
                                    newSportswear.setPrice(edtPrice.getText().toString());
                                    newSportswear.setDiscount(edtDiscout.getText().toString());
                                    newSportswear.setId(categotyId);
                                    newSportswear.setImage(uri.toString());

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(SportswearList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecciona la Imagen"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadListSportswear(String categotyId) {
        adapter=new FirebaseRecyclerAdapter<Sportswear, SportswearViewHolder>(
                Sportswear.class,
                R.layout.sportswear_item,
                SportswearViewHolder.class,
                sportswearList.orderByChild("id").equalTo(categotyId)
        ) {
            @Override
            protected void populateViewHolder(SportswearViewHolder viewHolder, Sportswear model, int position) {
                viewHolder.sportswear_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.sportswear_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST&& resultCode==RESULT_OK
                && data!=null&&data.getData()!=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Imagen selecionada");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull  MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
          showUpdateSportswearDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteSportswear(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteSportswear(String key) {
        sportswearList.child(key).removeValue();
    }

    private void showUpdateSportswearDialog(String key, Sportswear item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(SportswearList.this);
        alertDialog.setTitle("Modificar Sportwear");
        alertDialog.setMessage("Por favor llena toda la informacion");

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_sportswear,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtDescription=add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscout=add_menu_layout.findViewById(R.id.edtDiscoubt);

        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());
        edtDiscout.setText(item.getDiscount());


        btnSelect=add_menu_layout.findViewById(R.id.BtnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.BtnUpload);




        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_car);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDiscount(edtDiscout.getText().toString());
                    item.setDescription(edtDescription.getText().toString());

                    sportswearList.child(key).setValue(item);
                    //   Toast.makeText(Home.this, "Nueva Categoria Agregada", Toast.LENGTH_SHORT).show();
                    Snackbar.make(rootLayout,"Nueva Sportwear"+item.getName()+"Fue Modificada",Snackbar.LENGTH_SHORT).show();
                }

        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }


    private void changeImage(final Sportswear item) {
        if(saveUri!=null)
        {
            ProgressDialog mDialog= new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(SportswearList.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //   newCategory=new Category(edtName.getText().toString(),uri.toString());
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(SportswearList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });
        }
    }
}