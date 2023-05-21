package kontesthelperbyamitmaity.example.atask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    String currentUserID;
    RelativeLayout relativeLayout;
    ImageView profile_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeMethods();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container_nav,
                        new HomeFragment()).commit();
        bottomMenu();




    }

    private void InitializeMethods() {

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        chipNavigationBar.setItemSelected(R.id.nav_home,
                true);
    }



    public void onBackPressed(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this,R.style.AlertDialogTheme);
        builder.setTitle("Confirm Exit");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Do you really want to exit?");
        builder.setBackground(getResources().getDrawable(R.drawable.material_dialog_box , null));
        builder.setCancelable(false);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Exit cancelled", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Fragment fragment = null;
                        switch (i){
                            case R.id.nav_home:
                                fragment = new HomeFragment();
                                break;
                            case R.id.nav_new_archive:
                                fragment = new FavLCFragment();
                                break;
                            case R.id.nav_new_ranking:
                                fragment = new FavCFFragment();
                                break;
                            case R.id.nav_settings:
                                fragment = new SettingsFragment();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container_nav,
                                        fragment).commit();

                    }
                });
    }
}